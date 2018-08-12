package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.utils.setId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.ConcurrentHashMap

class UserRepositoryImpl : UserRepository {

    private val userCache: MutableMap<String, User> = ConcurrentHashMap()

    override fun getCurrentUserSingle(): Single<User> {
        return getCurrentUserId().flatMap { userId ->
            val documentReference = userDocument(userId)
            getUser(documentReference, userId)
                    .doOnSuccess(::updateCache)
        }
    }

    private fun getUser(documentReference: DocumentReference, userId: String): Single<User> {
        return RxFirestore.getDocumentSingle(documentReference, User::class.java)
                .setId(userId)
                .doOnSuccess(::updateCache)
    }

    override fun createUser(user: User): Completable {
        val documentReference = userDocument(user.id)
        return RxFirestore.setDocument(documentReference, user)
    }

    override fun getCachedCurrentUser(): Single<User> {
        return getCurrentUserId().flatMap { id ->
            userCache[id]?.let { user ->
                return@flatMap Single.just(user)
            }

            return@flatMap getUser(userDocument(id), id)
        }
    }

    override fun observeCurrentUser(): Flowable<User> {
        val currentUser = getCurrentFirebaseUser()
                ?: return Flowable.error(NoUserException)

        return observeUser(currentUser.uid)
    }

    override fun observeUser(userId: String): Flowable<User> {
        val documentReference = userDocument(userId)

        return RxFirestore.observeDocumentRef(documentReference, User::class.java)
                .setId(userId)
                .doOnNext(::updateCache)
    }


    override fun getCurrentFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getCurrentUserId(): Single<String> {
        return Single.fromCallable {
            getCurrentFirebaseUser()?.uid
                    ?: throw NoUserException
        }
    }

    override fun isCurrentUser(id: String): Boolean {
        getCurrentFirebaseUser()?.let {
            return it.uid == id
        }

        return false
    }

    private fun userDocument(userId: String) = getUsersCollection().document(userId)

    private fun getUsersCollection() = FirestoreCollection.USERS.getDbCollection()

    private fun updateCache(user: User) {
        userCache[user.id] = user
    }
}

interface UserRepository {
    fun createUser(user: User): Completable

    fun getCurrentFirebaseUser(): FirebaseUser?

    fun observeCurrentUser(): Flowable<User>

    fun getCurrentUserSingle(): Single<User>

    fun getCurrentUserId(): Single<String>

    fun getCachedCurrentUser(): Single<User>

    fun observeUser(userId: String): Flowable<User>

    fun isCurrentUser(id: String): Boolean
}