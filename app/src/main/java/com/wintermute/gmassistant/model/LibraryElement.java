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
public class LibraryElement implements Parcelable
{
    public static final Creator<LibraryElement> CREATOR = new Creator<LibraryElement>()
    {
        @Override
        public LibraryElement createFromParcel(Parcel in)
        {
            return new LibraryElement(in);
        }

        @Override
        public LibraryElement[] newArray(int size)
        {
            return new LibraryElement[size];
        }
    };
    private String name;
    private String path;
    private boolean isRoot;

    public LibraryElement(Parcel in)
    {
        name = in.readString();
        path = in.readString();
        isRoot = in.readByte() != 0;
    }

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
