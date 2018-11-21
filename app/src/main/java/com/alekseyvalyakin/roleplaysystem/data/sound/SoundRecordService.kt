package com.alekseyvalyakin.roleplaysystem.data.sound

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import com.alekseyvalyakin.roleplaysystem.utils.NotificationInteractor
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import timber.log.Timber
import javax.inject.Inject

class SoundRecordService : Service() {

    @Inject
    lateinit var soundInteractor: SoundRecordInteractor
    @Inject
    lateinit var notificationInteractor: NotificationInteractor
    private val compositeDisposable = CompositeDisposable()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                when (intent.action) {
                    STOP_SOUND_RECORD -> soundInteractor.stopRecordingFile()
                    RESUME_SOUND_RECORD -> soundInteractor.resumeRecordingFile()
                    PAUSE_SOUND_RECORD -> soundInteractor.pauseRecordingFile()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("created service")
        RpsApp.app.getAppComponent().inject(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(STOP_SOUND_RECORD)
        intentFilter.addAction(RESUME_SOUND_RECORD)
        intentFilter.addAction(PAUSE_SOUND_RECORD)
        registerReceiver(receiver, intentFilter)

        compositeDisposable.add(soundInteractor.observeRecordingState()
                .distinctUntilChanged(Function<RecordInfo, Boolean> {
                    return@Function it.inProgress || it.isRecordingFinished()
                })
                .subscribeWithErrorLogging {
                    Timber.d("got update")
                    if (it.isRecordingFinished()) {
                        stopSelf()
                    } else {
                        val inProgress = it.inProgress
                        startForeground(NotificationInteractor.SOUND_RECORD_NOTIF_ID,
                                notificationInteractor.getSoundRecordNotification(if (inProgress) {
                                    getString(R.string.recording_sound)
                                } else {
                                    getString(R.string.record_sound_paused)
                                }, inProgress))
                        Timber.d("update notification")
                    }
                })


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        Timber.d("On destroy service")
        unregisterReceiver(receiver)
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val STOP_SOUND_RECORD = "com.alekseyvalyakin.roleplaysystem.stop_sound_record"
        const val PAUSE_SOUND_RECORD = "com.alekseyvalyakin.roleplaysystem.pause_sound_record"
        const val RESUME_SOUND_RECORD = "com.alekseyvalyakin.roleplaysystem.resume_sound_record"

        fun startService(context: Context) {
            context.startService(Intent(context, SoundRecordService::class.java))
        }
    }

}
