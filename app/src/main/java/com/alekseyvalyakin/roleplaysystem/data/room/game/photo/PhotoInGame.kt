package com.alekseyvalyakin.roleplaysystem.data.room.game.photo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame.Companion.TABLE_NAME


@Entity(tableName = TABLE_NAME)
data class PhotoInGame @Ignore constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "game_id")
        var gameId: String = "",

        @ColumnInfo(name = "file_path")
        var filePath: String = ""
) {
    constructor() : this(id = 0)

    companion object {
        const val TABLE_NAME = "photo_in_game"
    }
}