package com.example.sampletask2.di.component

import com.example.sampletask2.di.FragmentScope
import com.example.sampletask2.di.module.FragmentModule
import com.example.sampletask2.ui.main.detail.DetailFragment
import com.example.sampletask2.ui.main.home.HomeFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: DetailFragment)
}