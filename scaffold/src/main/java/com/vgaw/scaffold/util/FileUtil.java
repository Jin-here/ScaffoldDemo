package com.vgaw.scaffold.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static boolean hasExtentsion(String filename) {
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return true;
        } else {
            return false;
        }
    }

    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * must start with "/"
     * @param path
     * @return
     */
    public static String buildAssetPath(String path) {
        return String.format("file:///android_asset%s", path);
    }

    public static boolean saveBitmap2File(File dir, String fileName, Bitmap bmp) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //获取文件
        File file = new File(dir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            /*ImageActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));*/
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getPath(Context context, Uri uri) {
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(context, uri)) {
            String wholeId = DocumentsContract.getDocumentId(uri);
            if (isExternalStorageDocument(uri)) {
                String[] split = wholeId.split(":");
                String type = split[0];
                if ("primary".equals(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    // Environment.getExternalStorageDirectory(): /storage/emulated/0
                }
                // TODO: 2015-12-22 handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // content://com.android.providers.downloads.documents/document/1
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(wholeId));
                path = getPath(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // content://com.android.providers.media.documents/document/image%3A11
                String[] split = wholeId.split(":");
                String id = split[1];
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                path = getPath(context, contentUri,
                        MediaStore.Images.Media._ID + "=?",
                        new String[]{id});
            }
        } else if ("content".equals(uri.getScheme())) {
            // action_pick && api<19
            // content://media/external/images/media/12
            if (isGooglePhotosUri(uri)) {
                path = uri.getLastPathSegment();
            }
            path = getPath(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // action_pick && api>=19
            // file:///storage/emulated/0/...
            path = uri.getPath();
        }
        return path;
    }

    private static String getPath(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        Cursor cursor = null;
        try{
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA},
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        }finally{
        }
        return path;
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}