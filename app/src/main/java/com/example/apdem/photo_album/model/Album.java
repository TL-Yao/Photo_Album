package com.example.apdem.photo_album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Album implements Parcelable{
    private String albumName;

    private List<Photo> photoList;

    private String id;

    public Album(String name){
        albumName = name.trim();
        photoList = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }
    protected Album(Parcel in) {
        albumName = in.readString();
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

    public int getNumPhoto() { return  photoList.size(); }

    public String getId(){ return  id; }

    public List<Photo> getPhotoList() { return photoList; }

    public void addPhoto (Uri uri){
        Photo photo = new Photo(uri);
        photoList.add(photo);
    }

    public void addPhotoObject(Photo addPhoto){
        photoList.add(addPhoto);
    }
    public void setAlbumName(String newName) { albumName = newName.trim(); }

}
