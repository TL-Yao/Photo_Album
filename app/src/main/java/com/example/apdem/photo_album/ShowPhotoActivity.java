package com.example.apdem.photo_album;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.apdem.photo_album.Util.PermissionUtils;
import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ShowPhotoActivity extends AppCompatActivity{
    private static final String MODEL_ALBUM = "albums";
    public static  final String KEY_PHOTO_ID = "photo_id";

    private List<Photo> photoList;
    private List<Album> albumList;
    private String photo_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Album album = getIntent().getParcelableExtra(PhotoListActivity.KEY_ALBUM);
        photoList = album.getPhotoList();
        albumList = SaveUtils.read(this,MODEL_ALBUM, new TypeToken<List<Album>>(){});
        photo_id = getIntent().getStringExtra(KEY_PHOTO_ID);

        ViewPager viewPager = (ViewPager) findViewById(R.id.photo_page);
        viewPager.setAdapter(new photoAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(findPhoto());

        Photo photo = photoList.get(findPhoto());

        TextView person_tag = (TextView) findViewById(R.id.person_value);
        TextView location_tag = (TextView) findViewById(R.id.location_value);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setupUI(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupUI(int position){
        Photo photo = photoList.get(position);

        TextView person_tag = (TextView) findViewById(R.id.person_value);
        TextView location_tag = (TextView) findViewById(R.id.location_value);

        person_tag.setText(photo.getPerson_tag());
        location_tag.setText(photo.getLocation_tag());
    }

    private class photoAdapter extends FragmentPagerAdapter {

        public photoAdapter(FragmentManager fm) { super(fm); }

        @Override
        public Fragment getItem(int position) { return PhotoFragment.newInstance(photoList.get(position).getPhotoUri()); }

        @Override
        public int getCount() { return photoList.size(); }
    }

    private int findPhoto(){
        int position = -1;

        for(int i = 0; i < photoList.size(); ++i){
            if(photoList.get(i).getId().equals(photo_id)){
                position = i;
                break;
            }
        }

        return position;
    }

    @Override
    /* listener for action bar event,
     * back to home screen if user click back button
     * save and back to home screen if user click save button
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
