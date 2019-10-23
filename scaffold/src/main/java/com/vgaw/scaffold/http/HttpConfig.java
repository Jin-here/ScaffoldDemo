package com.vgaw.scaffold.http;

/**
 * Created by caojin on 2017/5/25.
 */

import okhttp3.Interceptor;

/**
 * should be initialed in your application's onCreate() method
 */
public class HttpConfig {
    private static final int DEFAULT_CONNECT_TIME_OUT = 10;
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private boolean debug;
    private String secretId;
    private String secretKey;
    private long connectTimeout = DEFAULT_CONNECT_TIME_OUT;
    private long readTimeout = DEFAULT_READ_TIME_OUT;
    private String url;
    private String testUrl;
    private Interceptor interceptor;

    public HttpConfig secretId(String secretId) {
        this.secretId = secretId;
        return this;
    }

    public String secretId() {
        return secretId;
    }

    public HttpConfig secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String secretKey() {
        return secretKey;
    }

    public HttpConfig debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public boolean debug() {
        return this.debug;
    }

    /**
     * @param connectTimeout    in second
     */
    public HttpConfig connectTimeout(long connectTimeout) {
        if (connectTimeout > -1) {
            this.connectTimeout = connectTimeout;
        }
        return this;
    }

    public long connectTimeout() {
        return connectTimeout;
    }

    /**
     * @param readTimeout    in second
     */
    public HttpConfig readTimeout(long readTimeout) {
        if (readTimeout > -1) {
            this.readTimeout = readTimeout;
        }
        return this;
    }

    public long readTimeout() {
        return readTimeout;
    }

    public HttpConfig interceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public Interceptor interceptor() {
        return this.interceptor;
    }

    public HttpConfig url(String url) {
        this.url = url;
        return this;
    }

    public String url() {
        return url;
    }

    public HttpConfig testUrl(String testUrl) {
        this.testUrl = testUrl;
        return this;
    }

    public String testUrl() {
        return testUrl;
    }
}
