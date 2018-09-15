package com.alekseyvalyakin.roleplaysystem.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.alekseyvalyakin.roleplaysystem.R

class NotificationInteractorImpl(
        private val context: Context
) : NotificationInteractor {
    private val channelId = context.getString(R.string.default_notification_channel_id)
    private fun commonBuilder() = NotificationCompat.Builder(context, channelId)
    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Show notification with a progress bar.
     */
    override fun showProgressNotification(notifId: Int, caption: String, completedUnits: Long, totalUnits: Long) {
        var percentComplete = 0

        if (totalUnits > 0) {
            percentComplete = (100 * completedUnits / totalUnits).toInt()
        }

        val builder = commonBuilder()
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(caption)
                .setProgress(100, percentComplete, false)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setAutoCancel(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(notifId, builder.build())
    }

    override fun dismissNotification(notificationId: Int) {
        manager.cancel(notificationId)
    }
}

interface NotificationInteractor {

    fun showProgressNotification(notifId: Int, caption: String, completedUnits: Long, totalUnits: Long)
    fun dismissNotification(notificationId: Int)
}