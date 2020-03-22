package com.wintermute.gmassistant.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Light implements Parcelable
{
    private Long id;
    private Long color;
    private Long brightness;
    private Long resetLight;

    private Light(Parcel in)
    {
        if (in.readByte() == 0) { id = null; } else { id = in.readLong(); }
        if (in.readByte() == 0) { color = null; } else { color = in.readLong(); }
        if (in.readByte() == 0) { brightness = null; } else { brightness = in.readLong(); }
        if (in.readByte() == 0) { resetLight = null; } else { resetLight = in.readLong(); }
    }

    public static final Creator<Light> CREATOR = new Creator<Light>()
    {
        @Override
        public Light createFromParcel(Parcel in)
        {
            return new Light(in);
        }

        @Override
        public Light[] newArray(int size)
        {
            return new Light[size];
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
        if (color == null) { dest.writeByte((byte) 0); } else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(color);
        }
        if (brightness == null) { dest.writeByte((byte) 0); } else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(brightness);
        }
        if (resetLight == null) { dest.writeByte((byte) 0); } else
        {
            dest.writeByte((byte) 1);
            dest.writeLong(resetLight);
        }
    }
}
