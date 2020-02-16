package com.vgaw.scaffold.util.context;

import android.content.Context;
import android.os.Environment;

import com.vgaw.scaffold.R;

import java.io.File;

/**
 * Created by caojin on 2016/9/12.
 */
public class FileManager {
    private static final String DIR_NAME = ContextManager.getInstance().getApplicationContext().getString(R.string.app_name);
    private static final String DIR_DEBUG = "debug";
    private static final String DIR_BUG = "bug";
    private static final String DIR_MOVIE = "movie";
    private static final String DIR_PICTURE = "picture";
    private static final String DIR_SCREENSHOT = "screenshot";
    private static final String DIR_APK = "apk";
    private static final String DIR_TEMP = "temp";

    /**
     * 根目录
     * @return
     */
    public static File getDir(){
        return Environment.getExternalStoragePublicDirectory(DIR_NAME);
    }

    public static File getInternalDir(Context context) {
        return context.getFilesDir();
    }

    public static File getTempDir(Context context) {
        File dir = new File(getInternalDir(context).getAbsolutePath() + File.separator + DIR_TEMP);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static File getBugDir(Context context) {
        File dir = new File(getDir().getAbsolutePath() + File.separator + DIR_BUG);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static File getDebugDir() {
        File dir = new File(getDir().getAbsolutePath() + File.separator + DIR_DEBUG);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 视频存放目录
     * @return
     */
    public static File getMovieDir(){
        File file = new File(getDir().getAbsolutePath() + File.separator + DIR_MOVIE);
        if (!file.exists()){
            file.mkdirs();
        }
        return format(file);
    }

    /**
     * 图片存放目录
     * @return
     */
    public static File getPictureDir(){
        File file = new File(getDir().getAbsolutePath() + File.separator + DIR_PICTURE);
        if (!file.exists()){
            file.mkdirs();
        }
        return format(file);
    }

    public static File getScreenshotDir(){
        File file = new File(getDir().getAbsolutePath() + File.separator + DIR_SCREENSHOT);
        if (!file.exists()){
            file.mkdirs();
        }
        return format(file);
    }

    /**
     * APK存放
     * @param versionName
     * @return
     */
    public static File getAPKFile(Context context, String versionName){
        File file = new File(getInternalDir(context).getAbsolutePath() + File.separator + DIR_APK);
        if (!file.exists()){
            file.mkdirs();
        }
        return new File(file, getAPKFileName(context, versionName));
    }

    /**
     * 清除APK文件及文件夹
     */
    public static void clearAPKDir(){
        File file = new File(getDir().getAbsolutePath() + File.separator + DIR_APK);
        if (file.exists()){
            File[] apks = file.listFiles();
            for (File apk : apks){
                apk.delete();
            }
            file.delete();
        }
    }

    public static void deleteAPK(Context context, String versionName){
        File file = new File(getInternalDir(context).getAbsolutePath() + File.separator + DIR_APK);
        if (file.exists()){
            File apk = new File(file, getAPKFileName(context, versionName));
            if (apk.exists()){
                apk.delete();
            }
        }
    }

    private static String getAPKFileName(Context context, String versionName){
        String name = context.getString(R.string.app_name_official);
        return String.format("%s_%s.apk", name, versionName);
    }

    /**
     * 形如：类别/年月日/
     * @return
     */
    private static File format(File dir) {
        /*String currentTime = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        File temp = new File(dir + File.separator+ currentTime);
        if (!temp.exists() || !temp.isDirectory()){
            temp.mkdirs();
        }
        return temp;*/
        return dir;
    }
}
