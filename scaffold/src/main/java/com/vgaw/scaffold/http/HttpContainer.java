package com.vgaw.scaffold.http;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;

public class HttpContainer {
    private Set<Call> mCallList = new HashSet<>();

    public <T> void enqueueCall(Call<T> call, Callback<T> callback) {
        mCallList.add(call);
        call.enqueue(callback);
    }

    public void cancelAllCall() {
        for (Call call : mCallList) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }
}
