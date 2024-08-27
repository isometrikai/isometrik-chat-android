package io.isometrik.chat.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Utilities {

    public static boolean isGivenPermissionsGranted(Context context, ArrayList<String> permissionList) {
        boolean allGranted = true;
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return allGranted;
    }
    public static ArrayList<String> getPermissionsListForExternalStorage(boolean isForRead) {
        ArrayList<String> permissionsRequired = new ArrayList<>();
        if (Build.VERSION.SDK_INT < 33) {
            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(isForRead){
                permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            permissionsRequired.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissionsRequired.add(Manifest.permission.READ_MEDIA_VIDEO);
            permissionsRequired.add(Manifest.permission.READ_MEDIA_AUDIO);
        }

        return permissionsRequired;
    }

    public static boolean checkSelfExternalStoragePermissionIsGranted(Context context, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            } else {
                return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }

    }

    public static boolean shouldShowExternalPermissionStorageRational(Activity activity, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            } else {
                return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        } else {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_IMAGES)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_VIDEO)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_MEDIA_AUDIO);
        }
    }

    public static void requestExternalStoragePermission(Activity activity, int requestCode, boolean isForRead) {

        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            } else {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCode);
            }

        } else {
            activity.requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO},
                    requestCode);
        }

    }

    public static boolean isAllPermissionGranted(int[] grantResults) {

        boolean allPermissionsGranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        return allPermissionsGranted;
    }
}
