package com.example.sampletask2.di.component

import com.example.sampletask2.di.ActivityScope
import com.example.sampletask2.di.module.ActivityModule
import com.example.sampletask2.ui.main.MainActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: MainActivity)
}