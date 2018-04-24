package com.example.apdem.photo_album;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        setupAlbums();
    }

    private void setupAlbums(){
        LinearLayout albums_left = (LinearLayout) findViewById(R.id.albums_left);
        LinearLayout albums_right = (LinearLayout) findViewById(R.id.albums_right);

        for (int i = 5; i > 0; --i){
            albums_left.addView(getEducationView());
        }

        for (int i = 5; i > 0; --i){
            albums_right.addView(getEducationView());
        }
    }

    private View getEducationView() {
        final ViewGroup nullParent = null;
        View view = getLayoutInflater().inflate(R.layout.album, nullParent);
        String name = "fuck this projectabcdefghijklmnopqrst";
        ((TextView) view.findViewById(R.id.album_name)).setText(name);
        ((TextView) view.findViewById(R.id.num_photo)).setText("12345");
        return view;
    }
}
