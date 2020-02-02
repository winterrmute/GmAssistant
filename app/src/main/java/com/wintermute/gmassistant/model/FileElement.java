package com.wintermute.gmassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents file collected by {@link com.wintermute.gmassistant.services.FileBrowserService}
 */
@Data
@AllArgsConstructor
public class FileElement implements Parcelable
{
    private String name;
    private String path;
    private boolean isRoot;

    protected FileElement(Parcel in)
    {
        name = in.readString();
        path = in.readString();
        isRoot = in.readByte() != 0;
    }

    public static final Creator<FileElement> CREATOR = new Creator<FileElement>()
    {
        @Override
        public FileElement createFromParcel(Parcel in)
        {
            return new FileElement(in);
        }

        @Override
        public FileElement[] newArray(int size)
        {
            return new FileElement[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeByte((byte) (isRoot ? 1 : 0));
    }
}
