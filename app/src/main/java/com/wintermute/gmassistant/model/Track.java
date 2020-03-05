package com.wintermute.gmassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
public class Track implements Parcelable
{
    private Long id;
    private String name;
    private String path;
    private Long duration;
    private String artist;
    private String tag;

    protected Track(Parcel in)
    {
        if (in.readByte() == 0) { id = null; } else { id = in.readLong(); }
        name = in.readString();
        path = in.readString();
        if (in.readByte() == 0) { duration = null; } else { duration = in.readLong(); }
        artist = in.readString();
        tag = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>()
    {
        @Override
        public Track createFromParcel(Parcel in)
        {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size)
        {
            return new Track[size];
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
        if (id == null) { dest.writeByte((byte) 0); } else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(path);
        if (duration == null) { dest.writeByte((byte) 0); } else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(duration);
        }
        dest.writeString(artist);
        dest.writeString(tag);
    }
}
