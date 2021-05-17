package com.xxxx.consumer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.api.entity.dto.JwtUser;
import com.xxxx.api.entity.dto.LoginToken;
import com.xxxx.api.entity.dto.LoginUser;
import com.xxxx.api.uilt.JwtTokenUtils;
import com.xxxx.api.uilt.Result;
import com.xxxx.consumer.controller.UsersController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static HashMap<String,HashMap<String,String>> hashMap=new HashMap<String,HashMap<String,String>>();
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(),new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
//        System.out.println("jwtUser:" + jwtUser.toString());

        String role = "";
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }
        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), role, false);

//        ValueOperations operations=redisTemplate.opsForValue();
        LoginToken loginToken=new LoginToken();
        loginToken.setName(jwtUser.getUsername());
        loginToken.setToken(token);
        loginToken.setAuthorities(role);
        UsersController.saveToken(loginToken);
//        userTokenService.saveToken(loginToken);
//        operations.set(jwtUser.getUsername(),loginToken,100000);
//        HashMap<String,String > hashMap2=new HashMap<String, String>();//1未过期，0过期
//        hashMap2.put("token",token);
//        hashMap2.put("expired","1");
//        hashMap.put(jwtUser.getUsername(),hashMap2);
//        System.out.println("token:"+token);
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        Result result=new Result();
        result.setCode("200");
        result.setMsg("成功");
        result.setData(token);
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }

    // 这是验证失败时候调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        Result result=new Result();
        result.setCode("400");
        result.setMsg("账号或密码错误");
        result.setData(failed.getMessage());
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
