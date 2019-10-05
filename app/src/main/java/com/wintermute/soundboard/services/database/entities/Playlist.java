package com.wintermute.soundboard.services.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist")
public class Playlist
{
    Playlist(String name)
    {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    long playlistId;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "songs")
    String songs;

    @Dao
    public interface PlaylistDao
    {
        @Insert
        public void insertNewPlaylist(Playlist playlist);
    }
}
