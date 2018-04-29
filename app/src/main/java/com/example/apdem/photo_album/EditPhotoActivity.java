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
import android.widget.TextView;
import android.widget.Toast;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.model.Photo;

import java.util.List;

public class EditPhotoActivity extends AppCompatActivity{
    public static final String KEY_EDIT_PHOTO = "edit_photo";
    public static final String KEY_DELETE_PHOTO_ID = "delete_photo_id";


    private Photo photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = getIntent().getParcelableExtra(PhotoListActivity.KEY_PHOTO);

        setupUIForEdit();
    }

    private void setupUIForEdit(){
        ((EditText) findViewById(R.id.edit_person_value)).getText().clear();
        ((EditText) findViewById(R.id.edit_location_value)).getText().clear();

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

        TextView saveAndNext = (TextView) findViewById(R.id.save_and_next);
        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveAndNext();
            }
        });

    }

    private void onClickSaveAndNext(){
        String person_value = ((EditText) findViewById(R.id.edit_person_value)).getText().toString();
        String location_value = ((EditText) findViewById(R.id.edit_location_value)).getText().toString();

        if(!person_value.equals(""))
            photo.addPerson_tag(person_value);

        if(!person_value.equals(""))
            photo.addLocation_tag(location_value);

        Toast toast=Toast.makeText(getApplicationContext(), "successful save!", Toast.LENGTH_SHORT);
        toast.show();
        setupUIForEdit();

    }

    private void saveAndExit(){
        String person_value = ((EditText) findViewById(R.id.edit_person_value)).getText().toString();
        String location_value = ((EditText) findViewById(R.id.edit_location_value)).getText().toString();

        if(!person_value.equals(""))
            photo.addPerson_tag(person_value);

        if(!person_value.equals(""))
            photo.addLocation_tag(location_value);

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
