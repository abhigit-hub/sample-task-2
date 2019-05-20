package com.example.sampletask2.di.component

import com.example.sampletask2.bg.work.DailySyncWorker
import com.example.sampletask2.di.WorkerScope
import com.example.sampletask2.di.module.WorkerModule
import dagger.Component

@WorkerScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [WorkerModule::class]
)
interface WorkerComponent {

    fun inject(dailySyncWorker: DailySyncWorker)
}