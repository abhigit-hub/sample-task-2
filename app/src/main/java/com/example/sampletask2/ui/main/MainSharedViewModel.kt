package com.example.sampletask2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.utils.common.Resource
import com.example.sampletask2.utils.common.Status
import com.example.sampletask2.utils.common.WorkerUtils
import com.example.sampletask2.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainSharedViewModel(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable,
    private val postRepository: PostRepository
) : ViewModel() {

    private val messageStringId: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val messageString: MutableLiveData<Resource<String>> = MutableLiveData()
    private val snackbarMessage: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val tabName: LiveData<String> = postRepository.getTabNameLiveData()
    private val posts: LiveData<List<Any>> = postRepository.getPostsLiveData()
    private val selectedStory: LiveData<StoryEntity> = postRepository.getSelectedPostLiveData()
    private val loading: LiveData<Boolean> = Transformations
        .map(postRepository.getDataSetUpStatusLiveData()) { resource ->
            resource?.let {
                if (it.status == Status.LOADING || it.status == Status.ERROR)
                    messageStringId.postValue(it)
                else if (it.status == Status.INTERNET_LOST) snackbarMessage.postValue(it)

                return@map it.status == Status.LOADING
            }
        }

    fun getMessageStringId(): LiveData<Resource<Int>> = messageStringId
    fun getMessageString(): LiveData<Resource<String>> = messageString
    fun getSnackbarMessage(): LiveData<Resource<Int>> = snackbarMessage
    fun getTabName(): LiveData<String> = tabName
    fun getPosts(): LiveData<List<Any>> = posts
    fun getSelectedStory(): LiveData<StoryEntity> = selectedStory
    fun isLoading(): LiveData<Boolean> = loading

    fun onViewCreated() {
        postRepository.init(compositeDisposable)
        postRepository.initiateDataSetUp()
        WorkerUtils.setUpWork()
    }

    fun onRetryClicked() {
        postRepository.retryFetchingData()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        postRepository.onCleared()
        super.onCleared()
    }
}