package com.alekseyvalyakin.roleplaysystem.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel;
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao;

@Database(entities = {PhotoInGameUploadModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoInGameDao photoInGameDao();
}