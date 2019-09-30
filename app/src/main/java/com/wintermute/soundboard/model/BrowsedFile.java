package com.wintermute.soundboard.model;

/**
 * Represents a file browsed by {@link com.wintermute.soundboard.services.FileBrowserService}
 *
 * @author wintermute
 */
public class BrowsedFile
{
    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }

    private String name;
    private String path;

    /**
     * Object builder.
     */
    public static class Builder
    {
        private String name;
        private String path;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder withPath(String path){
            this.path = path;
            return this;
        }

        public BrowsedFile build(){
            BrowsedFile result = new BrowsedFile();
            result.name = name;
            result.path = path;
            return result;
        }
    }
}
