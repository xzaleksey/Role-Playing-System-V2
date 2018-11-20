package com.alekseyvalyakin.roleplaysystem.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundService


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
                .setDefaults(Notification.DEFAULT_VIBRATE)
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


    private fun getMediaStyle(): android.support.v4.media.app.NotificationCompat.MediaStyle {
        return android.support.v4.media.app.NotificationCompat.MediaStyle()
    }

    override fun getSoundRecordNotification(caption: String, inProgress: Boolean): Notification {

        val builder = commonBuilder()
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(caption)
                .setStyle(getMediaStyle().setShowActionsInCompactView())
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
        val actionStop = NotificationCompat.Action.Builder(
                R.drawable.ic_stop,
                context.getString(R.string.stop), getPendingIntentStopRecord())
                .setShowsUserInterface(true)
                .build()

        builder.addAction(actionStop)
        if (inProgress) {
            val actionPause = NotificationCompat.Action.Builder(
                    R.drawable.ic_player_pause,
                    context.getString(R.string.pause),
                    getPendingIntentPauseRecord())
                    .setShowsUserInterface(true)
                    .build()

            builder.addAction(actionPause)
        } else {
            val actionResume = NotificationCompat.Action.Builder(
                    R.drawable.ic_play,
                    context.getString(R.string.continue_record), getPendingIntentResumeRecord())
                    .setShowsUserInterface(true)
                    .build()

            builder.addAction(actionResume)
        }
        return builder.build()
    }

    private fun getPendingIntentStopRecord(): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(SoundService.STOP_SOUND_RECORD), 0)
    }

    private fun getPendingIntentResumeRecord(): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(SoundService.RESUME_SOUND_RECORD), 0)
    }

    private fun getPendingIntentPauseRecord(): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(SoundService.PAUSE_SOUND_RECORD), 0)
    }

    override fun dismissNotification(notificationId: Int) {
        manager.cancel(notificationId)
    }

}

interface NotificationInteractor {
    fun showProgressNotification(notifId: Int, caption: String, completedUnits: Long, totalUnits: Long)
    fun dismissNotification(notificationId: Int)
    fun getSoundRecordNotification(caption: String, inProgress: Boolean): Notification

    companion object {
        const val SOUND_RECORD_NOTIF_ID = 1
    }

}