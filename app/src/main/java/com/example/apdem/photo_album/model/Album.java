package com.example.apdem.photo_album.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Album implements Parcelable{
    private String albumName;

    private int numPhoto;

    private List<Photo> photoList;


    protected Album(Parcel in) {
        albumName = in.readString();
        numPhoto = in.readInt();
        photoList = in.createTypedArrayList(Photo.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(albumName);
        parcel.writeInt(numPhoto);
        parcel.writeTypedList(photoList);
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
