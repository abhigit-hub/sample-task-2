package com.example.sampletask2.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sampletask2.R
import com.example.sampletask2.data.local.db.DatabaseService
import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.data.local.prefs.UserPreferences
import com.example.sampletask2.data.model.StoryItem
import com.example.sampletask2.data.remote.NetworkService
import com.example.sampletask2.data.remote.response.StoryResponse
import com.example.sampletask2.utils.common.Resource
import com.example.sampletask2.utils.log.Logger
import com.example.sampletask2.utils.network.NetworkHelper
import com.example.sampletask2.utils.rx.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Singleton

@Singleton
class PostRepository constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val schedulerProvider: SchedulerProvider,
    private val userPreferences: UserPreferences,
    private val networkHelper: NetworkHelper
) {

    companion object {
        /*FLAG to avoid refreshing data source every time this repository is accessed*/
        private var FLAG_POSTS_SETUP = false
        const val TAG = "PostRepository"

        /*
        * Counter to keep track of collection entity sizes.
        * This counter will be used to switch flow/stream (From API retrieval to DB retrieval flow)
        * If there are 3 Collection entities, there will be 3 subscriptions made, we need to wait until
        * all three streams are complete
        * */
        private var collectionCounter = 0
        private var subscriptionCounter = 0
    }

    /*
    * This variable has to be made Atomic Boolean because there are two different threads accessing the
    * fetchData() method. If not handled the API call may occur twice, resulting in duplicate calls and duplicate
    * record insertions
    * 1. One call comes from the UI to access fetchData()
    * 2. Another call may or may not come from the WorkManager work(depends on a first launch)
    * */
    private var fetchingData: AtomicBoolean = AtomicBoolean(false)
    private lateinit var compositeDisposable: CompositeDisposable

    private val postsLiveData: MutableLiveData<List<Any>> = MutableLiveData()
    private val dataSetupStatusLiveData: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val tabNameLiveData: MutableLiveData<String> = MutableLiveData()
    private val selectedPostLiveData: MutableLiveData<StoryEntity> = MutableLiveData()

    fun getPostsLiveData(): LiveData<List<Any>> = postsLiveData
    fun getDataSetUpStatusLiveData(): LiveData<Resource<Int>> = dataSetupStatusLiveData
    fun getTabNameLiveData(): LiveData<String> = tabNameLiveData
    fun getSelectedPostLiveData(): LiveData<StoryEntity> = selectedPostLiveData

    fun init(compositeDisposable: CompositeDisposable) {
        this.compositeDisposable = compositeDisposable
    }

    fun initiateDataSetUp() {
        if (this.compositeDisposable != null) {
            fetchData()
        }
    }

    /*
    * Control logic method to check what state the app is in and based on that
    * decide the source to retrieve data from.
    *
    * */
    private fun fetchData() {
        if (!fetchingData.getAndSet(true)) {
            dataSetupStatusLiveData.postValue(Resource.loading(R.string.fetching_data))
            when {
                //1. Do Nothing
                FLAG_POSTS_SETUP -> {
                    dataSetupStatusLiveData.postValue(Resource.success(R.string.data_setup_complete))
                }

                //2. If offline data is available, load from DB
                userPreferences.isDataConfigured() -> {
                    dataSetupStatusLiveData.postValue(Resource.loading(R.string.fetching_data))
                    compositeDisposable.add(fetchDataFromDb().subscribe({ list ->
                        FLAG_POSTS_SETUP = true
                        list?.let { postsLiveData.postValue(list) }
                    }, {
                        dataSetupStatusLiveData.postValue(Resource.error(R.string.something_went_wrong))
                    }, {
                        dataSetupStatusLiveData.postValue(Resource.success(R.string.data_setup_complete))
                    }
                    ))
                }

                //3. If offline data is unavailable, load from API and save in DB
                else -> {
                    if (checkInternetConnectionWithMessage()) {
                        dataSetupStatusLiveData.postValue(Resource.loading(R.string.fetching_data))
                        compositeDisposable.add(fetchDataFromApi().subscribe({}, {
                            fetchingData.set(false)
                            dataSetupStatusLiveData.postValue(Resource.error(R.string.something_went_wrong))
                        }, {
                            fetchingData.set(false)
                        }
                        ))
                    } else fetchingData.set(false)
                }
            }
        }
    }


    /*
    * This method takes care of making HomePost(main) Api Call and format the Response received
    * and insert all the records into the Database for data persistency
    *
    * It also triggers a call to fetchMoreStories()
    * */
    private fun fetchDataFromApi(): Observable<Long> {
        var cachedCollectionId: Long = 0

        return networkService.doHomePostListApiCall().toObservable()
            .subscribeOn(schedulerProvider.io())
            .doOnNext { postListResponse -> postListResponse?.name?.let { userPreferences.setTabName(it) } }
            .concatMap { postListResponse ->
                Observable.zip(
                    deleteCollection().subscribeOn(schedulerProvider.io()).toObservable(),
                    deleteStory().subscribeOn(schedulerProvider.io()).toObservable(),
                    BiFunction<Int, Int, Int> { o1, o2 ->
                        return@BiFunction o1
                    })
                    .doOnError { throwable -> Logger.d(TAG, throwable.toString()) }
                    .ignoreElements().andThen(Observable.just(postListResponse))
            }
            .concatMap { postListResponse -> Observable.fromIterable(postListResponse.responseList) }
            .filter { it.has("type") }
            .concatMap { jsonResponse -> formatHomeApiResponse(jsonResponse, cachedCollectionId) }
            .concatMap { entity -> insertToDBBasedOnEntity(entity) }
            .subscribeOn(schedulerProvider.io())
            .doOnNext { collectionId ->
                fetchMoreStories(collectionId)
                cachedCollectionId = collectionId
            }
    }


    /*
    * This method takes care of fetching all Collection items and making an API call to fetch all nested
    * stories for each Collection item. And eventually the stories fetched are stored into local Database
    *
    * */
    @SuppressLint("CheckResult")
    private fun fetchMoreStories(collectionId: Long) {

        getCollectionById(collectionId)
            .concatMap { collectionEntity -> getStoriesApiCall(collectionEntity) }
            .concatMap { response -> Observable.fromIterable(response.items) }
            .concatMap { storyItem -> formatStoryApiResponse(storyItem, collectionId) }
            .concatMap { storyEntity -> insertStory(storyEntity) }
            .subscribeOn(schedulerProvider.io())
            .subscribe({}, {}, {
                subscriptionCounter += 1
                if (subscriptionCounter == collectionCounter) {
                    fetchingData.set(false)
                    fetchData()
                    userPreferences.setDataConfigured(true)
                    dataSetupStatusLiveData.postValue(Resource.success(R.string.data_setup_complete))
                }
            })
    }


    /*
    * This method takes care of fetching all Collection and Story items from the local Database
    * and formats the aggregated list in a manner presentable to the UI
    *
    * */
    private fun fetchDataFromDb(): Observable<List<Any>> {
        val collectionList: MutableList<Any> = ArrayList()

        return getAllCollections()
            .flatMap { list -> Observable.fromIterable(list) }
            .concatMap { collectionEntity ->
                collectionList.add(collectionEntity)
                getStoriesByCollectionId(collectionEntity.id)
            }
            .concatMap { list ->
                if (list.isNotEmpty() &&
                    collectionList[collectionList.size - 1] is CollectionEntity &&
                    (collectionList[collectionList.size - 1] as CollectionEntity).id == list[0].collectionId
                ) {
                    collectionList.addAll(list)
                }
                return@concatMap Observable.just(collectionList.toList())
            }
            .subscribeOn(schedulerProvider.io())
    }


    /*
    * This method takes care of refreshing all Collection and Story items from the Remote API
    * and updates the Local Database.
    *
    * The Worker decides the rate of refreshing.
    * */
    fun refreshStaleData(): Observable<Any>? {
        var observable: Observable<Any>? = null

        if (!fetchingData.get())
            compositeDisposable.add(
                fetchDataFromApi()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({}, {
                        observable = Observable.error(it)
                    }, {
                        observable = Observable.empty()
                    }
                    )
            )
        return observable
    }

    /*
    * In scenarios such as failure in fetching of data or no internet connection, this method is used
    * to retry fetching/preparing of data set
    *
    * */
    fun retryFetchingData() {
        if (!userPreferences.isDataConfigured())
            initiateDataSetUp()
    }


    /*
    *
    * Helper functions in order to either create an Observable or alter an existing Observable stream.
    * These functions are used as components to plugin to the the RxStreams(or compose with other Observables)
    * in mostly all of the above methods.
    * */
    private fun getAllCollections(): Observable<List<CollectionEntity>> =
        Observable.fromCallable {
            databaseService.collectionDao().getAllCollections()
        }

    private fun getCollectionById(collectionId: Long): Observable<CollectionEntity> =
        Observable.fromCallable {
            val collectionEntity: CollectionEntity = databaseService.collectionDao().getCollectionById(collectionId)
            collectionEntity?.run { return@fromCallable collectionEntity }
        }

    private fun getStoriesByCollectionId(collectionId: Long): Observable<List<StoryEntity>> =
        Observable.fromCallable {
            databaseService.storyDao().getStoriesByCollectionId(collectionId)
        }

    private fun getStoriesApiCall(collectionEntity: CollectionEntity): Observable<StoryResponse> =
        networkService.doStoriesApiCall(collectionEntity.url)
            .toObservable().subscribeOn(schedulerProvider.io())


    private fun insertStory(storyEntity: StoryEntity): Observable<Long> =
        Observable.fromCallable { databaseService.storyDao().insertStory(storyEntity) }

    private fun deleteCollection(): Single<Int> =
        Single.fromCallable { databaseService.collectionDao().nukeCollectionTable() }

    private fun deleteStory(): Single<Int> = Single.fromCallable { databaseService.storyDao().nukeStoryTable() }

    private fun formatStoryApiResponse(storyItem: StoryItem, collectionId: Long): Observable<StoryEntity> {
        val storyEntity = StoryEntity(
            0,
            storyItem.id,
            storyItem.story.authorName,
            storyItem.story.headline,
            storyItem.story.summary,
            storyItem.story.heroImage,
            true,
            collectionId
        )
        return Observable.just(storyEntity)
    }

    private fun formatHomeApiResponse(jsonResponse: JsonObject, cachedCollectionId: Long): Observable<Any> {
        val gson = Gson()
        return if (jsonResponse.get("type").asString == "collection") {
            val collectionEntity = gson.fromJson(jsonResponse.toString(), CollectionEntity::class.java)
            collectionEntity.apply {
                this.collectionId = this.id
                this.id = 0
            }
            collectionCounter++
            Observable.just(collectionEntity)
        } else {
            val storyItem = gson.fromJson(jsonResponse.toString(), StoryItem::class.java)
            val storyEntity = StoryEntity(
                0,
                storyItem.id,
                storyItem.story.authorName,
                storyItem.story.headline,
                storyItem.story.summary,
                storyItem.story.heroImage,
                false,
                cachedCollectionId
            )
            Observable.just(storyEntity)
        }
    }

    private fun insertToDBBasedOnEntity(entity: Any): Observable<Long> {
        return when (entity) {
            is CollectionEntity -> Observable.fromCallable {

                databaseService.collectionDao().insertCollection(entity)
            }
            is StoryEntity -> Observable.fromCallable {
                databaseService.storyDao().insertStory(entity)
                -1L
            }
            else -> Observable.fromCallable { -1L }
        }
    }


    /*
    * These methods are events propagated from the Adapter's ViewModel.
    * Any updates to the Collection/Story entity must be recorded in the Repository layer
    * */
    fun updateTabName(name: String) {
        userPreferences.setTabName(name)
        tabNameLiveData.postValue(name)
    }

    fun updateSelectedStory(storyEntity: StoryEntity?) {
        storyEntity?.let {
            selectedPostLiveData.postValue(it)
        }
    }


    /*Clear any unnecessary state information in Repository*/
    fun onCleared() {
        selectedPostLiveData.postValue(null)
    }

    private fun checkInternetConnectionWithMessage(): Boolean =
        if (networkHelper.isNetworkConnected()) {
            true
        } else {
            dataSetupStatusLiveData.postValue(Resource.connectionFailure(R.string.network_connection_error))
            false
        }
}
