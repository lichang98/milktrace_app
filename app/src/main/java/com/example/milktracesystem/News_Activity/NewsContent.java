package com.example.milktracesystem.News_Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.R;

/**
 * 用于显示新闻的具体内容
 * Created by 李畅 on 2017/11/20.
 */

public class NewsContent extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscontent);
        String newsName = "乳制品新闻";         //显示界面的标题
        int newsImageId = R.drawable.showmilk2; //界面上方显示的图片资源ID号
        Toolbar toolbar = (Toolbar)findViewById(R.id.news_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsing_toolbar);
        ImageView newsImg = (ImageView)findViewById(R.id.news_imgview);//界面上方显示的图片
        TextView newsContent = (TextView)findViewById(R.id.news_content_text);//显示的新闻的内容
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);  //显示默认的返回键
        }
        collapsingToolbarLayout.setTitle(newsName); //设置标题
        Glide.with(this).load(newsImageId).into(newsImg);   //将资源中的图片载入ImageView
        newsContent.setText(R.string.news_content); //设置显示的新闻的内容
    }

    /**
     * 重写菜单选项的执行
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:     //android 默认，返回键
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
