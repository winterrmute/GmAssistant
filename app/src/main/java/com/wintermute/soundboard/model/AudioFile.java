package com.wintermute.soundboard.model;

import lombok.Data;

/**
 * Represents audiofile as an java object, database entity and DAO.
 *
 * @author wintermute
 */
@Data
public class AudioFile
{
    private long id;
    private String title;
    private String artist;
    private String path;
    private long duration;


    /**
     * Object builder.
     */
    public static class Builder
    {
        private long id;
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
            AudioFile result = new AudioFile();
            result.title = this.title;
            result.artist = this.artist;
            result.path = this.path;
            //            result.duration = this.duration;
            return result;
        }
    }
}
