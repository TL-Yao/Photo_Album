package com.example.apdem.photo_album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apdem.photo_album.Util.ImageUtils;
import com.example.apdem.photo_album.model.Photo;

import java.util.List;

public class SearchResultPhotoActivity extends AppCompatActivity {

    Photo photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = getIntent().getParcelableExtra(SearchResultActivity.KEY_PHOTO);
        setupUI();
    }

    private void setupUI(){
        TextView person_tag = (TextView) findViewById(R.id.result_person_value);
        TextView location_tag = (TextView) findViewById(R.id.result_location_value);
        ImageView image = (ImageView) findViewById(R.id.result_photo);

        person_tag.setText(buildPersonTag(photo.getPerson_tag()));
        location_tag.setText(buildLocationTag(photo.getLocation_tag()));
        ImageUtils.loadImage(photo.getPhotoUri(), image);
    }

    private String buildPersonTag(List<String> listPersonTag){

        StringBuilder sb = new StringBuilder();
        for(String s : listPersonTag){
            sb.append(s);
            sb.append(',');
        }

        if(sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));

        return sb.toString();
    }

    private String buildLocationTag(List <String> listLocationTag){
        StringBuilder sb = new StringBuilder();
        for(String s : listLocationTag){
            sb.append(s);
            sb.append(',');
        }

        if(sb.lastIndexOf(",") != -1)
            sb.deleteCharAt(sb.lastIndexOf(","));

        return sb.toString();
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
