package com.vgaw.scaffold.view;

import android.widget.Toast;

import androidx.annotation.StringRes;

import com.vgaw.scaffold.util.context.ContextManager;

/**
 * Created by caojin on 2017/9/16.
 */

public class AppToast {
    private static final long SHORT_DURATION_TIMEOUT = 4000;
    private static String sLstMsg;
    private static long sLstTime;

    public static void show(String msg) {
        if (msg != null) {
            if (msg.equals(sLstMsg)) {
                long crtTime = System.currentTimeMillis();
                if (crtTime - sLstTime > SHORT_DURATION_TIMEOUT) {
                    sLstTime = crtTime;
                } else {
                    return;
                }
            } else {
                sLstMsg = msg;
            }
            Toast.makeText(ContextManager.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(@StringRes int resId) {
        show(ContextManager.getInstance().getString(resId));
    }
}
