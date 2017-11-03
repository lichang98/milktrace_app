package com.example.milktracesystem.HttpUtil;

/**
 * Created by 李畅 on 2017/9/16.
 */

/**
 * http连接回调机制接口
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
