package com.example.sampletask2.bg.work

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.example.sampletask2.MyApp
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.component.DaggerWorkerComponent
import com.example.sampletask2.di.module.WorkerModule
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DailySyncWorker(context: Context, workerParams: WorkerParameters) :
    RxWorker(context, workerParams) {


    companion object {

        const val TAG = "DailySyncWorker"
    }

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var postRepository: PostRepository

    init {
        injectDependencies()
    }

    private fun injectDependencies() {
        DaggerWorkerComponent.builder()
            .applicationComponent((applicationContext as MyApp).applicationComponent)
            .workerModule(WorkerModule())
            .build()
            .inject(this)
    }

    override fun createWork(): Single<Result> {
        postRepository.init(compositeDisposable)
        return Single.create {
            val disposable = postRepository.refreshStaleData()?.subscribe({

            }, {
                Result.failure()
            }, {
                Result.success()
            })
            disposable?.let { compositeDisposable.add(it) }
        }
    }

    override fun onStopped() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
        super.onStopped()
    }
}
