package com.wintermute.soundboard.model;

import lombok.Data;

/**
 * Represents a file browsed by {@link com.wintermute.soundboard.services.FileBrowserService}
 *
 * @author wintermute
 */
@Data
public class BrowsedFile
{
    private String name;
    private String path;
    private boolean checked;

    /**
     * Object builder.
     */
    public static class Builder
    {
        private String name;
        private String path;
        private boolean checked;

        public Builder(String name)
        {
            this.name = name;
        }

        public Builder withPath(String path)
        {
            this.path = path;
            return this;
        }

        public Builder withCheckStatus(boolean checked)
        {
            this.checked = checked;
            return this;
        }

        public BrowsedFile build()
        {
            BrowsedFile result = new BrowsedFile();
            result.name = name;
            result.path = path;
            result.checked = checked;
            return result;
        }
    }
}
