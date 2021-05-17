package com.xxxx.api.uilt;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxxx.api.entity.Qq;
import com.xxxx.api.mapper.PersonMapper;
import com.xxxx.api.service.PersonService;
import com.xxxx.api.service.QqService;
import com.xxxx.api.service.UsersService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class QQProvider {
    @Reference(interfaceClass = QqService.class)
    private QqService qqUserService;
    @Reference(interfaceClass = UsersService.class)
    private UsersService usersService;
    @Reference(interfaceClass = PersonService.class)
    private PersonService personService;
    @Autowired(required = false)
    private PersonMapper personMapper;

    public Qq getAccessToken(String url){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            Object json = JSONObject.parse(string);
            JSONObject json2 = (JSONObject) JSON.toJSON(json);
            String url2="https://graph.qq.com/oauth2.0/me?access_token="+json2.get("access_token").toString()+"&fmt=json";
            return getOpenID(url2,json2.get("access_token").toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Qq getOpenID(String url, String accessToken){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            Object json = JSONObject.parse(string);
            JSONObject json2 = (JSONObject) JSON.toJSON(json);
            System.out.println(json2);
            String url2="https://graph.qq.com/user/get_user_info?access_token=";
            url2+=accessToken;
            url2+="&oauth_consumer_key="+json2.get("client_id");
            url2+="&openid="+json2.get("openid");
            return getUserInfo(url2,json2.get("openid").toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Qq getUserInfo(String url, String openid){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            Object json = JSONObject.parse(string);
            JSONObject json2 = (JSONObject) JSON.toJSON(json);
            System.out.println(json2);
            Qq qqUser = JSON.parseObject(string, Qq.class);
            qqUser.setOpenid(openid);
            qqUser.setRole("ROLE_QQ");
            return qqUser;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
