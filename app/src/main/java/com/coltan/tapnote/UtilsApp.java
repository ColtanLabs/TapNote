package com.coltan.tapnote;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class UtilsApp {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_READ = 1;

    public static Boolean checkPermissions(Activity activity) {
        Boolean res = false;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_READ);
        } else {
            res = true;
        }

        return res;
    }
}
