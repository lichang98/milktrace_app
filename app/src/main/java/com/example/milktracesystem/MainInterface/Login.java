package com.example.milktracesystem.MainInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.R;
import com.example.milktracesystem.httpbean.LoginBean;
import com.google.gson.Gson;
import com.rey.material.app.Dialog;
import com.stephentuso.welcome.WelcomeHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by 李畅 on 2017/9/16.
 */

public class Login extends AppCompatActivity{
//    private Button button1;
//    private Button buttonRegister;      //注册按钮
//    private WelcomeHelper welcomeHelper;    //欢迎界面
    private com.rey.material.widget.TextView createAccountText;     //点击创建账户的文本
    private com.rey.material.widget.Button loginButton; //登录按钮
    @Override
    protected void onCreate(Bundle savedInstanceStatue){
        super.onCreate(savedInstanceStatue);
        setContentView(R.layout.login_layout);
        //欢迎界面
//        welcomeHelper = new WelcomeHelper(Login.this,Activity_Launch.class);
//        welcomeHelper.show(savedInstanceStatue);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }

        final android.support.design.widget.TextInputEditText userNameEdit = (android.support.design.widget.TextInputEditText)
                findViewById(R.id.username);    //用户名edittext
        final android.support.design.widget.TextInputEditText passwdEdit = (android.support.design.widget.TextInputEditText)
                findViewById(R.id.passwd);  //登录密码



        createAccountText = (com.rey.material.widget.TextView)findViewById(R.id.login_create_account);
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        loginButton = (com.rey.material.widget.Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 测试，获取服务器端的验证
                String username = userNameEdit.getText().toString();
                String passwd = passwdEdit.getText().toString();
                final LoginBean loginBean = new LoginBean(username,passwd);
                final Gson gson = new Gson(); //使用gson 将bean 转换为json格式的字符串
                Log.i("发送的json 字符串为：" , gson.toJson(loginBean));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.sendJsonData(HttpUtil.URL_LOGIN,
                                HttpUtil.LOGIN_INFO,gson.toJson(loginBean), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        //成功返回结果
                                        final String backResult = response.body().string();
                                        //判断服务器端是否确认成功
                                        if(backResult.trim().equals("check")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(Login.this,
                                                            "服务器端确认成功", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Login.this,
                                                            GeneralActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }else{
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(Login.this,
                                                            "服务器端认证失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }
                                });
                    }
                }).start();

//                Intent intent = new Intent(Login.this,GeneralActivity.class);
//                startActivity(intent);
            }
        });
//        button1 = (Button)findViewById(R.id.login);
//        button1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                String username = ((EditText)findViewById(R.id.account)).getText().toString();
//                String pwd = ((EditText)findViewById(R.id.password)).getText().toString();
//                if(username.equals("") && pwd.equals("")){
//                    Intent intent = new Intent(Login.this,GeneralActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(Login.this,"用户名或密码错误！",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        buttonRegister = (Button)findViewById(R.id.register);
//        //注册按钮点击
//        buttonRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Login.this,Register.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        welcomeHelper.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            Login.this.finish();
        return true;
    }
}















