package com.xxxx.api.uilt;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.*;
import com.xxxx.api.entity.Bookmain;
import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class QiniuCloudUtil {
    String ak = "lAw-DoYabOx2pqNwZfpGlWzyhC3sYvOLi792kN2i";
    String sk = "O-yxLfDK3dGFnBiXk_JN9007nW40CSjzQvhUZNzX";    // 密钥配置
    Auth auth = Auth.create(ak, sk);    // TODO Auto-generated constructor stub
    Configuration cfg = new Configuration(Region.region2());
    String bucketname = "vuebook";    //空间名
    UploadManager uploadManager = new UploadManager(cfg);
    public String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }
    public String put64image(String key,String file64) throws Exception {
        int l = -1;
        String url = "http://upload.qiniup.com/putb64/" + l+"/key/"+ UrlSafeBase64.encodeToString(key);
        //非华东空间需要根据注意事项 1 修改上传域名
        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder().
                url(url).
                addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        System.out.println(request.headers());
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        return file64;
    }
    //上传网络图片
    public void uploadimage(Bookmain bookmain){
        String remoteSrcUrl="http:"+bookmain.getPic().substring(0,bookmain.getPic().indexOf("?"));
        BucketManager bucketManager = new BucketManager(auth, cfg);
        String key=bookmain.getName();
        try {
            FetchRet fetchRet = bucketManager.fetch(remoteSrcUrl,bucketname, key);
            System.out.println(fetchRet.key);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
    //上传网络图片
    public String uploadimage(String url,String name){
        Configuration cfg = new Configuration(Region.region2());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        String key=name;
        try {
            FetchRet fetchRet = bucketManager.fetch(url,bucketname, key);
            System.out.println(fetchRet.key);
            return "https://qinui.zjykt.top/"+name;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
        return null;
    }
    public String uploadimage(MultipartFile uploadFile){
        try {
            String key=uploadFile.getOriginalFilename();
            com.qiniu.http.Response response = uploadManager.put(uploadFile.getBytes(),key, getUpToken());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            com.qiniu.http.Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}