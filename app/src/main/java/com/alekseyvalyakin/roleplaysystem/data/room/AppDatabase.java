package com.alekseyvalyakin.roleplaysystem.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame;
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao;

@Database(entities = {PhotoInGame.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoInGameDao photoInGameDao();
}