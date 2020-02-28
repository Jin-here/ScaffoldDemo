package com.vgaw.scaffolddemo.http;

import android.util.SparseArray;

import com.vgaw.scaffold.util.context.ContextManager;
import com.vgaw.scaffolddemo.R;

public class ErrorMap {
    private static final int ERROR_UNKNOWN = -1;

    private static SparseArray<String> sMap = new SparseArray<>();

    static {
        // TODO: 2020-02-05 replace with your error code
        sMap.put(ERROR_UNKNOWN, ContextManager.getInstance().getString(R.string.base_unknown));
    }

    public static String getUnknownMsg() {
        return getErrorMsg(ERROR_UNKNOWN);
    }

    public static String getErrorMsg(int code) {
        return sMap.get(code);
    }
}