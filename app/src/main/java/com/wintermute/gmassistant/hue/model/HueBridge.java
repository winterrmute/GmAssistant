package com.wintermute.gmassistant.hue.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HueBridge implements Parcelable
{
    private String name;
    private String ip;
    private String username;

    protected HueBridge(Parcel in)
    {
        name = in.readString();
        ip = in.readString();
        username = in.readString();
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
        dest.writeString(name);
        dest.writeString(ip);
        dest.writeString(username);
    }
}
