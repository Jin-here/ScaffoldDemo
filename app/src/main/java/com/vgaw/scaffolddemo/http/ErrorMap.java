package com.vgaw.scaffolddemo.http;

import android.util.SparseArray;

public class ErrorMap {
    private static SparseArray<String> sMap = new SparseArray<>();

    static {
        // TODO: 2020-02-05 replace with your error code
    }

    public static String getErrorMsg(int code) {
        return sMap.get(code);
    }
}