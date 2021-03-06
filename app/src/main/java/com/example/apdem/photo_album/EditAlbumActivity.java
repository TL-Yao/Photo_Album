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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class EditAlbumActivity extends AppCompatActivity {
    public static final String KEY_EDIT_ALBUM = "edit_album";
    public static final String KEY_DELETE_ALBUM_ID = "delete_album";
    private static final String MODEL_ALBUM = "albums";

    private Album data;
    List<Album> albumList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        albumList = SaveUtils.read(this, MODEL_ALBUM, new TypeToken<List<Album>>(){});
        data = initializeData();
        setupUIForEdit();
    }

    private void setupUIForEdit(){
        ((EditText) findViewById(R.id.edit_album_name)).setText(data.getAlbumName());

        LinearLayout delete = (LinearLayout)  findViewById(R.id.delete_album);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_DELETE_ALBUM_ID, data.getId());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private Album initializeData() {
        return getIntent().getParcelableExtra(KEY_EDIT_ALBUM);
    }

    private boolean isDuplicatedName(String name){
        for(Album album : albumList){
            if(album.getAlbumName().equals(name) &&
                    !album.getId().equals(data.getId())){
                return true;
            }
        }

        return false;
    }

    private void saveAndExit(){
        String name = ((EditText) findViewById(R.id.edit_album_name)).getText().toString().trim();

        if(name.equals("")){
            showToast("can not resolve empty album name.");
        }else if(isDuplicatedName(name)){
            showToast("already exist album with the same name");
        }else{
            data.setAlbumName(name);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(KEY_EDIT_ALBUM, data);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    private void showToast(String text){
        Toast toast=Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    /* listener for action bar event,
     * back to home screen if user click back button
     * save and back to home screen if user click save button
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if(item.getItemId() == R.id.save_btn){
            saveAndExit();
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
