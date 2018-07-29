package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class UserRepositoryImpl : UserRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun createUser(user: User): Completable {
        val documentReference = db.collection(FirestoreCollection.USERS.directory).document(user.id)
        return RxFirestore.setDocument(documentReference, user)
    }

    override fun getUser(id: String): Maybe<User> {
        val documentReference = db.collection(FirestoreCollection.USERS.directory).document(id)
        return RxFirestore.getDocument(documentReference, User::class.java).map {
            it.id = id
            return@map it
        }
    }

    override fun observeCurrentUser(): Flowable<User> {
        val currentUser = getCurrentUser()
                ?: return Flowable.empty()

        val uid = currentUser.uid
        val documentReference = db.collection(FirestoreCollection.USERS.directory).document(uid)
        return RxFirestore.observeDocumentRef(documentReference, User::class.java)
                .map {
                    it.id = uid
                    return@map it
                }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

}

interface UserRepository {
    fun createUser(user: User): Completable

    fun getUser(id: String): Maybe<User>

    fun getCurrentUser(): FirebaseUser?

    fun observeCurrentUser(): Flowable<User>
}