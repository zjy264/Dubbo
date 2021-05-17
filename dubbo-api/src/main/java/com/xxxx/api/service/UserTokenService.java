package com.xxxx.api.service;

import com.xxxx.api.entity.dto.LoginToken;

public interface UserTokenService {
    void saveToken(LoginToken loginToken);
    LoginToken getToken(String username);
}
