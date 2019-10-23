package com.vgaw.scaffolddemo.data.callback;

public interface ObjectCallback<T> {
    void onSuccess(T data);

    void onFailed(int code, String error);
}
