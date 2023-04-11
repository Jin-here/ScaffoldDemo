package com.vgaw.scaffold.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.vgaw.scaffold.util.context.ContextManager;

/**
 * Created by caojin on 2017/9/16.
 */

public class ScaffoldToast {
    private static final long TOO_QUICK_TIMEOUT = 2000;
    private static String sLstMsg;
    private static long sLstTime;

    private static Toast sToast;
    private static TextView sContentView;

    /**
     * 自定义toast样式
     * @param viewId
     * @param contentId
     */
    public static void setToastView(@NonNull @LayoutRes int viewId, @IdRes int contentId) {
        Context context = ContextManager.getInstance().getApplicationContext();
        sToast = new Toast(context);
        View view = View.inflate(context, viewId, null);
        sToast.setView(view);
        sContentView = view.findViewById(contentId);
    }

    public static void show(@Nullable String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(sLstMsg)) {
                long crtTime = System.currentTimeMillis();
                if (crtTime - sLstTime > TOO_QUICK_TIMEOUT) {
                    sLstTime = crtTime;
                } else {
                    return;
                }
            } else {
                sLstMsg = msg;
            }
            Handler handler = ContextManager.getInstance().getHandler();
            if (handler != null) {
                handler.post(() -> {
                    if (sContentView == null) {
                        Toast.makeText(ContextManager.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } else {
                        sContentView.setText(msg);
                        sToast.show();
                    }
                });
            }
        }
    }

    public static void show(@StringRes int resId) {
        show(ContextManager.getInstance().getString(resId));
    }
}
