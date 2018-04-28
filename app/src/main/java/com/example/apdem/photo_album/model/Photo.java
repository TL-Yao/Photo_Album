package com.example.apdem.photo_album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.UUID;

public class Photo implements Parcelable{
    private String person_tag;

    private String location_tag;

    private Uri photoUri;

    private String id;

    public Photo (String person, String location, Uri uri){
        person_tag = person;
        location_tag = location;
        photoUri = uri;
        id = UUID.randomUUID().toString();
    }

    protected Photo(Parcel in) {
        person_tag = in.readString();
        location_tag = in.readString();
        photoUri = in.readParcelable(Uri.class.getClassLoader());
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(person_tag);
        parcel.writeString(location_tag);
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
    public String getPerson_tag() { return person_tag; }
    public String getLocation_tag() { return  location_tag; }

    public void setPerson_tag(String person_val) { person_tag = person_val; }
    public void setLocation_tag(String location_val) { location_tag = location_val; }

}
