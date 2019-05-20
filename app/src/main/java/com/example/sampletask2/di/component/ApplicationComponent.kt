package com.example.sampletask2.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.sampletask2.MyApp
import com.example.sampletask2.data.local.db.DatabaseService
import com.example.sampletask2.data.local.prefs.UserPreferences
import com.example.sampletask2.data.remote.NetworkService
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.ApplicationContext
import com.example.sampletask2.di.module.ApplicationModule
import com.example.sampletask2.utils.network.NetworkHelper
import com.example.sampletask2.utils.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: MyApp)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getNetworkService(): NetworkService

    fun getDatabaseService(): DatabaseService

    fun getSharedPreferences(): SharedPreferences

    fun getUserPreferences(): UserPreferences

    fun getNetworkHelper(): NetworkHelper

    fun getSchedulerProvider(): SchedulerProvider

    fun getCompositeDisposable(): CompositeDisposable

    fun getPostRepository(): PostRepository
}