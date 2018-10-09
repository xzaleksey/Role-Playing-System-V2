package com.alekseyvalyakin.roleplaysystem.data.firestore.tags

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import timber.log.Timber

class GameTagsRepositoryImpl() : BaseGameFireStoreRepository<Tag>(Tag::class.java), GameTagsRepository {
    private val instance = FirebaseFirestore.getInstance()

    override fun observeTagsOrdered(gameId: String): Flowable<List<Tag>> {
        val query = getCollection(gameId)
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query, gameId)
    }


    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.TagsInGame(gameId).getDbCollection()
    }

    override fun addSkill(id: String, skillId: String, gameId: String): Completable {
        val tag = Tag(id = id, skillIds = listOf(skillId))
        val tagReference = getDocumentReference(id, gameId)
        return RxFirestore.runTransaction(instance, Transaction.Function { transaction ->
            val documentSnapshot = transaction.get(tagReference)
            if (!documentSnapshot.exists()) {
                Timber.d("creating document $tag")
                transaction.set(tagReference, tag)
            } else {
                Timber.d("Document exists")
                transaction.update(tagReference, Tag.SKILL_IDS_FIELD, FieldValue.arrayUnion(skillId))
            }
        })
    }
}

interface GameTagsRepository : GameFireStoreRepository<Tag> {
    fun observeTagsOrdered(gameId: String): Flowable<List<Tag>>

    fun addSkill(id: String, skillId: String, gameId: String): Completable
}