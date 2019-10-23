package com.vgaw.scaffold.util.img;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

public class ImageUtil {
    public static boolean isGif(String extension) {
        return !TextUtils.isEmpty(extension) && extension.toLowerCase().equals("gif");
    }

    /**
     * 将图片添加到画廊
     */
    public static void addToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(mediaScanIntent);
    }
}
