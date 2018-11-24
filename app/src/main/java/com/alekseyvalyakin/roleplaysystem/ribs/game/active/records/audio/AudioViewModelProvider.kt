package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import android.os.FileObserver
import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.data.sound.AudioFileInteractor
import com.alekseyvalyakin.roleplaysystem.data.sound.FormatWAV
import com.alekseyvalyakin.roleplaysystem.data.sound.RawSamples
import com.alekseyvalyakin.roleplaysystem.flexible.divider.ShadowDividerViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter.AudioItemViewModel
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.jakewharton.rxrelay2.BehaviorRelay
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.io.File

class AudioViewModelProviderImpl(
        private val filterModelFlowable: Flowable<FilterModel>,
        private val fileInfoProvider: FileInfoProvider,
        private val audioFileInteractor: AudioFileInteractor,
        private val game: Game
) : AudioViewModelProvider {

    private val filesRelay = BehaviorRelay.createDefault(getFiles())

    private val fileObserver: FileObserver = object : FileObserver(fileInfoProvider.getRecordsDir(game.id).absolutePath) {
        override fun onEvent(event: Int, path: String?) {
            if (event in setOf(FileObserver.CREATE, FileObserver.DELETE, FileObserver.MODIFY, FileObserver.MOVED_TO, FileObserver.MOVED_FROM)) {
                filesRelay.accept(getFiles())
            }
        }
    }

    override fun observeViewModel(): Flowable<AudioViewModel> {
        return Flowables.combineLatest(filterModelFlowable,
                filesRelay.toFlowable(BackpressureStrategy.LATEST),
                audioFileInteractor.observe())
                .map { triple ->
                    val items = mutableListOf<IFlexible<*>>()
                    val filter = triple.first
                    val files = triple.second
                    val audioState = triple.third

                    files.forEach {
                        val nameWithoutExtension = it.nameWithoutExtension
                        if (nameWithoutExtension.startsWith(filter.query)) {
                            val selected = audioState.file == it
                            items.add(AudioItemViewModel(it,
                                    nameWithoutExtension,
                                    DateTime(RawSamples(it).duration)
                                            .withZone(DateTimeZone.UTC)
                                            .toString("HH:mm:ss"),
                                    selected,
                                    selected && audioState.isPlaying
                            ))
                        }
                    }
                    if (items.isNotEmpty()) {
                        items.add(ShadowDividerViewModel(items.size))
                    }
                    val viewModel = AudioViewModel(items, audioState)

                    return@map viewModel
                }.doOnSubscribe {
                    fileObserver.startWatching()
                }.doOnTerminate {
                    fileObserver.stopWatching()
                }
    }

    fun getFiles(): Array<out File> {
        return fileInfoProvider.getRecordsDir(game.id).listFiles { file ->
            file.absolutePath.endsWith(FormatWAV.FORMAT_NAME)
        }?.apply {
            sortByDescending { it.lastModified() }
        } ?: return emptyArray()
    }

}

interface AudioViewModelProvider {
    fun observeViewModel(): Flowable<AudioViewModel>
}