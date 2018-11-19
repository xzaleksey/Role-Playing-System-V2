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

    }

    override fun seekTo(progress: Int) {
        player.seekTo(player.duration * progress / 100)
    }

    override fun isStateEnded(): Boolean {
        return player.playbackState == Player.STATE_ENDED
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

    override fun pause() {
        player.playWhenReady = false
    }

    override fun stop() {
        player.stop(true)
    }

    override fun resume() {
        player.playWhenReady = true
    }

    override fun getProgress(): Int {
        return ((player.currentPosition * 100) / player.duration).toInt()
    }

}

interface ExoPlayerInteractor {
    fun playFile(file: File)
    fun pause()
    fun resume()
    fun getProgress(): Int
    fun stop()
    fun isStateEnded(): Boolean
    fun seekTo(progress: Int)
}