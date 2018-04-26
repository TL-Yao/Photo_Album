package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apdem.photo_album.model.Album;

public class AddAlbumActivity extends AppCompatActivity {
    public static final String KEY_NEW_ALBUM = "new_album";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        //the button back to home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void saveAndExit(){
        String name = ((EditText) findViewById(R.id.new_album_name)).getText().toString();
        Album album = new Album(name);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_NEW_ALBUM, album);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
