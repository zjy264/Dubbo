package com.xxxx.consumer.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.api.uilt.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:没有访问权限
 * @author: zjy
 * @date: 2021/4/22
 */
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        PrintWriter writer2 = httpServletResponse.getWriter();
        Result result=new Result();
        result.setCode("403");
        result.setMsg("失败");
        result.setData(e.getMessage());
        writer2.write(new ObjectMapper().writeValueAsString(result));
        writer2.flush();
        writer2.close();
    }
}