package com.example.milktracesystem.MainInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milktracesystem.R;
import com.rey.material.app.Dialog;
import com.stephentuso.welcome.WelcomeHelper;


/**
 * Created by 李畅 on 2017/9/16.
 */

public class Login extends AppCompatActivity{
//    private Button button1;
//    private Button buttonRegister;      //注册按钮
    private WelcomeHelper welcomeHelper;    //欢迎界面
    private com.rey.material.widget.TextView createAccountText;     //点击创建账户的文本
    private com.rey.material.widget.Button loginButton; //登录按钮
    @Override
    protected void onCreate(Bundle savedInstanceStatue){
        super.onCreate(savedInstanceStatue);
        setContentView(R.layout.login_layout);
        //欢迎界面
        welcomeHelper = new WelcomeHelper(Login.this,Activity_Launch.class);
        welcomeHelper.show(savedInstanceStatue);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

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
                Intent intent = new Intent(Login.this,GeneralActivity.class);
                startActivity(intent);
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
        welcomeHelper.onSaveInstanceState(outState);
    }

}















