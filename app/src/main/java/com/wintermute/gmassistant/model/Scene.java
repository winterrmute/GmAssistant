package com.wintermute.gmassistant.model;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a scene contained by playlist. A scene holds the track to play, light belonging to it and knows the next
 * track to play.
 *
 * @author wintermute
 */
@Data
@NoArgsConstructor
public class Scene implements Parcelable
{
    private Long id;
    private String name;
    private Light light;
    private Track effect;
    private Track music;
    private Track ambience;

    protected Scene(Parcel in)
    {
        if (in.readByte() == 0) { id = null; } else { id = in.readLong(); }
        name = in.readString();
        this.light = (Light) in.readSerializable();
        this.effect = in.readParcelable(Track.class.getClassLoader());
        this.music = in.readParcelable(Track.class.getClassLoader());
        this.ambience = in.readParcelable(Track.class.getClassLoader());
    }

    public static final Creator<Scene> CREATOR = new Creator<Scene>()
    {
        @Override
        public Scene createFromParcel(Parcel in)
        {
            return new Scene(in);
        }

        @Override
        public Scene[] newArray(int size)
        {
            return new Scene[size];
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
        if (id != null)
        {
            dest.writeInt(1);
            dest.writeLong(id);
        } else
        {
            dest.writeInt(0);
        }
        dest.writeString(name);
        dest.writeSerializable(light);
        dest.writeParcelable(effect, flags);
        dest.writeParcelable(music, flags);
        dest.writeParcelable(ambience, flags);
    }
}
