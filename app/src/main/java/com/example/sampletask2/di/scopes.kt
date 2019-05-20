package com.example.sampletask2.di

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class FragmentScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class WorkerScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ViewModelScope