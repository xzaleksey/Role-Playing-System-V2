package com.alekseyvalyakin.roleplaysystem.data.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao;
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel;

@Database(entities = {PhotoInGameUploadModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoInGameDao photoInGameDao();
}