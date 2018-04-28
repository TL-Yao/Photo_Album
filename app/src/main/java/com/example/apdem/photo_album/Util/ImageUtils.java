package com.example.apdem.photo_album.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.IOException;

public class ImageUtils {
    public static void loadImage(@NonNull Uri uri,
                                 @NonNull ImageView imageView) {

            System.out.println("in ImageUtils" + uri);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            imageView.setImageURI(uri);

    }
}
