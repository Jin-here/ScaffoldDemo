package com.vgaw.scaffold.page;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.util.net.Network;
import com.vgaw.scaffold.util.net.NetworkChange;
import com.vgaw.scaffold.util.net.NetworkWatcher;
import com.vgaw.scaffold.util.phone.ImmerseUtil;

import java.util.Observable;

public class BaseAc extends AppCompatActivity {
    private boolean mImmerse;
    private boolean mKeepScreenOn;

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
        StatusBarUtil.setLightMode(this);

        NetworkChange.getInstance().addObserver(mNetworkWatcher);
    }

    @Override
    protected void onDestroy() {
        NetworkChange.getInstance().deleteObserver(mNetworkWatcher);
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

    private NetworkWatcher mNetworkWatcher = new NetworkWatcher() {
        @Override
        public void update(Observable observable, Object data) {
            super.update(observable, data);
            Network network = (Network) data;
            if (network.isConnected()) {
                // connected
                network.setConnected(true);

            } else {
                // disconnected
                network.setConnected(false);
            }
        }
    };
}
