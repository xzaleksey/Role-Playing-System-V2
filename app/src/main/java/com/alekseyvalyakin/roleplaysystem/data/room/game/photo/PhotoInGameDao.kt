package com.alekseyvalyakin.roleplaysystem.data.room.game.photo

import android.arch.persistence.room.*
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel.Companion.TABLE_NAME
import io.reactivex.Flowable

@Dao
interface PhotoInGameDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flowable<List<PhotoInGameUploadModel>>

    @Insert
    fun insert(photoInGameUploadModel: PhotoInGameUploadModel): Long

    @Update
    fun update(photoInGameUploadModel: PhotoInGameUploadModel)

    @Delete
    fun delete(photoInGameUploadModel: PhotoInGameUploadModel)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()

}