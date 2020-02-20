package com.vgaw.scaffold.util.context;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by caojin on 2016/10/24.
 */

public class ContextManager {
    private static volatile ContextManager sInstance;

    private Context mContext;
    private Handler mHandler;
    private WeakReference<Activity> mCurrentActivityWeakRef;
    private List<Activity> mActivityList = new ArrayList<>();
    private ContextCallback mContextCallback;

    private ContextManager() {}

    public static ContextManager getInstance() {
        if (sInstance == null) {
            synchronized (ContextManager.class) {
                if (sInstance == null) {
                    sInstance = new ContextManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Application context) {
        mContextCallback = new ContextCallback();
        context.registerActivityLifecycleCallbacks(mContextCallback);
        mContext = context;
        mHandler = new Handler();
    }

    public Context getApplicationContext() {
        return mContext;
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    public FragmentManager getFragmentManager() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null && currentActivity instanceof FragmentActivity) {
            return ((FragmentActivity) currentActivity).getSupportFragmentManager();
        }
        return null;
    }

    public Activity getActivity(Class cls) {
        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (next.getClass() == cls) {
                return next;
            }
        }
        return null;
    }

    public Activity getCurrentActivity() {
        if (mCurrentActivityWeakRef != null) {
            return mCurrentActivityWeakRef.get();
        }
        return null;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public void removeRemainingActivity(Class me) {
        for (Activity activity : mActivityList) {
            if (activity.getClass() != me) {
                activity.finish();
            }
        }
    }

    public void removeRemainingActivity(Activity me) {
        for (Activity activity : mActivityList) {
            if (activity != me) {
                activity.finish();
            }
        }
    }

    public void removeAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    public void release() {
        mActivityList.clear();
        mCurrentActivityWeakRef = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public AssetManager getAsset() {
        return getApplicationContext().getAssets();
    }

    public int getDimen(@DimenRes int id) {
        return getApplicationContext().getResources().getDimensionPixelSize(id);
    }

    public int getColor(@ColorRes int id) {
        return getApplicationContext().getResources().getColor(id);
    }

    public String getString(@StringRes int id) {
        return getApplicationContext().getResources().getString(id);
    }

    public Drawable getDrawable(@DrawableRes int id) {
        return getApplicationContext().getResources().getDrawable(id);
    }

    public String[] getStringArray(@ArrayRes int id) {
        return getApplicationContext().getResources().getStringArray(id);
    }

    public boolean inBackground(){
        return mContextCallback.inBackground();
    }
}

