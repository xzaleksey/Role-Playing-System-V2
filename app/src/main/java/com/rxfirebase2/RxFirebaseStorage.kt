package com.rxfirebase2

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.io.File
import java.io.InputStream

object RxFirebaseStorage {

    /**
     * Asynchronously downloads the object from this [StorageReference] a byte array will be allocated large enough to hold the entire file in memory.
     *
     * @param storageRef           represents a reference to a Google Cloud Storage object.
     * @param maxDownloadSizeBytes the maximum allowed size in bytes that will be allocated. Set this parameter to prevent out of memory conditions from occurring.
     * If the download exceeds this limit, the task will fail and an IndexOutOfBoundsException will be returned.
     * @return a [Single] which emits an byte[] if success.
     */
    fun getBytes(storageRef: StorageReference,
                 maxDownloadSizeBytes: Long): Maybe<ByteArray> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, storageRef.getBytes(maxDownloadSizeBytes)) }
    }

    /**
     * Asynchronously retrieves a long lived download URL with a revocable token.
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @return a [Single] which emits an [Uri] if success.
     */
    fun getDownloadUrl(storageRef: StorageReference): Maybe<Uri> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, storageRef.downloadUrl) }
    }

    /**
     * Asynchronously downloads the object at this [StorageReference] to a specified system filepath.
     *
     * @param storageRef      represents a reference to a Google Cloud Storage object.
     * @param destinationFile a File representing the path the object should be downloaded to.
     * @return a [Single] which emits an [FileDownloadTask.TaskSnapshot] if success.
     */
    fun getFile(storageRef: StorageReference,
                destinationFile: File): Single<FileDownloadTask.TaskSnapshot> {
        return Single.create<FileDownloadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.getFile(destinationFile).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously downloads the object at this [StorageReference] to a specified system filepath.
     *
     * @param storageRef     represents a reference to a Google Cloud Storage object.
     * @param destinationUri a file system URI representing the path the object should be downloaded to.
     * @return a [Single] which emits an [FileDownloadTask.TaskSnapshot] if success.
     */
    fun getFile(storageRef: StorageReference,
                destinationUri: Uri): Single<FileDownloadTask.TaskSnapshot> {
        return Single.create<FileDownloadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.getFile(destinationUri).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Retrieves metadata associated with an object at this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @return a [Single] which emits an [StorageMetadata] if success.
     */
    fun getMetadata(storageRef: StorageReference): Maybe<StorageMetadata> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, storageRef.metadata) }
    }

    /**
     * Asynchronously downloads the object at this [StorageReference] via a InputStream.
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @return a [Single] which emits an [StreamDownloadTask.TaskSnapshot] if success.
     */
    fun getStream(storageRef: StorageReference): Single<StreamDownloadTask.TaskSnapshot> {
        return Single.create<StreamDownloadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.stream.addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously downloads the object at this [StorageReference] via a InputStream.
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param processor  A StreamDownloadTask.StreamProcessor that is responsible for reading data from the InputStream.
     * The StreamDownloadTask.StreamProcessor is called on a background thread and checked exceptions thrown
     * from this object will be returned as a failure to the OnFailureListener registered on the StreamDownloadTask.
     * @return a [Single] which emits an [StreamDownloadTask.TaskSnapshot] if success.
     */
    fun getStream(storageRef: StorageReference,
                  processor: StreamDownloadTask.StreamProcessor): Single<StreamDownloadTask.TaskSnapshot> {
        return Single.create<StreamDownloadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.getStream(processor).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads byte data to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param bytes      The byte[] to upload.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putBytes(storageRef: StorageReference,
                 bytes: ByteArray): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putBytes(bytes).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads byte data to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param bytes      The byte[] to upload.
     * @param metadata   [StorageMetadata] containing additional information (MIME type, etc.) about the object being uploaded.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putBytes(storageRef: StorageReference,
                 bytes: ByteArray,
                 metadata: StorageMetadata): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putBytes(bytes, metadata).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads from a content URI to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param uri        The source of the upload. This can be a file:// scheme or any content URI. A content resolver will be used to load the data.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putFile(storageRef: StorageReference,
                uri: Uri): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putFile(uri).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    fun putFileAndObserveUri(storageRef: StorageReference, uri: Uri, listener: OnProgressListener<in UploadTask.TaskSnapshot>? = null): Single<Uri> {
        return Single.create { emitter ->
            val taskSnapshotStorageTask = storageRef.putFile(uri)
            if (listener != null) {
                taskSnapshotStorageTask.addOnProgressListener(listener)
            }
            taskSnapshotStorageTask.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            val urlTask: Task<Uri> = taskSnapshotStorageTask.onSuccessTask {
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onSuccess(task.result!!)
                } else {
                    if (!emitter.isDisposed) {
                        emitter.onError(task.exception as Throwable)
                    }
                }
            }
            emitter.setCancellable {
                taskSnapshotStorageTask.cancel()
            }
        }
    }

    /**
     * Asynchronously uploads from a content URI to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param uri        The source of the upload. This can be a file:// scheme or any content URI. A content resolver will be used to load the data.
     * @param metadata   [StorageMetadata] containing additional information (MIME type, etc.) about the object being uploaded.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putFile(storageRef: StorageReference,
                uri: Uri,
                metadata: StorageMetadata): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putFile(uri, metadata)
                    .addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                        if (!emitter.isDisposed)
                            emitter.onError(e)
                    }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads from a content URI to this [StorageReference].
     *
     * @param storageRef        represents a reference to a Google Cloud Storage object.
     * @param uri               The source of the upload. This can be a file:// scheme or any content URI. A content resolver will be used to load the data.
     * @param metadata          [StorageMetadata] containing additional information (MIME type, etc.) about the object being uploaded.
     * @param existingUploadUri If set, an attempt is made to resume an existing upload session as defined by getUploadSessionUri().
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putFile(storageRef: StorageReference,
                uri: Uri,
                metadata: StorageMetadata,
                existingUploadUri: Uri): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putFile(uri, metadata, existingUploadUri)
                    .addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                        if (!emitter.isDisposed)
                            emitter.onError(e)
                    }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param stream     The InputStream to upload.
     * @param metadata   [StorageMetadata] containing additional information (MIME type, etc.) about the object being uploaded.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putStream(storageRef: StorageReference,
                  stream: InputStream,
                  metadata: StorageMetadata): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putStream(stream, metadata)
                    .addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                        if (!emitter.isDisposed)
                            emitter.onError(e)
                    }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads a stream of data to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param stream     The InputStream to upload.
     * @return a [Single] which emits an [UploadTask.TaskSnapshot] if success.
     */
    fun putStream(storageRef: StorageReference,
                  stream: InputStream): Single<UploadTask.TaskSnapshot> {
        return Single.create<UploadTask.TaskSnapshot> { emitter ->
            val taskSnapshotStorageTask = storageRef.putStream(stream).addOnSuccessListener { taskSnapshot -> emitter.onSuccess(taskSnapshot) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }

            emitter.setCancellable { taskSnapshotStorageTask.cancel() }
        }
    }

    /**
     * Asynchronously uploads a stream of data to this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @param metadata   [StorageMetadata] containing additional information (MIME type, etc.) about the object being uploaded.
     * @return a [Maybe] which emits an [StorageMetadata] if success.
     */
    fun updateMetadata(storageRef: StorageReference,
                       metadata: StorageMetadata): Maybe<StorageMetadata> {
        return Maybe.create { emitter -> RxHandler.assignOnTask(emitter, storageRef.updateMetadata(metadata)) }
    }

    /**
     * Deletes the object at this [StorageReference].
     *
     * @param storageRef represents a reference to a Google Cloud Storage object.
     * @return a [Completable] if the task is complete successfully.
     */
    fun delete(storageRef: StorageReference): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, storageRef.delete()) }
    }
}