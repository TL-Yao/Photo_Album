package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.model.Photo;

public class EditPhotoActivity extends AppCompatActivity{
    public static final String KEY_EDIT_PHOTO = "edit_photo";
    public static final String KEY_DELETE_PHOTO_ID = "delete_photo_id";


    Photo photo;
    String personValue;
    String locationValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = getIntent().getParcelableExtra(PhotoListActivity.KEY_PHOTO);
        personValue = photo.getPerson_tag();
        locationValue = photo.getLocation_tag();

        setupUIForEdit();
    }

    private void setupUIForEdit(){
        ((EditText) findViewById(R.id.edit_person_value)).setText(personValue);
        ((EditText) findViewById(R.id.edit_location_value)).setText(locationValue);

        LinearLayout delete = (LinearLayout)  findViewById(R.id.delete_photo);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_DELETE_PHOTO_ID, photo.getId());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.photo_for_delete);
        ImageUtils.loadImage(photo.getPhotoUri(), imageView);

    }

    private void saveAndExit(){
        String person_val = ((EditText) findViewById(R.id.edit_person_value)).getText().toString();
        String location_val = ((EditText) findViewById(R.id.edit_location_value)).getText().toString();
        photo.setPerson_tag(person_val);
        photo.setLocation_tag(location_val);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDIT_PHOTO, photo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
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
