package com.vgaw.scaffold.util.img;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.WorkerThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShotUtil {
    /**
     * 适用于view已渲染
     *
     * @param view
     * @return
     */
    public static Bitmap createBitmap(View view) {
        //启用绘图缓存
        view.setDrawingCacheEnabled(true);
        //强制构建绘图缓存（防止上面启用绘图缓存的操作失败）
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public static void refreshBitmap(View view) {
        view.setDrawingCacheEnabled(false);
    }

    /**
     * 适用于scrollerview，即部分内容超出屏幕（兼容{@link #createBitmap(View)}）
     *
     * @param v
     * @return
     */
    public static Bitmap createBitmap2(View v) {
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        v.draw(c);
        return bmp;
    }

    /**
     * 适用于view没有渲染
     *
     * @param v
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap3(View v, int width, int height) {
        // 测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        // 调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        v.draw(c);
        return bmp;
    }

    @WorkerThread
    public static void saveBitmap(Bitmap bitmap, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Bitmap drawBg4Bitmap(int color, Bitmap originBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight(), originBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, originBitmap.getWidth(), originBitmap.getHeight(), paint);
        canvas.drawBitmap(originBitmap, 0, 0, paint);

        originBitmap.recycle();
        return bitmap;
    }
}
