package com.wintermute.soundboard.services.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.wintermute.soundboard.services.database.entities.AudioFile;

@Database(entities = {AudioFile.class}, version = 1)
public abstract class DbManager extends RoomDatabase
{

    private static DbManager INSTANCE;

    public abstract AudioFile.AudioFileDao audioFileDao();

    public static DbManager getDbManager(Context ctx){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(), DbManager.class, "playlist").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
