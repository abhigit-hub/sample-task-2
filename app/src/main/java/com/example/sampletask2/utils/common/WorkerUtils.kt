package com.example.sampletask2.utils.common

import androidx.work.*
import com.example.sampletask2.bg.work.DailySyncWorker
import java.util.concurrent.TimeUnit

object WorkerUtils {

    fun setUpWork() {
        if (!isWorkScheduled(DailySyncWorker.TAG)) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequest.Builder(DailySyncWorker::class.java, 1, TimeUnit.DAYS)
                .addTag(DailySyncWorker.TAG)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueue(request)
        }
    }

    private fun isWorkScheduled(tag: String): Boolean {
        val workInfosByTag = WorkManager.getInstance().getWorkInfosByTag(tag)

        var isRunning = false
        val workInfos = workInfosByTag.get()

        workInfos.forEach {
            if (it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED) {
                isRunning = true
                return isRunning
            }
        }

        return isRunning
    }
}