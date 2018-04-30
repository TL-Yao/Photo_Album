package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MovePhotoActivity extends AppCompatActivity {
    private static final String MODEL_ALBUM = "albums";
    public static final String KEY_MOVE_PHOTO = "move_photo";
    public static final String KEY_MOVE_PHOTO_ALBUM_ID = "move_photo_album_id";

    private List<Album> albumList;
    private Photo photo;
    String album_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);

        albumList = SaveUtils.read(this, MODEL_ALBUM, new TypeToken<List<Album>>(){});
        photo = getIntent().getParcelableExtra(KEY_MOVE_PHOTO);
        album_id = getIntent().getStringExtra(KEY_MOVE_PHOTO_ALBUM_ID);
        setupAlbums();
    }

    private void setupAlbums(){
        LinearLayout albums_left = (LinearLayout) findViewById(R.id.albums_left);
        LinearLayout albums_right = (LinearLayout) findViewById(R.id.albums_right);

        albums_left.removeAllViews();
        albums_right.removeAllViews();

        boolean isLeft = true;
        for(Album album : albumList){
            if(album.getId().equals(album_id)){
                continue;
            }

            if(isLeft){
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.album_for_move, nullParent);
                setupAlbum(albumView, album);
                albums_left.addView(albumView);
                isLeft = false;
            }else{
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.album_for_move, nullParent);
                setupAlbum(albumView, album);
                albums_right.addView(albumView);
                isLeft = true;
            }
        }
    }

    private void setupAlbum(View albumView, final Album album) {
        ((TextView) albumView.findViewById(R.id.album_name_move)).setText(album.getAlbumName());
        ((TextView) albumView.findViewById(R.id.num_photo_move)).setText(String.valueOf(album.getNumPhoto()));

        if(album.getNumPhoto() != 0) {
            //setup cover of the album
            ImageView cover = (ImageView) albumView.findViewById(R.id.cover_move);
            Uri firstPic = album.getPhotoList().get(0).getPhotoUri();
            ImageUtils.loadImage( firstPic, cover);
        }
        //onclick listener for opening album
        LinearLayout albumFrame = (LinearLayout) albumView.findViewById(R.id.album_frame_move);
        albumFrame.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                moveAndExit(album);
            }
        });
    }

    private void moveAndExit(Album album){
        album.getPhotoList().add(photo);
        SaveUtils.save(this, MODEL_ALBUM, albumList);
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


}
