package com.example.apdem.photo_album.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PermissionUtils {

    public static final int REQ_CODE_READ_EXTERNAL_STORAGE = 200;
    public static final int REQ_CODE_WRITE_EXTERNAL_STORAGE = 201;


    public static boolean checkPermission(@NonNull Context context,
                                          @NonNull String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(@NonNull Activity activity,
                                          @NonNull String[] permissions,
                                          int reqCode) {
        ActivityCompat.requestPermissions(activity, permissions, reqCode);
    }

    public static void requestReadExternalStoragePermission(@NonNull Activity activity) {
        requestPermissions(activity,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                REQ_CODE_READ_EXTERNAL_STORAGE);
    }

    public static void requestWriteExternalStoragePermission(@NonNull Activity activity) {
        requestPermissions(activity,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQ_CODE_WRITE_EXTERNAL_STORAGE);
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
