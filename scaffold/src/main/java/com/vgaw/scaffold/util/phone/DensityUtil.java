package com.vgaw.scaffold.util.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public final class DensityUtil {
    private static float sDensity = -1f;
    private static float sScaledDensity = -1f;

    private DensityUtil() {}

    public static float getDensity(Context context) {
        if (sDensity == -1) {
            sDensity = context.getResources().getDisplayMetrics().density;
        }
        return sDensity;
    }

    public static float getScaledDensity(Context context) {
        if (sScaledDensity == -1) {
            sScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        }
        return sScaledDensity;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) (spValue * getScaledDensity(context) + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / getScaledDensity(context) + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        Resources res = context.getResources();
        if (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return res.getDisplayMetrics().widthPixels;
        }
        return res.getDisplayMetrics().widthPixels + getNavigationBarHeight(context);
    }

    /**
     * @param context
     * @param size 如果长度为1，则size[0]为高度，如果长度大于1，则size[1]为宽度
     */
    public static void getScreenHeight(Context context, @NonNull int[] size) {
        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {}
        } else if (Build.VERSION.SDK_INT >= 17) {
            // includes window decorations (statusbar bar/menu bar)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {}
        }
        int statusBarHeight = getStatusBarHeight(context);
        int navigationHeight = getNavigationBarHeight(context);
        size[0] = heightPixels - statusBarHeight - navigationHeight;
        if (size.length > 1) {
            size[1] = widthPixels;
        }
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        if (checkDeviceHasNavigationBar(context)) {
            //如果小米手机开启了全面屏手势隐藏了导航栏则返回 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0) {
                    return 0;
                }
            }
            int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
            if (rid != 0) {
                int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        // 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }
}
