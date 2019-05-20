package com.example.sampletask2.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.sampletask2.BuildConfig
import com.example.sampletask2.MyApp
import com.example.sampletask2.data.local.db.DatabaseService
import com.example.sampletask2.data.local.prefs.UserPreferences
import com.example.sampletask2.data.remote.NetworkService
import com.example.sampletask2.data.remote.Networking
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.ApplicationContext
import com.example.sampletask2.utils.network.NetworkHelper
import com.example.sampletask2.utils.rx.RxSchedulerProvider
import com.example.sampletask2.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider =
        RxSchedulerProvider()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        application.getSharedPreferences("sample-task-2-prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDatabaseService(): DatabaseService =
        Room.databaseBuilder(
            application, DatabaseService::class.java,
            "sample-task-2-db"
        ).build()

    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService =
        Networking.create(
            BuildConfig.BASE_URL,
            application.cacheDir,
            10 * 1024 * 1024 // 10MB
        )

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

    @Singleton
    @Provides
    fun providePostRepository(
        networkService: NetworkService,
        databaseService: DatabaseService,
        schedulerProvider: SchedulerProvider,
        userPreferences: UserPreferences,
        networkHelper: NetworkHelper
    ): PostRepository =
        PostRepository(networkService, databaseService, schedulerProvider, userPreferences, networkHelper)
}