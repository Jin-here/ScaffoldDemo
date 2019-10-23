package com.vgaw.scaffolddemo.data.callback;

import java.util.List;

public interface ListCallback<T> {
    void onSuccess(List<T> data);

    void onFailed(int code, String error);
}
