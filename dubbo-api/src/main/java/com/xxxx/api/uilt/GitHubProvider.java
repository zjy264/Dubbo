package com.xxxx.api.uilt;

import com.alibaba.fastjson.JSON;
import com.xxxx.api.entity.Github;
import okhttp3.*;

import java.io.IOException;

public class GitHubProvider {
    public String getAccessToken(String url){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return string.substring(13,string.indexOf("&"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "error";
    }
    public Github getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+accessToken)
                .addHeader("Cookie", "_octo=GH1.1.1095327959.1612336421; logged_in=no")
                .build();
        Headers requestHeaders = request.headers();
        int requestHeadersLength = requestHeaders.size();
        for (int i = 0; i < requestHeadersLength; i++){
            String headerName = requestHeaders.name(i);
            String headerValue = requestHeaders.get(headerName);
            System.out.print("TAG----------->Name:"+headerName+"------------>Value:"+headerValue+"\n");
        }
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            Github githubUser = JSON.parseObject(string, Github.class);//将string解析成GitHub对象
            return githubUser;
        } catch (IOException e) {
            return null;
        }

    }
}
