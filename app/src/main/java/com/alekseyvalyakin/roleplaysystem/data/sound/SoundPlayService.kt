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

class SoundPlayService : Service() {

    @Inject
    lateinit var audioFileInteractor: AudioFileInteractor
    @Inject
    lateinit var notificationInteractor: NotificationInteractor
    private val compositeDisposable = CompositeDisposable()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                when (intent.action) {
                    STOP -> audioFileInteractor.stop()
                    RESUME -> audioFileInteractor.resume()
                    PAUSE -> audioFileInteractor.pause()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("created service")
        RpsApp.app.getAppComponent().inject(this)

        val intentFilter = IntentFilter()
        intentFilter.addAction(STOP)
        intentFilter.addAction(RESUME)
        intentFilter.addAction(PAUSE)
        registerReceiver(receiver, intentFilter)

        compositeDisposable.add(audioFileInteractor.observe()
                .distinctUntilChanged(Function<AudioState, Boolean> {
                    return@Function it.isPlaying || it.isEmpty()
                })
                .subscribeWithErrorLogging {
                    Timber.d("got update")
                    if (it.isEmpty()) {
                        stopSelf()
                    } else {
                        val inProgress = it.isPlaying
                        val caption = if (inProgress) {
                            getString(R.string.playing_sound)
                        } else {
                            getString(R.string.sound_playing_paused)
                        }
                        startForeground(NotificationInteractor.SOUND_PLAY_NOTIF_ID,
                                notificationInteractor.getSoundPlayNotification(caption, inProgress, it.file.nameWithoutExtension))
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
        const val STOP = "com.alekseyvalyakin.roleplaysystem.stop_playing_file"
        const val PAUSE = "com.alekseyvalyakin.roleplaysystem.pause_playing_file"
        const val RESUME = "com.alekseyvalyakin.roleplaysystem.resume_playing_file"

        fun startService(context: Context) {
            context.startService(Intent(context, SoundPlayService::class.java))
        }
    }

}
