package com.imooc.miaoshaproject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Test {



    public static void main(String[] args) throws Exception {
        //String url = "http://10.22.82.111:8000/jcmadm/RMIService";
        String url = "http://localhost:8923/jcmadm/RMIService";

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        // 发送表单
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("className", "admJobFolderService"));
        params.add(new BasicNameValuePair("methodName", "deleteFolder"));//getFolderTree，saveOrUpdateFolder,deleteFolder
        //params.add(new BasicNameValuePair("params", "[{\"id\":\"\",\"parentFolderId\":\"\",\"sysCode\":\"TEST111\",\"folderCode\":\"TEST1\",\"folderName\":\"TEST2\",\"folderType\":\"smart\",\"calendarVersion\":\"1\"}]"));
        //params.add(new BasicNameValuePair("params", "[{\"keywords\":\"3\"}]"));
        params.add(new BasicNameValuePair("params", "[\"I4028b88197ce340d0197ce3466400001\"]"));


        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); // 表单编码
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("token", "1B46442D23136521953172451301D42D6F4A6552034D3AC8E8C805E88021B90F33E04B7A021D40E158A58B90635D240CAAD1F383D82437B2FD3BAE6A2A8C9880");

        // 执行请求
        HttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        System.out.println("状态码: " + statusCode);
        System.out.println("响应体: " + responseBody);
    }

}
