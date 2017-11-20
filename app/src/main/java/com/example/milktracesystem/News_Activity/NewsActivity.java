package com.example.milktracesystem.News_Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.milktracesystem.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by 李畅 on 2017/5/23.
 */

/**
 * 乳制品相关新闻政策信息界面
 */
public class NewsActivity extends AppCompatActivity{
 //   private List<NewsNormalItem> newsNormalItemList = new ArrayList<NewsNormalItem>();

    private NewsNormalItem[] newsNormalItems = {new NewsNormalItem("监管局视察", R.drawable.news_pic1),
    new NewsNormalItem("产品检查",R.drawable.news_pic2),new NewsNormalItem("对华恢复乳制品出口",
            R.drawable.news_pic3)};
    private List<NewsNormalItem> newsNormalItemList = new ArrayList<>();
    private NewsNormalAdapter newsNormalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //顶部工具栏
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitNews();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        //第二个参数表示显示的列数
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        newsNormalAdapter = new NewsNormalAdapter(newsNormalItemList);
        newsNormalAdapter.setOnItemClickListener(new MyNewsItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(NewsActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(newsNormalAdapter);
 /*       LayoutInflater inflater = getLayoutInflater();
        ViewGroup container = (ViewGroup) getWindow().getDecorView();
        View view = inflater.inflate(R.layout.activity_news,container,false);
        InitNewsList();
        NewsNormalAdapter newsNormalAdapter = new NewsNormalAdapter(view.getContext(),
                R.layout.news_normal_item,newsNormalItemList);
        ListView listView = (ListView)findViewById(R.id.news_lists);    //新闻界面listView
        listView.setAdapter(newsNormalAdapter);;
        listView.setOnItemClickListener(new ListItemOnClickListener());
        */
        //悬浮按钮点击
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsActivity.this, "FINDING MORE", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //ToolBar 菜单
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.general_toolbar,menu);
        setIconVisible(menu,true);
        return true;
    }
    //在OptionMenu中显示图片
    private void setIconVisible(Menu menu,boolean flag){
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

    //菜单子项的操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.backwards:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.scan:
                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
                new IntentIntegrator(NewsActivity.this).initiateScan();  //启动扫描
                break;
            case R.id.more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void InitNews(){
        newsNormalItemList.clear();
        for(int i=0;i<10;i++){
            Random random = new Random();
            int index = random.nextInt(newsNormalItems.length);
            newsNormalItemList.add(newsNormalItems[index]);
        }
    }


}

