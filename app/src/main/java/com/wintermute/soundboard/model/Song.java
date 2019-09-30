package com.wintermute.soundboard.model;

/**
 * Represents song contained by playlist.
 *
 * @author wintermute
 */
public class Song
{
    //    private long id;
    private String title;
    private String artist;
    private String path;
    private long duration;


    public String getPath()
    {
        return path;
    }

    public long getDuration()
    {
        return duration;
    }

    //    public long getId()
    //    {
    //        return id;
    //    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
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

        public Builder(String title){
            this.title = title;
        }

        public Builder withArtist(String artist){
            this.artist = artist;
            return this;
        }

        public Builder withPath(String path){
            this.path = path;
            return this;
        }

        public Builder withDuration(long duration){
            this.duration = duration;
            return this;
        }

        public Song build(){
            Song song = new Song();
            song.title = this.title;
            song.artist = this.artist;
            song.path = this.path;
            song.duration = this.duration;
            return song;
        }
    }
}
