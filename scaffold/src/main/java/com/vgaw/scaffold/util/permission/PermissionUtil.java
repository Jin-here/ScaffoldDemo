package com.vgaw.scaffold.util.permission;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by caojin on 2017/11/15.
 */

public class PermissionUtil {
    public static void requestPermission(Activity activity, ArrayList<String> permissionList, int requestCode) {
        ArrayList<String> resultList = new ArrayList<>();
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                resultList.add(permission);
            }
        }

        String[] permissionArray = new String[resultList.size()];
        resultList.toArray(permissionArray);
        ActivityCompat.requestPermissions(activity,
                permissionArray, requestCode);
    }

    public static void requestPermission(Activity activity, ArrayList<String> permissionList) {
        requestPermission(activity, permissionList, 1);
    }
}
