package com.alekseyvalyakin.roleplaysystem.data.room.game.photo

import android.arch.persistence.room.*
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame.Companion.TABLE_NAME
import io.reactivex.Flowable

@Dao
interface PhotoInGameDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flowable<List<PhotoInGame>>

    @Insert
    fun insert(photoInGame: PhotoInGame): Long

    @Update
    fun update(photoInGame: PhotoInGame)

    @Delete
    fun delete(photoInGame: PhotoInGame)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()

}