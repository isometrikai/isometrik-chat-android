package io.isometrik.chat.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.isometrik.ui.messages.chat.common.ChatConfig

object Utilities {
    fun isGivenPermissionsGranted(context: Context, permissionList: ArrayList<String>): Boolean {
        val allGranted = true
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return allGranted
    }

    @JvmStatic
    fun getPermissionsListForExternalStorage(isForRead: Boolean): ArrayList<String> {
        val permissionsRequired = ArrayList<String>()
        if (Build.VERSION.SDK_INT < 33) {
            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (isForRead) {
                permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            permissionsRequired.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissionsRequired.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissionsRequired.add(Manifest.permission.READ_MEDIA_AUDIO)
        }

        return permissionsRequired
    }

    @JvmStatic
    fun checkSelfExternalStoragePermissionIsGranted(context: Context, isForRead: Boolean): Boolean {
        return if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @JvmStatic
    fun shouldShowExternalPermissionStorageRational(
        activity: Activity,
        isForRead: Boolean
    ): Boolean {
        return if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        } else {
            (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_MEDIA_IMAGES
            )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_MEDIA_VIDEO
            )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.READ_MEDIA_AUDIO
            ))
        }
    }

    fun requestExternalStoragePermission(activity: Activity, requestCode: Int, isForRead: Boolean) {
        if (Build.VERSION.SDK_INT < 33) {
            if (isForRead) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode
                )
            }
        } else {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                ),
                requestCode
            )
        }
    }

    fun isAllPermissionGranted(grantResults: IntArray): Boolean {
        var allPermissionsGranted = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false
                break
            }
        }
        return allPermissionsGranted
    }

    //permission array for storage in android build below 13
    var storagePermissionsBelow33: Array<String> = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    //permission array for storage in android build above 12
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var storagePermissions33: Array<String> = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    //to get array of storage pemission
    fun getStoragePermissions(): Array<String> {
        return if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) storagePermissions33 else storagePermissionsBelow33
    }

    @JvmStatic
    fun showToast(toastMsg : String) = !ChatConfig.dontShowToastList.contains(toastMsg)

}
