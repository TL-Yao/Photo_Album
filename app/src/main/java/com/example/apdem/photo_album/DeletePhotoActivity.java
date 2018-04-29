package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DeletePhotoActivity extends AppCompatActivity{
    public static final String KEY_DELETE_ALBUM = "delete_album";
    private static final String MODEL_ALBUM = "albums";

    private Album album;
    private List<Photo> photoList;
    private List<Album> albumList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_list);

        album = getIntent().getParcelableExtra(KEY_DELETE_ALBUM);
        photoList = album.getPhotoList();
        albumList = SaveUtils.read(this,MODEL_ALBUM, new TypeToken<List<Album>>(){});

        setupPhotos();
    }

    private void setupPhotos(){
        photoList = album.getPhotoList();

        LinearLayout photos_left = (LinearLayout) findViewById(R.id.left_photo_list);
        LinearLayout photos_right = (LinearLayout) findViewById(R.id.right_photo_list);

        photos_left.removeAllViews();
        photos_right.removeAllViews();

        boolean isLeft = true;

        for(Photo photo : photoList){
            if(isLeft){
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.thumbnail_delete, nullParent);
                setupThumbnail(albumView, photo);
                photos_left.addView(albumView);
                isLeft = false;
            }else{
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.thumbnail_delete, nullParent);
                setupThumbnail(albumView, photo);
                photos_right.addView(albumView);
                isLeft = true;
            }
        }
    }

    private void setupThumbnail(View albumView, final Photo photo){
        ImageView thumbnail = (ImageView) albumView.findViewById(R.id.delete_image);
        ImageUtils.loadImage(photo.getPhotoUri(), thumbnail);

        ImageButton delete_photo = (ImageButton) albumView.findViewById(R.id.delete_photo_btn);
        delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto(photo);
            }
        });
    }

    private void deletePhoto(Photo photo){
        String photoId = photo.getId();

        for(Album currAlbum : albumList){
            if(currAlbum.getId().equals(album.getId())){
                for(int i = 0; i < currAlbum.getPhotoList().size(); ++i){
                    if(currAlbum.getPhotoList().get(i).getId().equals(photoId)){
                        currAlbum.getPhotoList().remove(i);
                        album = currAlbum;
                        SaveUtils.save(this, MODEL_ALBUM, albumList);
                        setupPhotos();
                    }
                }
            }
        }
    }

    @Override
    /* listener for action bar event,
     * back to home screen if user click back button
     * save and back to home screen if user click save button
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save_btn){
            Intent resultIntent = new Intent();
            resultIntent.putExtra(KEY_DELETE_ALBUM, album);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // put add button on action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
