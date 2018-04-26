package com.example.apdem.photo_album.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album implements Parcelable{
    private String albumName;

    private int numPhoto;

    private List<Photo> photoList;

    private String id;

    public Album(String name){
        albumName = name.trim();
        numPhoto = 0;
        photoList = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }
    protected Album(Parcel in) {
        albumName = in.readString();
        numPhoto = in.readInt();
        photoList = in.createTypedArrayList(Photo.CREATOR);
        id = in.readString();
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
        parcel.writeString(id);
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

    public String getAlbumName(){ return albumName; }
    public int getNumPhoto() { return  numPhoto; }
    public String getId(){ return  id; }
    public void setAlbumName(String newName) { albumName = newName.trim(); }
}
