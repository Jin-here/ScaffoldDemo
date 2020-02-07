package com.vgaw.scaffold.util;

import android.media.MediaPlayer;

import java.io.IOException;

public class MediaUtil {
    public static int getMediaLength(String audioUrl){
        int duration = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mediaPlayer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duration/1000;
    }
}
