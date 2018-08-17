package com.alekseyvalyakin.roleplaysystem.data.useravatar

import android.content.Context
import android.net.Uri
import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.base.image.ObservableUrlDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.rxfirebase2.RxFirebaseStorage
import id.zelory.compressor.Compressor
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAvatarRepositoryImpl @Inject constructor(
        private val fileInfoProvider: FileInfoProvider,
        private val context: Context,
        @ThreadConfig(ThreadConfig.TYPE.IO) private val ioScheduler: Scheduler,
        private val userRepository: UserRepository,
        private val resourcesProvider: ResourcesProvider
) : UserAvatarRepository {

    private val avatars = "avatars"
    private val subject = BehaviorSubject.create<String>().toSerialized()

    private val updateAvatarObservable = userRepository.observeCurrentUser()
            .doOnNext { user ->
                user?.photoUrl?.let {
                    subject.onNext(it)
                }
            }.share()

    override fun uploadAvatar(filePath: String): Single<String> {
        return Single.create<String> { emitter ->
            userRepository.getCurrentUserId().zipWith(
                    createLocalFileCopy(File(filePath)),
                    BiFunction { id: String, file: File -> return@BiFunction id to file })
                    .flatMap { pair ->
                        val reference = FirebaseStorage.getInstance().reference.child(avatars).child(pair.first)

                        return@flatMap RxFirebaseStorage
                                .putFileAndObserveUri(reference, Uri.fromFile(pair.second))
                                .flatMap { url ->
                                    val urlString = url.toString()
                                    userRepository.updatePhotoUrl(urlString)
                                            .toSingleDefault(urlString)
                                }
                    }.subscribeOn(ioScheduler)
                    .subscribe({
                        subject.onNext(it)
                        emitter.onSuccess(it)
                    }, {
                        if (!emitter.isDisposed) {
                            emitter.onError(it)
                        }
                    })
        }
    }

    override fun getAvatarImageProvider(): ImageProvider {
        return ObservableUrlDrawableProviderImpl(subject.toFlowable(BackpressureStrategy.LATEST)
                .toObservable()
                .distinctUntilChanged(),
                userRepository.getCurrentUserInfo()!!.uid,
                resourcesProvider,
                RequestOptions.circleCropTransform()
        )
    }

    override fun subscribeForUpdates(): Disposable {
        return updateAvatarObservable.subscribeWithErrorLogging()
    }

    private fun createLocalFileCopy(file: File): Single<File> {
        return Single.fromCallable {
            val newDirectory = fileInfoProvider.getFilesPath().absolutePath
            val compressor = Compressor.Builder(context)
                    .setDestinationDirectoryPath(newDirectory)
                    .setQuality(100)
                    .build()
            compressor.compressToFile(file)
        }
    }
}

interface UserAvatarRepository {
    fun uploadAvatar(filePath: String): Single<String>
    fun getAvatarImageProvider(): ImageProvider
    fun subscribeForUpdates(): Disposable
}