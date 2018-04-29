package com.example.apdem.photo_album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Photo implements Parcelable{
    private List<String> person_tag;

    private List<String> location_tag;

    private Uri photoUri;

    private String id;

    public Photo (Uri uri){
        person_tag = new ArrayList<>();
        location_tag = new ArrayList<>();
        photoUri = uri;
        id = UUID.randomUUID().toString();
    }

    protected Photo(Parcel in) {
        if(person_tag ==null){
            person_tag = new ArrayList<String>();
        }
        in.readList(person_tag, String.class.getClassLoader());
        if(location_tag ==null){
            location_tag = new ArrayList<String>();
        }
        in.readList(location_tag, String.class.getClassLoader());
        photoUri = in.readParcelable(Uri.class.getClassLoader());
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(person_tag);
        parcel.writeList(location_tag);
        parcel.writeParcelable(photoUri, i);
        parcel.writeString(id);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) { return new Photo[size]; }
    };

    public Uri getPhotoUri() { return photoUri; }
    public String getId() { return id; }
    public List<String> getPerson_tag() { return person_tag; }
    public List<String> getLocation_tag() { return  location_tag; }

    public void addPerson_tag(String value) { person_tag.add(value); }
    public void addLocation_tag(String value) { location_tag.add(value); }
}
