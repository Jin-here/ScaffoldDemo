package com.vgaw.scaffold.util.context;

import com.vgaw.scaffold.util.TimeUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWritter {
    private static LogWritter sInstance;

    private File mFile;
    private FileWriter mWriter;

    private LogWritter() {}

    public static LogWritter getInstance() {
        if (sInstance == null) {
            synchronized (LogWritter.class) {
                if (sInstance == null) {
                    sInstance = new LogWritter();
                }
            }
        }
        return sInstance;
    }

    public void write(String msg) {
        if (mFile == null) {
            mFile = new File(FileManager.getDebugDir(), String.valueOf(System.currentTimeMillis()));
            try {
                mWriter = new FileWriter(mFile);
            } catch (IOException e) {}
        }
        if (mWriter != null) {
            try {
                mWriter.write(String.format("%s: %s\n", TimeUtil.formatMillisecond("HH:mm:ss", System.currentTimeMillis()), msg));
            } catch (IOException e) {}
        }
    }

    public void release() {
        if (mWriter != null) {
            try {
                mWriter.close();
            } catch (IOException e) {}
        }
        mFile = null;
    }
}
