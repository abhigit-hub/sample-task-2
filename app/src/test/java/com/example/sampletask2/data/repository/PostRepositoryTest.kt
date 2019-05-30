package com.example.sampletask2.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.sampletask2.R
import com.example.sampletask2.data.local.db.DatabaseService
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.data.local.prefs.UserPreferences
import com.example.sampletask2.data.remote.NetworkService
import com.example.sampletask2.rx.TestSchedulerProvider
import com.example.sampletask2.utils.common.Resource
import com.example.sampletask2.utils.network.NetworkHelper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.internal.util.reflection.FieldSetter
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.reflect.Whitebox
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(MockitoJUnitRunner::class)
class PostRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var postRepositoryTest: PostRepository

    private lateinit var testScheduler: TestScheduler

    private lateinit var compositeDisposable: CompositeDisposable

    @Mock
    private lateinit var networkService: NetworkService

    @Mock
    private lateinit var databaseService: DatabaseService

    @Mock
    private lateinit var userPreferences: UserPreferences

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var tabNameObserver: Observer<String>

    @Mock
    private lateinit var selectedPostObserver: Observer<StoryEntity>

    @Mock
    private lateinit var dataSetupStatusObserver: Observer<Resource<Int>>

    private val TAB_NAME = "Trending"

    @Before
    fun setUp() {
        compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)

        postRepositoryTest = PostRepository(
            networkService,
            databaseService,
            testSchedulerProvider,
            userPreferences,
            networkHelper
        )

        postRepositoryTest.getTabNameLiveData().observeForever(tabNameObserver)
        postRepositoryTest.getSelectedPostLiveData().observeForever(selectedPostObserver)
        postRepositoryTest.getDataSetUpStatusLiveData().observeForever(dataSetupStatusObserver)
    }

    @Test
    fun givenRepoSettingUp_whenInitCalled_shouldReturnFalse() {
        val fetchingData = AtomicBoolean(true)
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("fetchingData"),
            fetchingData
        )

        val returnValue = postRepositoryTest.init(compositeDisposable)
        assert(returnValue == fetchingData.get())
    }

    @Test
    fun givenRepoAlreadySetUp_whenInitCalled_shouldReturnTrue() {
        val fetchingData = AtomicBoolean(false)
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("fetchingData"),
            fetchingData
        )

        val returnValue = postRepositoryTest.init(compositeDisposable)
        assert(returnValue == fetchingData.get())
    }

    @Test
    fun givenDataFetchInProgress_whenRefreshStaleDataCalled_shouldReturnNullObservable() {
        val preFetchingState = AtomicBoolean(true)
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("fetchingData"),
            preFetchingState
        )

        val observable = postRepositoryTest.refreshStaleData()
        assert(Objects.isNull(observable))
    }

    @Test
    fun givenTabStateChanged_whenUpdateTabNameCalled_shouldUpdateTabInfo() {
        postRepositoryTest.updateTabName(TAB_NAME)
        verify(userPreferences).setTabName(TAB_NAME)
        verify(tabNameObserver).onChanged(TAB_NAME)
    }

    @Test
    fun givenSelectedStoryUpdated_whenUpdateSelectedStoryCalled_shouldUpdateSelectedPost() {
        val storyEntity = StoryEntity(
            1, "1_01", "Abhijit Naik", "Headline", "Summary",
            "URL", true, 1
        )

        postRepositoryTest.updateSelectedStory(storyEntity)
        verify(selectedPostObserver).onChanged(storyEntity)
    }

    @Test
    fun givenFragmentDestroyed_whenOnClearedCalled_shouldClearSelectedPost() {
        postRepositoryTest.onCleared()
        verify(selectedPostObserver).onChanged(null)
    }

    @Test
    fun givenDataSetUp_whenFetchDataSetCalled_shouldShowDataSetUpAlert() {
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("FLAG_POSTS_SETUP"),
            true
        )
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("fetchingData"),
            AtomicBoolean(false)
        )

        postRepositoryTest.init(compositeDisposable)
        postRepositoryTest.fetchDataSet()
        testScheduler.triggerActions()

        assert(postRepositoryTest.getDataSetUpStatusLiveData().value?.data == R.string.data_setup_complete)
    }

    @Test
    fun givenNoInternet_whenFetchDataSetCalled_shouldShowNetworkError() {
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("fetchingData"),
            AtomicBoolean(false)
        )
        FieldSetter.setField(
            postRepositoryTest,
            postRepositoryTest.javaClass.getDeclaredField("FLAG_POSTS_SETUP"),
            false
        )
        doReturn(false).`when`(userPreferences).isDataConfigured()
        doReturn(false).`when`(networkHelper).isNetworkConnected()

        postRepositoryTest.init(compositeDisposable)
        postRepositoryTest.fetchDataSet()
        testScheduler.triggerActions()

        assert(postRepositoryTest.getDataSetUpStatusLiveData().value?.data == R.string.network_connection_error)
    }

    @After
    fun tearDown() {
        postRepositoryTest.getTabNameLiveData().removeObserver(tabNameObserver)
        postRepositoryTest.getSelectedPostLiveData().removeObserver(selectedPostObserver)
        postRepositoryTest.getDataSetUpStatusLiveData().removeObserver(dataSetupStatusObserver)
    }
}