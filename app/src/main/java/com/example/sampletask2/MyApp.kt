package com.example.sampletask2

import android.app.Application
import com.example.sampletask2.di.component.ApplicationComponent
import com.example.sampletask2.di.component.DaggerApplicationComponent
import com.example.sampletask2.di.module.ApplicationModule
import com.facebook.stetho.Stetho

class MyApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
        Stetho.initializeWithDefaults(this)
    }

    private fun injectDependencies() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}