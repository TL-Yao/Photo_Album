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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apdem.photo_album.model.LocationTagListAdapter;
import com.example.apdem.photo_album.model.PersonTagListAdapter;
import com.example.apdem.photo_album.model.Photo;

public class EditTagActivity extends AppCompatActivity{
    public static final String KEY_EDIT_PHOTO = "edit_photo";
    public static final String KEY_DELETE_PHOTO_ID = "delete_photo_id";


    private Photo photo;
    int personTagSelectIndex = -1;
    int locationTagSelectIndex = -1;
    ListView personTagList;
    ListView locationTagList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_tag);

        photo = getIntent().getParcelableExtra(ShowPhotoActivity.KEY_PHOTO);

        setupUIForEdit();
    }

    private void setupUIForEdit(){

        ((EditText) findViewById(R.id.edit_person_value)).getText().clear();
        ((EditText) findViewById(R.id.edit_location_value)).getText().clear();

        TextView saveAndNext = (TextView) findViewById(R.id.save_and_next_btn);
        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSaveAndNext();
            }
        });

        personTagList = (ListView) findViewById(R.id.person_tag_list);
        personTagList.setAdapter(new PersonTagListAdapter(this, photo.getPerson_tag()));
        personTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                personTagSelectIndex = index;
                view.setSelected(true);
            }
        });

        locationTagList = (ListView) findViewById(R.id.location_tag_list);
        locationTagList.setAdapter(new LocationTagListAdapter(this, photo.getLocation_tag()));
        locationTagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                locationTagSelectIndex = index;
                view.setSelected(true);
            }
        });

        TextView deletePersonTag = (TextView) findViewById(R.id.delete_selecting_person_tag_btn);
        deletePersonTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!personTagList.getAdapter().isEmpty() && personTagSelectIndex != -1) {
                    photo.deletePersonTag(personTagSelectIndex);
                    personTagSelectIndex = -1;
                }

                personTagList.setAdapter(new PersonTagListAdapter(EditTagActivity.this, photo.getPerson_tag()));
            }
        });

        TextView deleteLocationTag = (TextView) findViewById(R.id.delete_selecting_location_tag_btn);
        deleteLocationTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locationTagList.getAdapter().isEmpty() && locationTagSelectIndex != -1) {
                    photo.deleteLocationTag(locationTagSelectIndex);
                    locationTagSelectIndex = -1;
                }

                locationTagList.setAdapter(new LocationTagListAdapter(EditTagActivity.this, photo.getLocation_tag()));
            }
        });


    }

    private void onClickSaveAndNext(){
        String person_value = ((EditText) findViewById(R.id.edit_person_value)).getText().toString();
        String location_value = ((EditText) findViewById(R.id.edit_location_value)).getText().toString();

        if(!person_value.equals("")) {
            photo.addPerson_tag(person_value);
            Toast toast=Toast.makeText(getApplicationContext(), "successful save!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(!location_value.equals("")) {
            photo.addLocation_tag(location_value);
            Toast toast=Toast.makeText(getApplicationContext(), "successful save!", Toast.LENGTH_SHORT);
            toast.show();
        }

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
        if(item.getItemId() == R.id.save_btn){
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
