package com.alekseyvalyakin.roleplaysystem.data.sound

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import timber.log.Timber
import java.io.File

class ExoPlayerInteractorImpl(private val context: Context) : ExoPlayerInteractor {
    private val player: SimpleExoPlayer

    init {
        val rendersFactory = DefaultRenderersFactory(
                context,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        )

        val trackSelector = DefaultTrackSelector()
        player = ExoPlayerFactory.newSimpleInstance(context, rendersFactory, trackSelector)
        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                Timber.e(error)
            }
        })
//
//        Flowable.interval(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWithErrorLogging {
//                    if (player.duration != 0L) {
//                        val currentProgress = (player.currentPosition * 100) / player.duration
//                        if (!player.playWhenReady) {
//                            player.playWhenReady = true
//                        } else if (currentProgress > 50L) {
//                            player.playWhenReady = false
//                        }
//                        Timber.d("player progress $currentProgress")
//                    }
//                }
    }

    override fun playFile(file: File) {
        val userAgent = "RPG assistant"
        val mediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(
                context, userAgent)
        )
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(Uri.fromFile(file))
        player.prepare(mediaSource)
        player.playWhenReady = true
    }
}

interface ExoPlayerInteractor {
    fun playFile(file: File)
}