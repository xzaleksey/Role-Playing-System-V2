package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasTags
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.*
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import timber.log.Timber


class GameSkillsRepositoryImpl : BaseGameFireStoreRepository<UserGameSkill>(UserGameSkill::class.java), GameSkillsRepository {
    private val instance = FirebaseFirestore.getInstance()

    override fun observeCollectionsOrdered(gameId: String): Flowable<List<UserGameSkill>> {
        val query = getCollection(gameId)
                .orderBy(HasName.NAME_FIELD, Query.Direction.ASCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.SkillsInGame(gameId).getDbCollection()
    }

    override fun setSelected(gameId: String, id: String, selected: Boolean): Completable {
        return updateFieldOffline(id, selected, Selectable.SELECTED_FIELD, gameId)
    }

    override fun removeTag(tag: String, skillId: String, gameId: String): Completable {
        val skillReference = getDocumentReference(skillId, gameId)
        return RxFirestore.runTransaction(instance, Transaction.Function { transaction ->
            val documentSnapshot = transaction.get(skillReference)
            if (documentSnapshot.exists()) {
                Timber.d("Document exists")
                transaction.update(skillReference, HasTags.TAGS_FIELD, FieldValue.arrayRemove(tag))
            }
        })
    }

    override fun addTag(tag: String, skillId: String, gameId: String): Completable {
        val skillReference = getDocumentReference(skillId, gameId)
        return RxFirestore.runTransaction(instance, Transaction.Function { transaction ->
            val documentSnapshot = transaction.get(skillReference)
            if (documentSnapshot.exists()) {
                Timber.d("Document exists")
                transaction.update(skillReference, HasTags.TAGS_FIELD, FieldValue.arrayUnion(tag))
            }
        })
    }
}

interface GameSkillsRepository : GameFireStoreRepository<UserGameSkill> {
    fun observeCollectionsOrdered(gameId: String): Flowable<List<UserGameSkill>>

    fun setSelected(gameId: String, id: String, selected: Boolean): Completable

    fun removeTag(tag: String, skillId: String, gameId: String): Completable

    fun addTag(tag: String, skillId: String, gameId: String): Completable
}