package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.currentUser.CurrentUserInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.currentUser.NoUserException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.rxfirebase2.RxFirebaseUser
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
            getUser(documentReference)
                    .doOnSuccess(::updateCache)
        }
    }

    private fun getUser(documentReference: DocumentReference): Single<User> {
        return RxFirestore.getDocumentSingleHasId(documentReference, User::class.java)
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

            return@flatMap getUser(userDocument(id))
        }
    }

    override fun onDisplayNameChanged(name: String): Completable {
        return getCurrentFirebaseUser()?.run {
            return updateField(this.uid, name, User.FIELD_DISPLAY_NAME)
                    .andThen(RxFirebaseUser.updateProfile(this,
                            UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build())
                    )
        } ?: Completable.error(RuntimeException("no current user"))
    }

    override fun observeCurrentUser(): Flowable<User> {
        val currentUser = getCurrentUserInfo()
                ?: return Flowable.error(NoUserException)
        val currentUserCache = userCache[currentUser.uid]
        val flowable = observeUser(currentUser.uid)
        if (currentUserCache != null) {
            return flowable.startWith(currentUserCache)
        }
        return flowable
    }

    override fun observeUser(userId: String): Flowable<User> {
        val documentReference = userDocument(userId)

        return RxFirestore.observeDocumentRefHasId(documentReference, User::class.java)
                .doOnNext(::updateCache)
    }

    override fun getCurrentUserInfo(): CurrentUserInfo? {
        val currentUser = getCurrentFirebaseUser()
                ?: return null

        return CurrentUserInfo(currentUser)
    }

    private fun getCurrentFirebaseUser() = FirebaseAuth.getInstance().currentUser

    override fun getCurrentUserId(): Single<String> {
        return Single.fromCallable {
            getCurrentUserInfo()?.uid
                    ?: throw NoUserException
        }
    }

    override fun isCurrentUser(id: String): Boolean {
        getCurrentUserInfo()?.let {
            return it.uid == id
        }

        return false
    }

    override fun updatePhotoUrl(url: String): Completable {
        return getCurrentUserId().flatMapCompletable {
            updateField(it, url, User.FIELD_PHOTO_URL)
        }
    }


    private fun userDocument(userId: String) = getUsersCollection().document(userId)

    private fun updateField(id: String, text: String, fieldName: String): Completable {
        val document = getUsersCollection().document(id)
        return RxFirestore.updateDocumentOffline(document, mapOf(fieldName to text))
    }

    private fun getUsersCollection() = FirestoreCollection.USERS.getDbCollection()

    private fun updateCache(user: User) {
        userCache[user.id] = user
    }
}

interface UserRepository {
    fun getCurrentUserInfo(): CurrentUserInfo?

    fun isCurrentUser(id: String): Boolean

    fun getCurrentUserSingle(): Single<User>

    fun createUser(user: User): Completable

    fun observeCurrentUser(): Flowable<User>

    fun getCurrentUserId(): Single<String>

    fun getCachedCurrentUser(): Single<User>

    fun observeUser(userId: String): Flowable<User>

    fun onDisplayNameChanged(name: String): Completable

    fun updatePhotoUrl(url: String): Completable
}