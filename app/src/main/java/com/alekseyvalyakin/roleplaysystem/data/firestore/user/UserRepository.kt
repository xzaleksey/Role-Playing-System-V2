package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.google.firebase.firestore.FirebaseFirestore
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Maybe

class UserRepositoryImpl : UserRepository {
    val db = FirebaseFirestore.getInstance()

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

}

interface UserRepository {
    fun createUser(user: User): Completable

    fun getUser(id: String): Maybe<User>
}