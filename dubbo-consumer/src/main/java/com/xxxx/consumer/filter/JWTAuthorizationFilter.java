package com.xxxx.consumer.filter;


import com.xxxx.api.entity.dto.LoginToken;
import com.xxxx.api.uilt.JwtTokenUtils;
import com.xxxx.consumer.controller.UsersController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    // 这里从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        Boolean outdata = JwtTokenUtils.isExpiration(token);
        if (!outdata){
//            System.out.println("过期");
            return null;
        }else {
//            System.out.println("未过期");
            String username = JwtTokenUtils.getUsername(token);
            LoginToken loginToken=UsersController.getToken(username);
            if (UsersController.getToken(username)!=null){
                if (loginToken.getName()!=null){
                    if (loginToken.getToken().equals(token)){
                        String role = JwtTokenUtils.getUserRole(token);
                        return new UsernamePasswordAuthenticationToken(username, null,
                                Collections.singleton(new SimpleGrantedAuthority(role))
                        );
                    }
                }
            }

//            for (String key : JWTAuthenticationFilter.hashMap.keySet()) {
//                if (key.equals(username)){
//                    if(JWTAuthenticationFilter.hashMap.get(key).get("token").equals(token)){
//                        String role = JwtTokenUtils.getUserRole(token);
//                        if (username != null) {
//                            return new UsernamePasswordAuthenticationToken(username, null,
//                                    Collections.singleton(new SimpleGrantedAuthority(role))
//                            );
//                        }
//                    }
//                }
//            }
            return null;
        }
    }

}
