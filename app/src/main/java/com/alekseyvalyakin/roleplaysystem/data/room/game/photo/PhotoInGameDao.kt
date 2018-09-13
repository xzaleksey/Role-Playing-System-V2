package com.alekseyvalyakin.roleplaysystem.data.room.game.photo

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame.Companion.TABLE_NAME
import io.reactivex.Flowable

@Dao
interface PhotoInGameDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flowable<List<PhotoInGame>>

    @Insert
    fun insert(photoInGame: PhotoInGame)

    @Delete
    fun delete(photoInGame: PhotoInGame)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()

}