package com.alekseyvalyakin.roleplaysystem.data.firestorage

import android.net.Uri
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreIdPhoto
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rxfirebase2.RxFirebaseStorage
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseStorageRepositoryImpl : FirebaseStorageRepository {

    override fun uploadPhotoInGameAndGetUri(gameId: String, photoId: String, uri: Uri, listener: OnProgressListener<in UploadTask.TaskSnapshot>?): Single<Uri> {
        val reference = getPhotoInGameReference(gameId, photoId)
        return RxFirebaseStorage.putFileAndObserveUri(reference, uri, listener)

    }

    override fun deletePhotoInGame(gameId: String, photoId: String): Completable {
        return RxFirebaseStorage.delete(getPhotoInGameReference(gameId, photoId))
    }

    private fun getPhotoInGameReference(gameId: String, photoId: String): StorageReference {
        return FirebaseStorage.getInstance()
                .reference
                .child(Game.STORAGE_KEY)
                .child(gameId)
                .child(FireStoreIdPhoto.STORAGE_KEY)
                .child(photoId)
    }
}

interface FirebaseStorageRepository {
    fun uploadPhotoInGameAndGetUri(gameId: String, photoId: String, uri: Uri, listener: OnProgressListener<in UploadTask.TaskSnapshot>? = null): Single<Uri>

    fun deletePhotoInGame(gameId: String, photoId: String): Completable
}