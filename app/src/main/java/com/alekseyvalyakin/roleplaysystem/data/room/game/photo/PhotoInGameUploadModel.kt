package com.alekseyvalyakin.roleplaysystem.data.room.game.photo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class PhotoInGameUploadModel @Ignore constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "work_id")
        var workId: String = "",

        @ColumnInfo(name = "game_id")
        var gameId: String = "",

        @ColumnInfo(name = "file_path")
        var filePath: String = "",

        @ColumnInfo(name = "photoId")
        var photoId: String = ""
) {
    constructor() : this(id = 0)

    companion object {
        const val TABLE_NAME = "photo_in_game"
        const val STORAGE_KEY = "photos"
    }
}