package com.xxxx.api.entity.dto;


import java.io.Serializable;

public class LoginToken implements Serializable {
    private String name;
    private String token;
    private String authorities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "LoginToken{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }
}
