package com.vgaw.scaffold.page;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vgaw.scaffold.util.phone.ImmerseUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;

public class ScaffoldAc extends AppCompatActivity {
    protected boolean mImmerse;
    protected boolean mKeepScreenOn;
    protected boolean mDarkMode;

    protected void enableImmerse() {
        mImmerse = true;
    }

    protected void keepScreenOn() {
        mKeepScreenOn = true;
    }

    protected AppCompatActivity getSelf() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (mKeepScreenOn) {
            // 保持屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (mImmerse) {
            // 开启沉浸式
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == 0) {
                        Handler handler = new Handler();
                        handler.removeCallbacks(mEnterImmerseModeRunnable);
                        handler.postDelayed(mEnterImmerseModeRunnable, 2000);
                    }
                }
            });
            ImmerseUtil.startImmerse(this);
        }
        super.onCreate(savedInstanceState);
        if (mDarkMode) {
            StatusBarUtil.setDarkMode(this);
        } else {
            StatusBarUtil.setLightMode(this);
        }
    }

    @Override
    protected void onDestroy() {
        // 关闭屏幕常亮
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onDestroy();
    }

    private Runnable mEnterImmerseModeRunnable = new Runnable() {
        @Override
        public void run() {
            ImmerseUtil.startImmerse(getSelf());
        }
    };
}
