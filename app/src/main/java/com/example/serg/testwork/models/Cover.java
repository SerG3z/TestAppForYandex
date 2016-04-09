package com.example.serg.testwork.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by serg on 09.04.16.
 */
public class Cover implements Parcelable {
    private String small;
    private String big;

    public String getSmall() {
        return small;
    }

    public String getBig() {
        return big;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(small);
        parcel.writeString(big);
    }

    protected Cover(Parcel in) {
        small = in.readString();
        big = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };
}
