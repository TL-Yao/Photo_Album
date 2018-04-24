package com.example.apdem.photo_album.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Photo implements Parcelable{
    private String person_tag;

    private String location_tag;

    private Uri photoUri;


    public  Photo(String person, String location, Uri uri){
        person_tag = person;
        location_tag = location;
        photoUri = uri;
    }

    protected Photo(Parcel in) {
        person_tag = in.readString();
        location_tag = in.readString();
        photoUri = in.readParcelable(Uri.class.getClassLoader());
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
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
