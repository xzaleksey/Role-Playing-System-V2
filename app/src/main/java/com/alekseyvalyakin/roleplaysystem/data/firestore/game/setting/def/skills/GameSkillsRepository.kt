package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable


class GameSkillsRepositoryImpl : BaseGameFireStoreRepository<UserGameSkill>(UserGameSkill::class.java), GameSkillsRepository {

    override fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameSkill>> {
        val query = getCollection(gameId)
                .orderBy(HasName.NAME_FIELD, Query.Direction.ASCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.ClassesInGame(gameId).getDbCollection()
    }

    override fun setSelected(gameId: String, id: String, selected: Boolean): Completable {
        return updateFieldOffline(id, selected, Selectable.SELECTED_FIELD, gameId)
    }

}

interface GameSkillsRepository : GameFireStoreRepository<UserGameSkill> {
    fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameSkill>>

    fun setSelected(gameId: String, id: String, selected: Boolean): Completable
}