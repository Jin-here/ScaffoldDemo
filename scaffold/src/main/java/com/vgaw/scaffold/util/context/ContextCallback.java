package com.vgaw.scaffold.util.context;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author caojin
 * @date 2018/6/9
 */
public class ContextCallback implements Application.ActivityLifecycleCallbacks {
    public AtomicInteger sCount = new AtomicInteger(0);

    public boolean inBackground() {
        return sCount.get() == 0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ContextManager.getInstance().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        int before = sCount.get();
        int now = sCount.incrementAndGet();
        onValueChanged(before, now);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ContextManager.getInstance().setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {
        int before = sCount.get();
        int now = sCount.decrementAndGet();
        onValueChanged(before, now);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        ContextManager.getInstance().removeActivity(activity);
    }

    private void onValueChanged(int before, int now) {
        if (before < 1 && now > 0) {
            InBackgroundObserver.getInstance().notify(false);
            return;
        }
        if (before > 0 && now < 1) {
            InBackgroundObserver.getInstance().notify(true);
        }
    }
}
