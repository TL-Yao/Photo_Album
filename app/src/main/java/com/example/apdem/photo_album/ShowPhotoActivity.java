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
import android.view.Menu;
import android.view.MenuInflater;
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
    private static final int REQ_CODE_BACK_FROM_EDITPHOTO = 103;

    private List<Photo> photoList;
    private List<Album> albumList;
    private String photo_id;
    Photo photo;
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
        photo = photoList.get(findPhoto());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                photo = photoList.get(position);
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

        person_tag.setText(buildPersonTag(photo.getPerson_tag()));
        location_tag.setText(buildLocationTag(photo.getLocation_tag()));
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
        }else if(item.getItemId() == R.id.menu_edit){
            Intent intent = new Intent(ShowPhotoActivity.this, EditPhotoActivity.class);
            intent.putExtra(PhotoListActivity.KEY_PHOTO, photo);
            startActivityForResult(intent, REQ_CODE_BACK_FROM_EDITPHOTO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO
    }

    @Override
    // put edit button on action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
