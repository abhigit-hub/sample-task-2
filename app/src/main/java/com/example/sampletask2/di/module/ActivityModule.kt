package com.example.sampletask2.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.ui.main.MainSharedViewModel
import com.example.sampletask2.utils.ViewModelProviderFactory
import com.example.sampletask2.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideActivity(): AppCompatActivity = activity

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        postRepository: PostRepository
    ): MainSharedViewModel = ViewModelProviders.of(
        activity,
        ViewModelProviderFactory(MainSharedViewModel::class) {
            MainSharedViewModel(
                schedulerProvider,
                compositeDisposable,
                postRepository
            )
        }).get(MainSharedViewModel::class.java)
}