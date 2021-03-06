package com.example.apdem.photo_album;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apdem.photo_album.Util.SaveUtils;
import com.example.apdem.photo_album.model.Album;
import com.example.apdem.photo_album.model.Photo;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchByTagActivity extends AppCompatActivity {
    private static final String MODEL_ALBUM = "albums";

    List<Album> albumList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumList = SaveUtils.read(this, MODEL_ALBUM, new TypeToken<List<Album>>(){});
        setupSearchUI();
    }

    private void setupSearchUI(){

        final TextView andSearchBtn = (TextView) findViewById(R.id.and_search_btn);
        andSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personSearchValue = ((EditText) findViewById(R.id.search_person_value)).getText().toString();
                String locationSearchValue = ((EditText) findViewById(R.id.search_location_value)).getText().toString();

                final List<String> personToken = splitTag(personSearchValue);
                final List<String> locationToken = splitTag(locationSearchValue);

                if(personToken.size() == 0 && locationToken.size() == 0){
                    showToast("at least type one tag in one field.");
                }else{
                    Album resultAlbum = andSearch(personToken, locationToken);
                    Intent intent = new Intent(SearchByTagActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.KEY_SEARCH_RESULT, resultAlbum);
                    startActivity(intent);
                }

            }
        });

        TextView orSearchBtn = (TextView) findViewById(R.id.or_search_btn);
        orSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String personSearchValue = ((EditText) findViewById(R.id.search_person_value)).getText().toString();
                String locationSearchValue = ((EditText) findViewById(R.id.search_location_value)).getText().toString();

                final List<String> personToken = splitTag(personSearchValue);
                final List<String> locationToken = splitTag(locationSearchValue);

                if(personToken.size() == 0 && locationToken.size() == 0){
                    showToast("at least type one tag in one field.");
                }else{
                    Album resultAlbum = orSearch(personToken, locationToken);
                    Intent intent = new Intent(SearchByTagActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.KEY_SEARCH_RESULT, resultAlbum);
                    startActivity(intent);
                }
            }
        });
    }

    private void showToast(String text){
        Toast toast=Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
        toast.show();
    }
    private List<String> splitTag(String input){
        List<String> resultTags = new ArrayList<>();

        while(input.contains(",")){
            String token = input.substring(0, input.indexOf(","));

            if(!token.trim().equals(""))
                resultTags.add(token.trim());

            input = input.substring(input.indexOf(",") + 1);
        }

        if(!input.equals("")){
            resultTags.add(input.trim());
        }

        return resultTags;
    }

    private Album andSearch(List<String> personToken, List<String> locationToken){

        Album resultAlbum = new Album("andSearchResult");

        if(albumList == null){
            return resultAlbum;
        }

        for(Album album : albumList){
            for(Photo photo : album.getPhotoList()){
                boolean isTarget = true;

                if((photo.getPerson_tag().size() == 0 && personToken.size() != 0) ||
                        (photo.getLocation_tag().size() == 0 && locationToken.size() != 0))
                    continue;

                //once find one input tag not match or can not completion, set false and break
                for(int personTagIndex = 0; personTagIndex < personToken.size(); ++ personTagIndex){
                    String currInputTag = personToken.get(personTagIndex);

                    for(int photoPersonTagIndex = 0; photoPersonTagIndex < photo.getPerson_tag().size(); ++photoPersonTagIndex){
                        String currPhotoTag = photo.getPerson_tag().get(photoPersonTagIndex);

                        if(!currInputTag.equals(currPhotoTag)){
                            if(currInputTag.length() < currPhotoTag.length()){
                                if(!currPhotoTag.substring(0,currInputTag.length()).equals(currInputTag)){
                                    isTarget = false;
                                }else {
                                    isTarget = true;
                                    break;
                                }
                            }else{
                                isTarget = false;
                            }
                        }else{
                            isTarget = true;
                            break;
                        }
                    }
                }

                for(int locationTagIndex = 0; locationTagIndex < locationToken.size() && isTarget; ++locationTagIndex){
                    String currInputTag = locationToken.get(locationTagIndex);

                    for(int photoLocationTagIndex = 0; photoLocationTagIndex < photo.getLocation_tag().size(); ++photoLocationTagIndex){
                        String currPhotoTag = photo.getLocation_tag().get(photoLocationTagIndex);

                        if(!currInputTag.equals(currPhotoTag)){
                            if(currInputTag.length() < currPhotoTag.length()){
                                if(!currPhotoTag.substring(0,currInputTag.length()).equals(currInputTag)){
                                    isTarget = false;
                                }else {
                                    isTarget = true;
                                    break;
                                }
                            }else{
                                isTarget = false;
                            }
                        }else{
                            isTarget = true;
                            break;
                        }
                    }
                }

                if(isTarget)
                    resultAlbum.addPhotoObject(photo);

            }
        }

        return resultAlbum;
    }

    private Album orSearch(List<String> personToken, List<String> locationToken){
        Album resultAlbum = new Album("orSearchResult");

        if(albumList == null){
            return resultAlbum;
        }

        for(Album album : albumList){
            for(Photo photo : album.getPhotoList()){
                boolean isFound = false;

                a:  for(int personTagIndex = 0; personTagIndex < personToken.size(); ++ personTagIndex){
                        String currInputTag = personToken.get(personTagIndex);

                        for(int photoPersonTagIndex = 0; photoPersonTagIndex < photo.getPerson_tag().size(); ++photoPersonTagIndex){
                            String currPhotoTag = photo.getPerson_tag().get(photoPersonTagIndex);

                            if(currInputTag.equals(currPhotoTag)){
                                resultAlbum.addPhotoObject(photo);
                                isFound = true;
                                break a;
                            }

                            if (currInputTag.length() < currPhotoTag.length()) {
                                if (currPhotoTag.substring(0, currInputTag.length()).equals(currInputTag)) {
                                    resultAlbum.addPhotoObject(photo);
                                    isFound = true;
                                    break a;
                                }
                            }
                        }
                    }

                b: for(int locationTagIndex = 0; locationTagIndex < locationToken.size() && !isFound; ++locationTagIndex){
                        String currInputTag = locationToken.get(locationTagIndex);

                        for(int photoLocationTagIndex = 0; photoLocationTagIndex < photo.getLocation_tag().size(); ++photoLocationTagIndex){
                            String currPhotoTag = photo.getLocation_tag().get(photoLocationTagIndex);

                            if(currInputTag.equals(currPhotoTag)){
                                resultAlbum.addPhotoObject(photo);
                                break b;
                            }

                            if (currInputTag.length() < currPhotoTag.length()) {
                                if (currPhotoTag.substring(0, currInputTag.length()).equals(currInputTag)) {
                                    resultAlbum.addPhotoObject(photo);
                                    break b;
                                }
                            }
                        }
                    }
            }
        }

        return resultAlbum;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
