package com.vgaw.scaffold.img;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by caojin on 2017/9/14.
 */

public class ImageLoader {
    public static void load(Context context, String url, ImageView target) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context).load(url)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target) {
        if (!activity.isDestroyed()) {
            GlideApp.with(activity).load(url)
                    .into(target);
        }
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity).load(url)
                    .into(target);
        }
    }

    public static void load(Fragment fragment, String url, ImageView target) {
        GlideApp.with(fragment).load(url)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target) {
        GlideApp.with(fragment).load(url)
                .into(target);
    }

    public static void load(Context context, String url, ImageView target, int resourceId) {
        GlideApp.with(context).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target, int resourceId) {
        GlideApp.with(activity).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target, int resourceId) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity).load(url)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(Fragment fragment, String url, ImageView target, int resourceId) {
        GlideApp.with(fragment).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target, int resourceId) {
        GlideApp.with(fragment).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Context context, String url, ImageView target, Drawable resourceId) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target, Drawable resourceId) {
        if (!activity.isDestroyed()) {
            GlideApp.with(activity).load(url)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target, Drawable resourceId) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity).load(url)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(Fragment fragment, String url, ImageView target, Drawable resourceId) {
        GlideApp.with(fragment).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target, Drawable resourceId) {
        GlideApp.with(fragment).load(url)
                .placeholder(resourceId)
                .into(target);
    }

    /**
     * clear Disk Cache
     * @param applicationContext
     */
    public static void clearDiskCache(final Context applicationContext) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // This method must be called on a background thread.
                Glide.get(applicationContext).clearDiskCache();
                return null;
            }
        }.execute();
    }

    public static long getDiskCacheSize(Context applicationContext) {
        File cacheDir = GlideApp.getPhotoCacheDir(applicationContext);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            return getFolderSize(cacheDir);
        }
        return -1;
    }

    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
