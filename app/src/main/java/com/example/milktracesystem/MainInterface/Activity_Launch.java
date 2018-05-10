package com.example.milktracesystem.MainInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 李畅 on 2017/6/3.
 */

public class Activity_Launch extends WelcomeActivity{
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(com.stephentuso.welcome.R.color.background_material_light)
                .page(new TitlePage(R.drawable.welcome1,"欢迎使用乳品追溯APP").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .page(new BasicPage(R.drawable.welcome2,"便捷的查询","方便的浏览企业以及产品的信息").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .page(new BasicPage(R.drawable.welcome3,"便捷的反馈途径","可以方便投诉问题产品并获得反馈").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .page(new BasicPage(R.drawable.welcome5,"最新的乳制品新闻","浏览最新的乳制品新闻资讯").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .page(new BasicPage(R.drawable.welcome6,"便捷的数据采集","方便的录入简单的数据").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .page(new BasicPage(R.drawable.welcome4,"开始使用吧！","").
                        background(com.stephentuso.welcome.R.color.wel_default_background_color))
                .swipeToDismiss(true)
                .build();

    }
}
//public class Activity_Launch extends AppCompatActivity {
//    private ImageView bingPicImg;
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_launch);
//        bingPicImg = (ImageView)findViewById(R.id.bing_pic);
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String bingPic = preferences.getString("bing_pic",null);
////        if(bingPic != null){
////            Glide.with(this).load(bingPic).into(bingPicImg);
////        }else{
////            loadBingPic();
////        }
//        Glide.with(this).load(R.drawable.software_logo).into(bingPicImg);
//        Integer time= 2000;
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(Activity_Launch.this,Login.class));
//                Activity_Launch.this.finish();
//            }
//        },time);
//    }
//
//    private void loadBingPic(){
//        String requestBingPic = "http://guolin.tech/api/bing_pic";
//        HttpUtil.sendOkHttpRequest(requestBingPic, "",new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String bingPic  = response.body().string();
//                SharedPreferences.Editor editor = PreferenceManager.
//                        getDefaultSharedPreferences(Activity_Launch.this).edit();
//                editor.putString("bingPic",bingPic);
//                editor.apply();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.with(Activity_Launch.this).load(bingPic)
//                                .into(bingPicImg);
//                    }
//                });
//            }
//        });
//    }
//}
