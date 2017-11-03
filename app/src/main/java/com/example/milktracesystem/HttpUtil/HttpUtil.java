package com.example.milktracesystem.HttpUtil;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by 李畅 on 2017/9/16.
 */
//网络连接工具类


public class HttpUtil {
    public static void sendHttpRequest(final String address,final
                                       HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if(listener != null)
                        listener.onFinish(response.toString());
                }catch (Exception e){
                    if(listener != null)
                        listener.onError(e);
                }finally {
                    if(connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    /**
     * 使用OkHttp
     */
    public static void sendOkHttpRequest(String address,String param, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
       Request request = new Request.Builder()
                .addHeader("type",param)
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png;charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();
    public static final String POST_FEEDBACK_BITMAP = "feed_back_post";
    /**
     * 用于上传本地数据至服务器
     * @param mediaType  上传数据类型
     * @param data  为了保证可以传递各种类型的数据，传入的数据为byte[]类型
     * @param  url  目标服务器的url地址
     * @param  postType  用于在传送头部添加自定义信息，区别不同的提交请求
     */
    public static void postByOkHttp(final MediaType mediaType,final byte[] data, final String url
    , String postType) {
       RequestBody requestBody = new RequestBody() {
           @Override
           public MediaType contentType() {
               return mediaType;
           }

           @Override
           public void writeTo(BufferedSink bufferedSink) throws IOException {
               bufferedSink.write(data);
               bufferedSink.flush();
               bufferedSink.close();
           }
       };

       Request request = new Request.Builder()
               .url(url)
               .addHeader("post_type", postType)        //添加自定义头部，标识上传类型
               .post(requestBody)
               .build();
        try{
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful())
                throw new IOException("传送发生错误！"+response);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
