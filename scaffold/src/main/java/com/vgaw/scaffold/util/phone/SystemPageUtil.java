package com.vgaw.scaffold.util.phone;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created by caojin on 2017/6/23.
 */

public class SystemPageUtil {
    private static final String HUAWEI = "Huawei";
    private static final String MEIZU = "Meizu";
    private static final String XIAOMI = "Xiaomi";
    private static final String SONY = "Sony";
    private static final String OPPO = "OPPO";
    private static final String LG = "LG";
    private static final String VIVO = "vivo";
    private static final String SAMSUNG = "samsung";
    private static final String LETV = "Letv";
    private static final String ZTE = "ZTE";
    private static final String YULONG = "YuLong";
    private static final String LENOVO = "LENOVO";

    public static void jump2PermissoinPage(Activity activity) {
        try {
            Intent intent = new Intent();
            ComponentName comp = null;
            switch (Build.MANUFACTURER) {
                case HUAWEI:
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("packageName", activity.getPackageName());
                    comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
                    intent.setComponent(comp);
                    break;
                case MEIZU:
                    intent.setAction("com.meizu.safe.security.SHOW_APPSEC");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra("packageName", activity.getPackageName());
                    break;
                case XIAOMI:
                    MIUIUtil.applyMiuiPermission(activity);
                    return;
                case SONY:
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("packageName", activity.getPackageName());
                    comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
                    intent.setComponent(comp);
                    break;
                case OPPO:
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("packageName", activity.getPackageName());
                    comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
                    intent.setComponent(comp);
                    break;
                case LG:
                    intent.setAction("android.intent.action.MAIN");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("packageName", activity.getPackageName());
                    comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                    intent.setComponent(comp);
                    break;
                case LETV:
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("packageName", activity.getPackageName());
                    comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
                    intent.setComponent(comp);
                    break;
                case VIVO:
                case SAMSUNG:
                case ZTE:
                case YULONG:
                case LENOVO:
                default:
                    jumpToSettingPage(activity);
                    return;
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            jumpToSettingPage(activity);
        }
    }

    public static void jumpToSettingPage(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(intent);
        } catch (Exception e) {}
    }
}
