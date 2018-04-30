package com.example.apdem.photo_album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    public static final String KEY_SEARCH_RESULT = "search_result";
    public static final String KEY_PHOTO = "key_photo";
    Album album;
    List<Photo> photoList;
    Photo passPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        album = getIntent().getParcelableExtra(KEY_SEARCH_RESULT);
        System.out.println(album.getPhotoList().size());
        photoList = album.getPhotoList();

        setupPhotos();
    }

    private void setupPhotos(){

        LinearLayout photos_left = (LinearLayout) findViewById(R.id.left_photo_list);
        LinearLayout photos_right = (LinearLayout) findViewById(R.id.right_photo_list);

        photos_left.removeAllViews();
        photos_right.removeAllViews();

        boolean isLeft = true;

        for(Photo photo : photoList){
            if(isLeft){
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.thumbnail, nullParent);
                setupThumbnail(albumView, photo);
                photos_left.addView(albumView);
                isLeft = false;
            }else{
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.thumbnail, nullParent);
                setupThumbnail(albumView, photo);
                photos_right.addView(albumView);
                isLeft = true;
            }
        }
    }

    private void setupThumbnail(View albumView, final Photo photo){
        ImageView thumbnail = (ImageView) albumView.findViewById(R.id.thumbnail);
        ImageUtils.loadImage(photo.getPhotoUri(), thumbnail);
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResultActivity.this, SearchResultPhotoActivity.class);
                intent.putExtra(KEY_PHOTO, photo);
                System.out.println(photo.getPerson_tag().size() + "    " +  photo.getLocation_tag().size());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
