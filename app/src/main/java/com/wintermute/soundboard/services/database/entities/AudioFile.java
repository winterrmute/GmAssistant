package com.wintermute.soundboard.services.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Represents song contained by playlist.
 *
 * @author wintermute
 */
@Entity(tableName = "audiofile")
public class AudioFile
{
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "duration")
    private long duration;

    public String getPath()
    {
        return path;
    }

    public long getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    /**
     * Object builder.
     */
    public static class Builder
    {
        private String title;
        private String artist;
        private String path;
        private long duration;

        public Builder(String title)
        {
            this.title = title;
        }

        public Builder withArtist(String artist)
        {
            this.artist = artist;
            return this;
        }

        public Builder withPath(String path)
        {
            this.path = path;
            return this;
        }

        public Builder withDuration(long duration)
        {
            this.duration = duration;
            return this;
        }

        public AudioFile build()
        {
            AudioFile song = new AudioFile();
            song.title = this.title;
            song.artist = this.artist;
            song.path = this.path;
            //            song.duration = this.duration;
            return song;
        }
    }

    @Dao
    public interface AudioFileDao
    {
        @Query("SELECT * FROM audiofile WHERE title LIKE :title")
        AudioFile getAudioFile(String title);

        @Query("SELECT * FROM audiofile")
        List<AudioFile> getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void initAudioFiles(AudioFile... audioFiles);

        @Update
        void updateAudioFile(AudioFile... audioFiles);

//        @Query("DELETE FROM audiofile WHERE id = :audioFileId")
        @Delete
        void deleteAudioFile(AudioFile audioFile);

        @Insert
        void insertAll(AudioFile... audioFiles);

        @Insert
        long insertAudioFile(AudioFile audioFile);
    }
}
