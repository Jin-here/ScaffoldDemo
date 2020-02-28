package com.vgaw.scaffolddemo.http.callback;

import android.os.AsyncTask;

import com.vgaw.scaffolddemo.http.ErrorMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by caojin on 2017/3/9.
 */

public abstract class HttpBlobResultCallback implements Callback<ResponseBody>, HttpResultInterface<Void> {
    private File mFile;

    public HttpBlobResultCallback(File file) {
        this.mFile = file;
    }

    public HttpBlobResultCallback(File dir, String fileName) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.mFile = new File(dir, fileName);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            new AsyncTask<Void, Integer, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        save2File(response, mFile);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        onSuccess(null);
                    } else {
                        onFail(ErrorMap.getUnknownMsg());
                    }
                    onFinished();
                }
            }.execute();
        } else {
            onFail(ErrorMap.getErrorMsg(response.code()));
            onFinished();
        }
    }

    private static void save2File(Response<ResponseBody> response, File file) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = response.body().byteStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
