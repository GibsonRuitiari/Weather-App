package com.gibsoncodes.weatherapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.gibsoncodes.weatherapp.R
import com.gibsoncodes.weatherapp.ui.activities.MainScreen

class NotificationsClass(private val context:Context):ContextWrapper(context) {
   companion object{
      const val channelId="Channel Id"
   }
    init{
        createNotificationChannel()
    }
    private fun createNotificationChannel(){
        // api 26>
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name= "channelName"
            val descriptionText= "Test description text"
            val importance= NotificationManager.IMPORTANCE_DEFAULT
            val channel= NotificationChannel(
                channelId,
                name,importance).apply {
                description=descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }
     fun notificationsBuilder():NotificationCompat.Builder{
        val intent=Intent(this, MainScreen::class.java).apply {
            flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent:PendingIntent= PendingIntent.getActivity(context,
            0,intent,0)

        return NotificationCompat.Builder(context,
            channelId
        )
            .setSmallIcon(R.drawable.water)
            .setContentTitle("Weather Alert")
            .setContentText("Check the latest weather stats for your preferred  city")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
}