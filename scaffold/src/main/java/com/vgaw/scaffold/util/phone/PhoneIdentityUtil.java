package com.vgaw.scaffold.util.phone;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.Random;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by caojin on 2017/6/16.
 */

public class PhoneIdentityUtil {
    public static String getIMEI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String deviceId = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE))
                .getDeviceId();
        if (deviceId == null) {
            return getDeviceUUID(context);
        }
        return deviceId;
    }

    private static String getDeviceUUID(Context context) {
        String uuid = loadDeviceUUID(context);
        if (uuid == null) {
            uuid = buildDeviceUUID(context);
            saveDeviceUUID(context, uuid);
        }
        return uuid;
    }

    private static String buildDeviceUUID(Context context) {
        String androidId = getAndroidId(context);
        if ("9774d56d682e549c".equals(androidId)) {
            Random random = new Random();
            androidId = Integer.toHexString(random.nextInt())
                    + Integer.toHexString(random.nextInt())
                    + Integer.toHexString(random.nextInt());
        }
        return new UUID(androidId.hashCode(), getBuildInfo().hashCode()).toString();
    }

    private static void saveDeviceUUID(Context context, String uuid) {
        context.getSharedPreferences("device_uuid", Context.MODE_PRIVATE)
                .edit()
                .putString("uuid", uuid)
                .apply();
    }

    private static String loadDeviceUUID(Context context) {
        return context.getSharedPreferences("device_uuid", Context.MODE_PRIVATE)
                .getString("uuid", null);
    }

    private static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static String getBuildInfo() {
        //这里选用了几个不会随系统更新而改变的值
        StringBuffer buildSB = new StringBuffer();
        buildSB.append(Build.BRAND).append("/");
        buildSB.append(Build.PRODUCT).append("/");
        buildSB.append(Build.DEVICE).append("/");
        buildSB.append(Build.ID).append("/");
        buildSB.append(Build.VERSION.INCREMENTAL);
        return buildSB.toString();
    }
}
