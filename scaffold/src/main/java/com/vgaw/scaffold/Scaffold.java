package com.vgaw.scaffold;

import android.app.Application;

import com.tencent.mmkv.MMKV;
import com.vgaw.scaffold.util.context.ContextManager;

import timber.log.Timber;

public class Scaffold {
    public static void init(Application context, boolean debug) {
        ContextManager.getInstance().init(context);
        if (debug) {
            Timber.plant(new Timber.DebugTree());
        }
        MMKV.initialize(context);
    }
}
