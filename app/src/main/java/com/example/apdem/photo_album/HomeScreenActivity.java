package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    private static final int REQ_CODE_ADD_ALBUM = 100;
    private static final int REQ_CODE_EDIT_ALBUM = 101;
    private static final int REQ_CODE_BACK_FROM_PHOTO_LIST = 102;
    private static final String MODEL_ALBUM = "albums";
    private List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        loadData();
        setupAlbums();
    }

    private void loadData(){
        List<Album> saveAlbums = SaveUtils.read(this, MODEL_ALBUM, new TypeToken<List<Album>>(){});
        albumList = saveAlbums == null ? new ArrayList<Album>() : saveAlbums;
    }

    private void setupAlbums(){
        LinearLayout albums_left = (LinearLayout) findViewById(R.id.albums_left);
        LinearLayout albums_right = (LinearLayout) findViewById(R.id.albums_right);

        albums_left.removeAllViews();
        albums_right.removeAllViews();

        boolean isLeft = true;
        for(Album album : albumList){
            if(isLeft){
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.album, nullParent);
                setupAlbum(albumView, album);
                albums_left.addView(albumView);
                isLeft = false;
            }else{
                final ViewGroup nullParent = null;
                View albumView = getLayoutInflater().inflate(R.layout.album, nullParent);
                setupAlbum(albumView, album);
                albums_right.addView(albumView);
                isLeft = true;
            }
        }
    }

    //update AlbumList after editing
    private void updateAlbum(Album editAlbum) {

        for(int i = 0; i < albumList.size(); ++i){

            Album a =  albumList.get(i);

            System.out.print("here " + a.getId());
            if(TextUtils.equals(a.getId(), editAlbum.getId())){
                albumList.set(i, editAlbum);
                break;
            }
        }

        SaveUtils.save(this, MODEL_ALBUM, albumList);
        setupAlbums();
    }

    private void deleteAlbum(String id){
        for(int i = 0; i < albumList.size(); ++i){
            Album a = albumList.get(i);
            if (TextUtils.equals(a.getId(), id)){
                albumList.remove(i);
                break;
            }
        }

        SaveUtils.save(this, MODEL_ALBUM, albumList);
        setupAlbums();
    }

    //setup each album view in albums
    private void setupAlbum(View albumView, final Album album) {
        ((TextView) albumView.findViewById(R.id.album_name)).setText(album.getAlbumName());
        ((TextView) albumView.findViewById(R.id.num_photo)).setText(String.valueOf(album.getNumPhoto()));

        //setup edit image button
        ImageButton editAlbumName = (ImageButton) albumView.findViewById(R.id.edit_album_btn);
        editAlbumName.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, EditAlbumActivity.class);
                intent.putExtra(EditAlbumActivity.KEY_EDIT_ALBUM, album);
                startActivityForResult(intent, REQ_CODE_EDIT_ALBUM);
            }
        });

        if(album.getNumPhoto() != 0) {
            //setup cover of the album
            ImageView cover = (ImageView) albumView.findViewById(R.id.cover);
            Uri firstPic = album.getPhotoList().get(0).getPhotoUri();
            ImageUtils.loadImage(HomeScreenActivity.this, firstPic, cover);
        }
        //onclick listener for opening album
        LinearLayout albumFrame = (LinearLayout) albumView.findViewById(R.id.album_frame);
        albumFrame.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, PhotoListActivity.class);
                intent.putExtra(PhotoListActivity.KEY_PHOTO_LIST, album);
                startActivityForResult(intent, REQ_CODE_BACK_FROM_PHOTO_LIST);
            }
        });
    }

    @Override
    //receive data from other activities
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                //return from add album activity,put into the list and save in disk
                case REQ_CODE_ADD_ALBUM:
                    Album newAlbum = data.getParcelableExtra(AddAlbumActivity.KEY_NEW_ALBUM);
                    albumList.add(newAlbum);
                    setupAlbums();
                    SaveUtils.save(this, MODEL_ALBUM, albumList);
                    break;
                //return from edit album activity
                case REQ_CODE_EDIT_ALBUM:
                    String album_id = data.getStringExtra(EditAlbumActivity.KEY_DELETE_ALBUM_ID);
                    if(album_id != null){
                        deleteAlbum(album_id);
                    }else{
                        Album editAlbum = data.getParcelableExtra(EditAlbumActivity.KEY_EDIT_ALBUM);
                        updateAlbum(editAlbum);
                    }
                    break;
                case REQ_CODE_BACK_FROM_PHOTO_LIST:
                    loadData();
                    setupAlbums();
                    break;
            }
        }
    }

    @Override
    //switch to new activity if user click the add button on action bar
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(HomeScreenActivity.this, AddAlbumActivity.class);
        startActivityForResult(intent, REQ_CODE_ADD_ALBUM);
        return super.onOptionsItemSelected(item);
    }

    @Override
    // put add button on action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
