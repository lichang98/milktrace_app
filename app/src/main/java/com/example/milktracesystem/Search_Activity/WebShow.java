package com.example.milktracesystem.Search_Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.milktracesystem.R;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

/**
 * Created by 李畅 on 2017/11/3.
 */

/**
 * 使用Cordova框架，使用网页assets/www/index.html
 */
public class WebShow extends CordovaActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webshow);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar_webshow);      //顶部标题栏

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
        //使用getIntent获取SearchActivity传递的网址url
//        Intent intent = getIntent();
//        launchUrl = intent.getStringExtra("webUrl");
        launchUrl = "file:///android_asset/www/material_show.jsp";
        loadUrl(launchUrl);     //启动页面 www/index.html
    }


    @Override
    protected CordovaWebView makeWebView(){

        SystemWebView webView = (SystemWebView) findViewById(R.id.cordova_webview);
        webView.getSettings().setJavaScriptEnabled(true);       //允许javascript脚本
        CordovaWebView cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(webView));
        return cordovaWebView;
    }

    @Override
    protected void createViews(){
        if(preferences.contains("BackgroundColor")){
            int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
            appView.getView().setBackgroundColor(backgroundColor);
        }
        appView.getView().requestFocusFromTouch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.general_toolbar,menu);
        setIconVisible(menu,true);
        return true;
    }


    /**
     * 使得菜单项能够显示图标
     * @param menu  菜单
     * @param flag  是否显示图标
     */
    public void setIconVisible(Menu menu,boolean flag){
        if(menu != null){
            try{        //使用反射的方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu,flag);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 对toolbar菜单子项的操作
     * @param item  菜单中的选项
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.backwards:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.scan:
//                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.more:
//                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:         //退出当前活动
                WebShow.this.finish();
            default:
                break;
        }
        return true;
    }


}
