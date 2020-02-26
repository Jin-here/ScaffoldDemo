package com.vgaw.scaffolddemo.http.rx;

import android.os.AsyncTask;

import com.vgaw.scaffolddemo.http.ErrorMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by caojin on 2017/3/9.
 * todo need to optimize
 */

public abstract class HttpBlobResultCallback extends HttpResultObserver<ResponseBody> {
    private File file;

    public HttpBlobResultCallback(File file) {
        this.file = file;
    }

    public HttpBlobResultCallback(File dir, String fileName) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.file = new File(dir, fileName);
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    save2File(responseBody, file);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    onSuccess(null);
                } else {
                    onFail(ErrorMap.getUnknownMsg());
                }
            }
        }.execute();
    }

    private static void save2File(ResponseBody responseBody, File file) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = responseBody.byteStream();
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
