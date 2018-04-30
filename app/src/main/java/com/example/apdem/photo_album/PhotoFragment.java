package com.example.apdem.photo_album;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.apdem.photo_album.Util.ImageUtils;

public class PhotoFragment extends Fragment {
    public static final String KEY_URI_FRAG = "uri_frag";

    private ImageView imageView;
    private View view;

    @NonNull
    public static PhotoFragment newInstance (Uri uri){
        Bundle args = new Bundle();
        args.putString(KEY_URI_FRAG, uri.toString());

        PhotoFragment pictureFragment = new PhotoFragment();
        pictureFragment.setArguments(args);
        return  pictureFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.photo_fragment,container,false);
            imageView = (ImageView) view.findViewById(R.id.show_photo);
            return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String stringUri =getArguments().getString(KEY_URI_FRAG);
        Uri uri = Uri.parse(stringUri);
        ImageUtils.loadImage(uri, imageView);
    }

    @Nullable
    @Override
    public View getView() {
        return this.view;
    }

}
