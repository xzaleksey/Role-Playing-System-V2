package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class UserRepositoryImpl : UserRepository {
    override fun getCurrentUserSingle(): Single<User> {
        return getCurrentUserId().flatMap { userId ->
            val documentReference = getUsersCollection().document(userId)
            RxFirestore.getDocument(documentReference, User::class.java)
                    .map {
                        it.id = userId
                        return@map it
                    }.toSingle(User.EMPTY_USER)
                    .map {
                        if (it == User.EMPTY_USER) {
                            throw RuntimeException("No user found")
                        }
                        return@map it
                    }
        }
    }

    override fun createUser(user: User): Completable {
        val documentReference = getUsersCollection().document(user.id)
        return RxFirestore.setDocument(documentReference, user)
    }

    override fun getUser(id: String): Maybe<User> {
        val documentReference = getUsersCollection().document(id)
        return RxFirestore.getDocument(documentReference, User::class.java).map {
            it.id = id
            return@map it
        }
    }

    override fun observeCurrentUser(): Flowable<User> {
        val currentUser = getCurrentUser()
                ?: return Flowable.empty()

        val uid = currentUser.uid
        val documentReference = getUsersCollection().document(uid)
        return RxFirestore.observeDocumentRef(documentReference, User::class.java)
                .map {
                    it.id = uid
                    return@map it
                }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getCurrentUserId(): Single<String> {
        return Single.fromCallable {
            getCurrentUser()?.uid
                    ?: throw RuntimeException("User is absent")
        }
    }

    private fun getUsersCollection() = FirestoreCollection.USERS.getDbCollection()

}

interface UserRepository {
    fun createUser(user: User): Completable

    fun getUser(id: String): Maybe<User>

    fun getCurrentUser(): FirebaseUser?

    fun observeCurrentUser(): Flowable<User>

    fun getCurrentUserSingle(): Single<User>

    fun getCurrentUserId(): Single<String>
}