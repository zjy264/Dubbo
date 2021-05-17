package com.xxxx.api.uilt;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class BaiduTongjiUtil {
    public Object getData(JSONObject jsonObject){
        String url="https://api.baidu.com/json/tongji/v1/ReportService/getData";
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonObject));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return JSONObject.parse(string);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "error";
    }
}
