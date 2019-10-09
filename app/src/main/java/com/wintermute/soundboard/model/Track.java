package com.wintermute.soundboard.model;

import lombok.Data;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
public class Track
{
    private String id;
    private String name;
    private String artist;
    private String type;
    private String path;
    private String sceneId;

    public static class Builder {

        private String id;
        private String name;
        private String artist;
        private String type;
        private String path;
        private String sceneId;

        public Builder(){
        }

        public Builder withId(String id){
            this.id = id;
            return this;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withArtist(String artist){
            this.artist = artist;
            return this;
        }

        public Builder withType(String type){
            this.type = type;
            return this;
        }

        public Builder withPath(String path){
            this.path = path;
            return this;
        }

        public Builder withScene(String sceneId){
            this.sceneId = sceneId;
            return this;
        }

        public Track build(){
            Track result = new Track();
            result.id = this.id;
            result.name = this.name;
            result.artist = this.artist;
            result.type = this.type;
            result.path = this.path;
            result.sceneId = this.sceneId;
            return result;
        }
    }
}
