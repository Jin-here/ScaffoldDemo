package com.vgaw.scaffold.img;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * Created by caojin on 2017/9/14.
 * todo cancel request
 */

public class ImgLoader {
    public static void load(Context context, String url, ImageView target) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target) {
        if (!activity.isDestroyed()) {
            GlideApp.with(activity)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(target);
        }
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(target);
        }
    }

    public static void load(Fragment fragment, String url, ImageView target) {
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target) {
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(target);
    }

    public static void load(Context context, String url, ImageView target, int resourceId) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target, int resourceId) {
        GlideApp.with(activity)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target, int resourceId) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(Context context,String picPath,ImageView imageView,final ImgLoadListener listener){
        RequestOptions options = new RequestOptions()
                .error(0)
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .disallowHardwareConfig();
        Glide.with(context)
                .asDrawable()
                .apply(options)
                .load(picPath)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (listener != null){
                            listener.onLoadSuccess();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadGif(Context context, String url, ImageView target, int resourceId) {
        loadGif(context, url, target, resourceId, null);
    }

    public static void loadGif(Context context, String url, ImageView target, int resourceId, Animatable2Compat.AnimationCallback callback) {
        loadGif(context, url, target, resourceId, 1, callback);
    }

    public static void loadGif(Context context, String url, ImageView target, int resourceId, int loopCount, Animatable2Compat.AnimationCallback callback) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context)
                .asGif()
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(loopCount);
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationStart(Drawable drawable) {
                                super.onAnimationStart(drawable);
                                if (callback != null) {
                                    callback.onAnimationStart(drawable);
                                }
                            }

                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                super.onAnimationEnd(drawable);
                                resource.unregisterAnimationCallback(this);
                                if (callback != null) {
                                    callback.onAnimationEnd(drawable);
                                }
                            }
                        });
                        return false;
                    }
                })
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void loadGifForever(Context context, String url, ImageView target, int resourceId) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context)
                .asGif()
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(GifDrawable.LOOP_FOREVER);
                        return false;
                    }
                })
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Fragment fragment, String url, ImageView target, int resourceId) {
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target, int resourceId) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null || activity.isDestroyed()) {
            return;
        }
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Context context, String url, ImageView target, Drawable resourceId) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(Activity activity, String url, ImageView target, Drawable resourceId) {
        if (!activity.isDestroyed()) {
            GlideApp.with(activity)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(Context context, String url, ImageView target,
                            int placeholderId, int errorId) {
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        GlideApp.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(placeholderId)
                .error(errorId)
                .into(target);
    }

    public static void load(FragmentActivity fragmentActivity, String url, ImageView target, Drawable resourceId) {
        if (!fragmentActivity.isDestroyed()) {
            GlideApp.with(fragmentActivity)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(resourceId)
                    .into(target);
        }
    }

    public static void load(Fragment fragment, String url, ImageView target, Drawable resourceId) {
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    public static void load(androidx.fragment.app.Fragment fragment, String url, ImageView target, Drawable resourceId) {
        GlideApp.with(fragment)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(resourceId)
                .into(target);
    }

    /**
     * clear Disk Cache
     *
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
        File cacheDir = new File(applicationContext.getExternalCacheDir() + File.separator + "glide");
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
