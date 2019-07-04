package com.tunity.ekaterinatemnogrudova.tunity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by lirons on 12/02/2018.
 */


public class PermissionManager {

    private Activity mContext;


    public PermissionManager(Activity context) {
        mContext = context;
    }

    // the activity that call this method need to implement onRequestPermissionsResult callBack

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(mContext,
                 new String[]{Manifest.permission.CAMERA}, 50);
    }

    // use this function when try to get user contacts list
    // before perform action check user has permission
    // if permission not granted call requestContactsPermission method

    public boolean isUserHasCameraPermission() {
        return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED);
    }

}
