package com.vgaw.scaffolddemo.data.auth;

public class AuthInfo {
    private String token;

    public AuthInfo() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "token='" + token + '\'' +
                '}';
    }
}