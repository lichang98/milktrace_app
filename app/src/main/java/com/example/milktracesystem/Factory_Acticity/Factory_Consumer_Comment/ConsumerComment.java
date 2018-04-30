package com.example.milktracesystem.Factory_Acticity.Factory_Consumer_Comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
import com.example.milktracesystem.R;
import com.rey.material.widget.Spinner;

import java.lang.reflect.Method;

/**
 * Created by 李畅 on 2017/12/17.
 * 该类为用户评论界面
 * 用户在企业信息浏览界面通过点击进入企业评论界面
 */

public class ConsumerComment extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_comment);     //用户评论界面

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_consumer_comment);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }




    }



    //在OptionMenu中显示图片
    private void setIconVisible(Menu menu, boolean flag){
        if(menu != null){
            try{
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu,flag);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.general_toolbar, menu);
        setIconVisible(menu,true);
        return true;
    }
    //菜单子项的操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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
            case android.R.id.home:
               ConsumerComment.this.finish();
                break;
            default:
                break;
        }
        return true;
    }


}
