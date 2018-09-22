package com.rxfirebase2


import android.annotation.SuppressLint
import android.app.Activity
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.rxfirebase2.DocumentSnapshotMapper.*
import io.reactivex.*
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Executor

object RxFirestore {

    /**
     * Executes the given updateFunction and then attempts to commit the changes applied within the transaction.
     * If any document read within the transaction has changed, the updateFunction will be retried.
     * If it fails to commit after 5 attempts, the transaction will fail.
     *
     * @param firestore FirebaseFirestore instance.
     * @param function  The function to execute within the transaction context.
     */
    fun runTransaction(firestore: FirebaseFirestore,
                       function: Transaction.Function<Any>): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, firestore.runTransaction(function)) }
    }

    /**
     * Execute all of the writes in this write batch as a single atomic unit.
     *
     * @param batch A write batch, used to perform multiple writes as a single atomic unit.
     */
    fun atomicOperation(batch: WriteBatch): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, batch.commit()) }
    }

    /**
     * Execute all of the writes in this write batch as a single atomic unit.
     *
     * @param batches A list of write batched, used to perform multiple writes as a single atomic unit.
     */
    fun atomicOperation(batches: List<WriteBatch>): Completable {
        if (batches.isEmpty()) throw IllegalArgumentException("Batches list can't be empty")

        val batchTasks = ArrayList<Completable>()
        for (batch in batches) {
            batchTasks.add(Completable.create { emitter ->
                batch.commit()
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener { e ->
                            if (!emitter.isDisposed)
                                emitter.onError(e)
                        }
            }.subscribeOn(Schedulers.io()))
        }

        return Completable.merge(batchTasks)
    }

    /**
     * Adds a new document to this collection with the specified data, assigning it a document INFO automatically.
     *
     * @param ref  The given Collection reference.
     * @param data A Map containing the data for the new document..
     * @return a Single which emits the [DocumentReference] of the added Document.
     */
    fun addDocument(ref: CollectionReference,
                    data: Map<String, Any>): Single<DocumentReference> {
        return Single.create { emitter ->
            ref.add(data).addOnCompleteListener { task -> emitter.onSuccess(task.result) }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }

    @SuppressLint("RxLeakedSubscription")
    fun <T> addDocument(ref: CollectionReference,
                        pojo: T): Single<DocumentSnapshot> {
        return Single.create { emitter ->
            ref.add(pojo as Any).addOnSuccessListener { documentReference ->
                val documentSingle = getDocumentSingle(documentReference)
                documentSingle.subscribe({ documentSnapshot -> emitter.onSuccess(documentSnapshot) }, { throwable ->
                    if (!emitter.isDisposed)
                        emitter.onError(throwable)
                })
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }

    fun <T : HasId> addDocumentHasId(ref: CollectionReference,
                                     pojo: T): Single<T> {
        return Single.create { emitter ->
            ref.add(pojo as Any).addOnSuccessListener { reference ->
                reference.get().addOnSuccessListener {
                    try {
                        emitter.onSuccess(DocumentSnapshotMapper.ofHasId(pojo.javaClass).apply(it))
                    } catch (e: Exception) {
                        if (!emitter.isDisposed) {
                            emitter.onError(e)
                        }
                    }
                }.addOnFailureListener {
                    if (!emitter.isDisposed) {
                        emitter.onError(it)
                    }
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
    }


    /**
     * Adds a new document to this collection with the specified data, assigning it a document INFO automatically.
     *
     *
     * This method will just call the API `add` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref  The given Collection reference.
     * @param data A Map containing the data for the new document..
     * @return a Single which emits the [DocumentReference] of the added Document.
     */
    private fun addDocumentOffline(ref: CollectionReference,
                                   data: Map<String, Any>): Completable {
        return Completable.create { emitter ->
            try {
                ref.add(data)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /**
     * Adds a new document to this collection with the specified POJO as contents, assigning it a document INFO automatically.
     *
     *
     * This method will just call the API `add` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref  The given Collection reference.
     * @param pojo The POJO that will be used to populate the contents of the document.
     * @return a Single which emits the [DocumentReference] of the added Document.
     */
    private fun addDocumentOffline(ref: CollectionReference,
                                   pojo: Any): Completable {
        return Completable.create { emitter ->
            try {
                ref.add(pojo)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }


    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     * @param ref             The given Document reference.
     * @param updateFieldsMap A map of field / value pairs to update. Fields can contain dots to reference nested fields within the document.
     */
    fun updateDocument(ref: DocumentReference,
                       updateFieldsMap: Map<String, Any>): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.update(updateFieldsMap)) }
    }

    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     *
     * This method will just call the API `update` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref             The given Document reference.
     * @param updateFieldsMap A map of field / value pairs to update. Fields can contain dots to reference nested fields within the document.
     */
    fun updateDocumentOffline(ref: DocumentReference,
                              updateFieldsMap: Map<String, Any>): Completable {
        ref.update(updateFieldsMap)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     * @param ref                 The given Document reference.
     * @param field               The first field to update. Fields can contain dots to reference a nested field within the document.
     * @param value               The first value
     * @param moreFieldsAndValues Additional field/value pairs.
     */
    fun updateDocument(ref: DocumentReference,
                       field: String,
                       value: Any,
                       vararg moreFieldsAndValues: Any): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.update(field, value, *moreFieldsAndValues)) }
    }

    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     *
     * This method will just call the API `update` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref                 The given Document reference.
     * @param field               The first field to update. Fields can contain dots to reference a nested field within the document.
     * @param value               The first value
     * @param moreFieldsAndValues Additional field/value pairs.
     */
    fun updateDocumentOffline(ref: DocumentReference,
                              field: String,
                              value: Any,
                              vararg moreFieldsAndValues: Any): Completable {
        ref.update(field, value, *moreFieldsAndValues)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }


    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     * @param ref                 The given Document reference.
     * @param fieldPath           The first field to update. Fields can contain dots to reference a nested field within the document.
     * @param value               The first value
     * @param moreFieldsAndValues Additional field/value pairs.
     */
    fun updateDocument(ref: DocumentReference,
                       fieldPath: FieldPath,
                       value: Any,
                       vararg moreFieldsAndValues: Any): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.update(fieldPath, value, *moreFieldsAndValues)) }
    }

    /**
     * Updates fields in the document referred to by this DocumentReference. If no document exists yet, the update will fail.
     *
     *
     * This method will just call the API `update` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref                 The given Document reference.
     * @param fieldPath           The first field to update. Fields can contain dots to reference a nested field within the document.
     * @param value               The first value
     * @param moreFieldsAndValues Additional field/value pairs.
     */
    fun updateDocumentOffline(ref: DocumentReference,
                              fieldPath: FieldPath,
                              value: Any,
                              vararg moreFieldsAndValues: Any): Completable {
        ref.update(fieldPath, value, *moreFieldsAndValues)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
     *
     * @param ref          The given Document reference.
     * @param setFieldsMap A map of the fields and values for the document.
     * @param options      An object to configure the set behavior.
     */
    @JvmOverloads
    fun setDocument(ref: DocumentReference,
                    setFieldsMap: Map<String, Any>,
                    options: SetOptions = SetOptions.merge()): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.set(setFieldsMap, options)) }
    }

    /**
     * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
     *
     * @param ref     The given Document reference.
     * @param pojo    The POJO that will be used to populate the document contents.
     * @param options An object to configure the set behavior.
     */
    @JvmOverloads
    fun setDocument(ref: DocumentReference,
                    pojo: Any,
                    options: SetOptions = SetOptions.merge()): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.set(pojo, options)) }
    }

    /**
     * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
     *
     *
     * This method will just call the API `set` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref     The given Document reference.
     * @param pojo    The POJO that will be used to populate the document contents.
     * @param options An object to configure the set behavior.
     */
    fun setDocumentOffline(ref: DocumentReference,
                           pojo: Any,
                           options: SetOptions): Completable {
        ref.set(pojo, options)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
     *
     *
     * This method will just call the API `set` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref          The given Document reference.
     * @param setFieldsMap A map of the fields and values for the document.
     */
    fun setDocumentOffline(ref: DocumentReference,
                           setFieldsMap: Map<String, Any>): Completable {
        ref.set(setFieldsMap)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
     *
     *
     * This method will just call the API `set` method without wait for any complete listener. This is made in this way because the
     * listeners required connection for this kind of operations.
     *
     * @param ref  The given Document reference.
     * @param pojo The POJO that will be used to populate the document contents.
     */
    fun setDocumentOffline(ref: DocumentReference,
                           pojo: Any): Completable {
        ref.set(pojo)
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Deletes the document referred to by this DocumentReference.
     *
     * @param ref The given Document reference.
     */
    fun deleteDocument(ref: DocumentReference): Completable {
        return Completable.create { emitter -> RxCompletableHandler.assignOnTask(emitter, ref.delete()) }
    }

    /**
     * Deletes the document referred to by this DocumentReference.
     *
     * @param ref The given Document reference.
     */
    fun deleteDocumentOffline(ref: DocumentReference): Completable {
        ref.delete()
        return RxFirestoreOfflineHandler.listenOfflineListener(ref)
    }

    /**
     * Reads the document referenced by this DocumentReference.
     *
     * @param ref The given Document reference.
     */
    fun getDocument(ref: DocumentReference): Maybe<DocumentSnapshot> {
        return Maybe.create { emitter ->
            ref.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    emitter.onSuccess(documentSnapshot)
                } else {
                    emitter.onComplete()
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }

    fun getDocumentSingle(ref: DocumentReference): Single<DocumentSnapshot> {
        return Single.create { emitter ->
            ref.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    emitter.onSuccess(documentSnapshot)
                } else {
                    emitter.onError(DocumentNotExistsException())
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }

    fun <T> getDocumentSingle(ref: DocumentReference, clazz: Class<T>): Single<T> {
        return getDocumentSingle(ref).map(DocumentSnapshotMapper.of(clazz))
    }

    fun <T : HasId> getDocumentSingleHasId(ref: DocumentReference, clazz: Class<T>): Single<T> {
        return getDocumentSingle(ref).map(DocumentSnapshotMapper.ofHasId(clazz))
    }

    /**
     * Reads the collection referenced by this DocumentReference
     *
     * @param ref The given Collection reference.
     */
    fun getCollection(ref: CollectionReference): Maybe<QuerySnapshot> {
        return Maybe.create { emitter ->
            ref.get().addOnSuccessListener { documentSnapshots ->
                if (documentSnapshots.isEmpty) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(documentSnapshots)
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }


    /**
     * Reads the collection referenced by this DocumentReference
     *
     * @param query The given Collection query.
     */
    fun getCollection(query: Query): Maybe<QuerySnapshot> {
        return Maybe.create { emitter ->
            query.get().addOnSuccessListener { documentSnapshots ->
                if (documentSnapshots.isEmpty) {
                    emitter.onComplete()
                } else {
                    emitter.onSuccess(documentSnapshots)
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed)
                    emitter.onError(e)
            }
        }
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref             The given Document reference.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeDocumentRef(ref: DocumentReference,
                           metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                           strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<DocumentSnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(metadataChanges, EventListener { documentSnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(documentSnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref             The given Document reference.
     * @param executor        The executor to use to call the listener.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeDocumentRef(ref: DocumentReference,
                           executor: Executor,
                           metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                           strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<DocumentSnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(executor, metadataChanges, EventListener { documentSnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(documentSnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref             The given Document reference.
     * @param activity        The activity to scope the listener to.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeDocumentRef(ref: DocumentReference,
                           activity: Activity,
                           metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                           strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<DocumentSnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(activity, metadataChanges, EventListener { documentSnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(documentSnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref             The given Query reference.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeQueryRef(ref: Query,
                        metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                        strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<QuerySnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(metadataChanges, EventListener { querySnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(querySnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref             The given Query reference.
     * @param executor        The executor to use to call the listener.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeQueryRef(ref: Query,
                        executor: Executor,
                        metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                        strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<QuerySnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(executor, metadataChanges, EventListener { documentSnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(documentSnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this Query using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref             The given Query reference.
     * @param activity        The activity to scope the listener to.
     * @param metadataChanges Listen for metadata changes
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     */
    @JvmOverloads
    fun observeQueryRef(ref: Query,
                        activity: Activity,
                        metadataChanges: MetadataChanges = MetadataChanges.EXCLUDE,
                        strategy: BackpressureStrategy = BackpressureStrategy.DROP): Flowable<QuerySnapshot> {
        return Flowable.create({ emitter ->
            val registration = ref.addSnapshotListener(activity, metadataChanges, EventListener { documentSnapshot, e ->
                if (e != null && !emitter.isCancelled) {
                    emitter.onError(e)
                    return@EventListener
                }
                emitter.onNext(documentSnapshot!!)
            })
            emitter.setCancellable { registration.remove() }
        }, strategy)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference.
     *
     * @param ref      The given Document reference.
     * @param executor The executor to use to call the listener.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     */
    fun observeDocumentRef(ref: DocumentReference,
                           executor: Executor,
                           strategy: BackpressureStrategy): Flowable<DocumentSnapshot> {
        return observeDocumentRef(ref, executor, MetadataChanges.EXCLUDE, strategy)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref      The given Document reference.
     * @param activity The activity to scope the listener to.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     */
    fun observeDocumentRef(ref: DocumentReference,
                           activity: Activity,
                           strategy: BackpressureStrategy): Flowable<DocumentSnapshot> {
        return observeDocumentRef(ref, activity, MetadataChanges.EXCLUDE, strategy)
    }

    fun <T> observeDocumentRef(ref: DocumentReference,
                               clazz: Class<T>): Flowable<T> {
        return observeDocumentRef(ref, DocumentSnapshotMapper.of(clazz))
    }

    fun <T : HasId> observeDocumentRefHasId(ref: DocumentReference,
                                            clazz: Class<T>): Flowable<T> {
        return observeDocumentRef(ref, DocumentSnapshotMapper.ofHasId(clazz))
    }

    /**
     * Starts listening to the document referenced by this DocumentReference.
     *
     * @param ref    The given Document reference.
     * @param mapper specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref)
                .filter(DOCUMENT_EXISTENCE_PREDICATE)
                .map(mapper)
    }

    fun observeDocumentDeleteRef(ref: DocumentReference): Completable {
        return observeDocumentRef(ref)
                .filter(DOCUMENT_NOT_EXISTENCE_PREDICATE)
                .firstOrError()
                .ignoreElement()
    }

    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               clazz: Class<T>): Flowable<T> {
        return observeDocumentRef(ref, strategy, DocumentSnapshotMapper.of(clazz))
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref      The given Document reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref      The given Document reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param executor The executor to use to call the listener.
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               executor: Executor,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref, executor, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref      The given Document reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param activity The activity to scope the listener to.
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               activity: Activity,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref, activity, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference with the given options.
     *
     * @param ref             The given Document reference.
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     * @param executor        The executor to use to call the listener.
     * @param metadataChanges Listen for metadata changes
     * @param mapper          specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               executor: Executor,
                               metadataChanges: MetadataChanges,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref, executor, metadataChanges, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this DocumentReference using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref             The given Document reference.
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     * @param activity        The activity to scope the listener to.
     * @param metadataChanges Listen for metadata changes
     * @param mapper          specific function to map the dispatched events.
     */
    fun <T> observeDocumentRef(ref: DocumentReference,
                               strategy: BackpressureStrategy,
                               activity: Activity,
                               metadataChanges: MetadataChanges,
                               mapper: Function<in DocumentSnapshot, out T>): Flowable<T> {
        return observeDocumentRef(ref, activity, metadataChanges, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this Query.
     *
     * @param ref      The given Query reference.
     * @param executor The executor to use to call the listener.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     */
    fun observeQueryRef(ref: Query,
                        executor: Executor,
                        strategy: BackpressureStrategy): Flowable<QuerySnapshot> {
        return observeQueryRef(ref, executor, MetadataChanges.EXCLUDE, strategy)
    }

    /**
     * Starts listening to the document referenced by this Query using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref      The given Query reference.
     * @param activity The activity to scope the listener to.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     */
    fun observeQueryRef(ref: Query,
                        activity: Activity,
                        strategy: BackpressureStrategy): Flowable<QuerySnapshot> {
        return observeQueryRef(ref, activity, MetadataChanges.EXCLUDE, strategy)
    }

    fun <T> observeQueryRef(ref: Query,
                            clazz: Class<T>): Flowable<List<T>> {
        return observeQueryRef(ref, DocumentSnapshotMapper.listOf(clazz))
    }

    fun <T : HasId> observeQueryRefHasId(ref: Query,
                                         clazz: Class<T>): Flowable<List<T>> {
        return observeQueryRef(ref, DocumentSnapshotMapper.listOf(clazz) { documentSnapshot ->
            val `object` = documentSnapshot.toObject(clazz)
            if (`object` != null) {
                `object`.id = documentSnapshot.id
            }
            `object`
        })
    }

    /**
     * Starts listening to the document referenced by this Query.
     *
     * @param ref    The given Query reference.
     * @param mapper specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            mapper: Function<in QuerySnapshot, out List<T>>): Flowable<List<T>> {
        return observeQueryRef(ref)
                .map(mapper)
    }

    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            clazz: Class<T>): Flowable<List<T>> {
        return observeQueryRef(ref, strategy, DocumentSnapshotMapper.listOf(clazz))
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref      The given Query reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            mapper: Function<in QuerySnapshot, out List<T>>): Flowable<List<T>> {
        return observeQueryRef(ref, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref      The given Query reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param executor The executor to use to call the listener.
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            executor: Executor,
                            mapper: Function<in QuerySnapshot, out T>): Flowable<T> {
        return observeQueryRef(ref, executor, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref      The given Query reference.
     * @param strategy [BackpressureStrategy] associated to this [Flowable]
     * @param activity The activity to scope the listener to.
     * @param mapper   specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            activity: Activity,
                            mapper: Function<in QuerySnapshot, out T>): Flowable<T> {
        return observeQueryRef(ref, activity, MetadataChanges.EXCLUDE, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this Query with the given options.
     *
     * @param ref             The given Query reference.
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     * @param executor        The executor to use to call the listener.
     * @param metadataChanges Listen for metadata changes
     * @param mapper          specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            executor: Executor,
                            metadataChanges: MetadataChanges,
                            mapper: Function<in QuerySnapshot, out T>): Flowable<T> {
        return observeQueryRef(ref, executor, metadataChanges, strategy)
                .map(mapper)
    }

    /**
     * Starts listening to the document referenced by this Query using an Activity-scoped listener.
     * The listener will be automatically removed during onStop().
     *
     * @param ref             The given Query reference.
     * @param strategy        [BackpressureStrategy] associated to this [Flowable]
     * @param activity        The activity to scope the listener to.
     * @param metadataChanges Listen for metadata changes
     * @param mapper          specific function to map the dispatched events.
     */
    fun <T> observeQueryRef(ref: Query,
                            strategy: BackpressureStrategy,
                            activity: Activity,
                            metadataChanges: MetadataChanges,
                            mapper: Function<in QuerySnapshot, out T>): Flowable<T> {
        return observeQueryRef(ref, activity, metadataChanges, strategy)
                .map(mapper)
    }

    /**
     * Reads the collection referenced by this CollectionReference.
     *
     * @param ref   The given Collection reference.
     * @param clazz class type for the [DocumentSnapshot] items.
     */
    fun <T> getCollection(ref: CollectionReference,
                          clazz: Class<T>): Maybe<List<T>> {
        return getCollection(ref, DocumentSnapshotMapper.listOf(clazz))
    }

    /**
     * SReads the collection referenced by this CollectionReference.
     *
     * @param ref    The given Collection reference.
     * @param mapper specific function to map the dispatched events.
     */
    private fun <T> getCollection(ref: CollectionReference,
                                  mapper: DocumentSnapshotMapper<QuerySnapshot, List<T>>): Maybe<List<T>> {
        return getCollection(ref)
                .filter(QUERY_EXISTENCE_PREDICATE)
                .map(mapper)
    }

    /**
     * Reads the collection referenced by this Query.
     *
     * @param query The given Collection query.
     * @param clazz class type for the [DocumentSnapshot] items.
     */
    fun <T> getCollection(query: Query,
                          clazz: Class<T>): Maybe<List<T>> {
        return getCollection(query, DocumentSnapshotMapper.listOf(clazz))
    }

    /**
     * Reads the collection referenced by this Query.
     *
     * @param query  The given Collection query.
     * @param mapper specific function to map the dispatched events.
     */
    private fun <T> getCollection(query: Query,
                                  mapper: DocumentSnapshotMapper<QuerySnapshot, List<T>>): Maybe<List<T>> {
        return getCollection(query)
                .filter(QUERY_EXISTENCE_PREDICATE)
                .map(mapper)
    }

    /**
     * Reads the document referenced by this DocumentReference.
     *
     * @param ref   The given Document reference.
     * @param clazz class type for the [DocumentSnapshot] items.
     */
    fun <T> getDocument(ref: DocumentReference,
                        clazz: Class<T>): Maybe<T> {
        return getDocument(ref, DocumentSnapshotMapper.of(clazz))
    }

    /**
     * Reads the document referenced by this DocumentReference.
     *
     * @param ref    The given Document reference.
     * @param mapper specific function to map the dispatched events.
     */
    fun <T> getDocument(ref: DocumentReference,
                        mapper: Function<in DocumentSnapshot, out T>): Maybe<T> {
        return getDocument(ref)
                .filter(DOCUMENT_EXISTENCE_PREDICATE)
                .map(mapper)
    }
}
/**
 * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
 *
 * @param ref          The given Document reference.
 * @param setFieldsMap A map of the fields and values for the document.
 */
/**
 * Overwrites the document referred to by this DocumentReference. If the document does not yet exist, it will be created. If a document already exists, it will be overwritten.
 *
 * @param ref  The given Document reference.
 * @param pojo The POJO that will be used to populate the document contents.
 */
/**
 * Starts listening to the document referenced by this DocumentReference.
 *
 * @param ref The given Document reference.
 */
/**
 * Starts listening to the document referenced by this DocumentReference.
 *
 * @param ref      The given Document reference.
 * @param executor The executor to use to call the listener.
 */
/**
 * Starts listening to the document referenced by this DocumentReference using an Activity-scoped listener.
 * The listener will be automatically removed during onStop().
 *
 * @param ref      The given Document reference.
 * @param activity The activity to scope the listener to.
 */
/**
 * Starts listening to the document referenced by this DocumentReference with the given options.
 *
 * @param ref             The given Document reference.
 * @param executor        The executor to use to call the listener.
 * @param metadataChanges Listen for metadata changes
 */
/**
 * Starts listening to the document referenced by this DocumentReference using an Activity-scoped listener.
 * The listener will be automatically removed during onStop().
 *
 * @param ref             The given Document reference.
 * @param activity        The activity to scope the listener to.
 * @param metadataChanges Listen for metadata changes
 */
/**
 * Starts listening to the document referenced by this Query.
 *
 * @param ref The given Query reference.
 */
/**
 * Starts listening to the document referenced by this Query.
 *
 * @param ref      The given Query reference.
 * @param executor The executor to use to call the listener.
 */
/**
 * Starts listening to the document referenced by this Query using an Activity-scoped listener.
 * The listener will be automatically removed during onStop().
 *
 * @param ref      The given Query reference.
 * @param activity The activity to scope the listener to.
 */
/**
 * Starts listening to the document referenced by this Query with the given options.
 *
 * @param ref             The given Query reference.
 * @param executor        The executor to use to call the listener.
 * @param metadataChanges Listen for metadata changes
 */
/**
 * Starts listening to the document referenced by this Query using an Activity-scoped listener.
 * The listener will be automatically removed during onStop().
 *
 * @param ref             The given Query reference.
 * @param activity        The activity to scope the listener to.
 * @param metadataChanges Listen for metadata changes
 */
