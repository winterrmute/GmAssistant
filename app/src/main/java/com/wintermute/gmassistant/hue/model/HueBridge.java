package com.wintermute.gmassistant.hue.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HueBridge implements Parcelable
{
    private Long id;
    private String name;
    private String ip;
    private String username;
    private int active;

    protected HueBridge(Parcel in)
    {
        if (in.readByte() == 0) { id = null; } else { id = in.readLong(); }
        name = in.readString();
        ip = in.readString();
        username = in.readString();
        active = in.readInt();
    }

    public static final Creator<HueBridge> CREATOR = new Creator<HueBridge>()
    {
        @Override
        public HueBridge createFromParcel(Parcel in)
        {
            return new HueBridge(in);
        }

        @Override
        public HueBridge[] newArray(int size)
        {
            return new HueBridge[size];
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
        dest.writeString(ip);
        dest.writeString(username);
        dest.writeInt(active);
    }
}
