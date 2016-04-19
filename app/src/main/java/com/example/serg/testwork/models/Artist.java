package com.example.serg.testwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serg on 09.04.16.
 */
public class Artist implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("genres")
    private String[] genres;
    @SerializedName("tracks")
    private int tracks;
    @SerializedName("albums")
    private int albums;
    @SerializedName("link")
    private String link;
    @SerializedName("description")
    private String description;
    @SerializedName("cover")
    private Cover cover;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeStringArray(genres);
        parcel.writeInt(tracks);
        parcel.writeInt(albums);
        parcel.writeString(link);
        parcel.writeString(description);
        parcel.writeParcelable(cover, 1);
    }

    protected Artist(Parcel in) {
        id = in.readInt();
        name = in.readString();
        genres = in.createStringArray();
        tracks = in.readInt();
        albums = in.readInt();
        link = in.readString();
        description = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
