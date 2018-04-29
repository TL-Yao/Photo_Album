package com.example.apdem.photo_album;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.Util.PermissionUtils;
import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class PhotoListActivity extends AppCompatActivity {
    public static final String KEY_ALBUM = "album";
    public static final String KEY_PHOTO = "photo";
    private static final String MODEL_ALBUM = "albums";
    private static final int REQ_CODE_BACK_FROM_DELETE_PHOTO = 103;
    private static final int REQ_CODE_BACK_FROM_SHOW_PHOTO = 104;
    public static final  int PICK_IMAGE = 1;

    private Album album;
    private List<Photo> photoList;
    private List<Album> albumList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        album = getIntent().getParcelableExtra(KEY_ALBUM);
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
            //debug: 可能出现，album里出现了改动，但没有反应在showPhotoActivity里
            //debug: 一会儿要打开返回值的，要更新列表。
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoListActivity.this, ShowPhotoActivity.class);
                intent.putExtra(PhotoListActivity.KEY_ALBUM, album);
                intent.putExtra(ShowPhotoActivity.KEY_PHOTO_ID, photo.getId());
                startActivity(intent);
                //startActivityForResult(intent, REQ_CODE_BACK_FROM_PHOTO_LIST);
            }
        });
    }

    private void pickPicture(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                PICK_IMAGE);
    }

    private void backFromPickPicture(Uri uri){
        //parse temporary uri from google photo to path, then parse it to permanent Uri
        if(uri != null){
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            Uri fileUri = Uri.parse(path);
            album.addPhoto(fileUri);

            for(int i = 0; i < albumList.size(); ++i){
                if(albumList.get(i).getId().equals(album.getId())){
                    albumList.set(i, album);
                }
            }

            SaveUtils.save(this, MODEL_ALBUM, albumList);
            setupPhotos();
        }
    }



    private void updatePhoto(Album editAlbum){

        album = editAlbum;
        photoList = editAlbum.getPhotoList();
        albumList = SaveUtils.read(this,MODEL_ALBUM, new TypeToken<List<Album>>(){});
        setupPhotos();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickPicture();
        }
    }

    //return from selecting picture page, update photo list.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case PICK_IMAGE:
                    Uri uri = data.getData();
                    backFromPickPicture(uri);
                    break;
                case REQ_CODE_BACK_FROM_DELETE_PHOTO:
                    Album editAlbum = data.getParcelableExtra(DeletePhotoActivity.KEY_DELETE_ALBUM);
                    updatePhoto(editAlbum);
            }
        }
    }

    @Override
    /* listener for action bar event,
     * back to home screen if user click back button
     * save and back to home screen if user click save button
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.add_actionbar_btn) {
            if (!PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
                PermissionUtils.requestReadExternalStoragePermission(this);
            else
                pickPicture();
        } else if (item.getItemId() == R.id.delete_actionbar_btn){
            Intent intent = new Intent(this, DeletePhotoActivity.class);
            intent.putExtra(DeletePhotoActivity.KEY_DELETE_ALBUM, album);
            startActivityForResult(intent, REQ_CODE_BACK_FROM_DELETE_PHOTO);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // put add button on action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
