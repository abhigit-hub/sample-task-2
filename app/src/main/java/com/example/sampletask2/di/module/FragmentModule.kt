package com.example.sampletask2.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.ui.main.MainSharedViewModel
import com.example.sampletask2.ui.main.home.posts.PostAdapter
import com.example.sampletask2.utils.ViewModelProviderFactory
import com.example.sampletask2.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    fun provideFragment(): Fragment = fragment

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideMainSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        postRepository: PostRepository
    ): MainSharedViewModel =
        ViewModelProviders.of(fragment.requireActivity(),
            ViewModelProviderFactory(MainSharedViewModel::class) {
                MainSharedViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    postRepository
                )
            }).get(MainSharedViewModel::class.java)

    @Provides
    fun providePostAdapter() = PostAdapter(ArrayList())
}