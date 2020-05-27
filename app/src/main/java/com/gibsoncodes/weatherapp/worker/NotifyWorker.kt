package com.gibsoncodes.weatherapp.worker

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotifyWorker(val context: Context,params: WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        val notifications= NotificationsClass(context)
            with(NotificationManagerCompat.from(context)){
                notify(1,notifications.notificationsBuilder().build())
            }

        return Result.success()
    }

}