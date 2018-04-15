package com.example.milktracesystem.Factory_Acticity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.milktracesystem.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by 李畅 on 2017/4/23.
 */

public class FactoryActivity extends AppCompatActivity {
    private MaterialFragment materialFragment = new MaterialFragment();
    private ProductFragment productFragment = new ProductFragment();
    private TransportFragment transportFragment = new TransportFragment();
    private SalesFragment salesFragment = new SalesFragment();
    private ViewPager viewPager;        //页面
    private View viewbar;       //底部滑动滚条
    private ArrayList<Fragment> fragmentArrayList;  //fragment列表
    private int currIndex;      //当前列表编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_layout);

        final Button button1 = (Button)findViewById(R.id.material_factory);
        final Button button2 = (Button)findViewById(R.id.product_factory);
        final Button button3 = (Button)findViewById(R.id.transport_factory);
        final Button button4 = (Button)findViewById(R.id.sales_factory);
        button1.setOnClickListener(new PageChangeListener(0));
        button2.setOnClickListener(new PageChangeListener(1));
        button3.setOnClickListener(new PageChangeListener(2));
        button4.setOnClickListener(new PageChangeListener(3));
        viewPager = (ViewPager)findViewById(R.id.viewpager);    //界面viewpager
        viewbar = (View)findViewById(R.id.bottom_bar);
        clearSelection();
        InitBar();      //初始化
        InitViewPager();
        button1.setTextColor(getResources().getColor(R.color.colorPrimary));
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
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
            case R.id.scan:
                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                FactoryActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
    /**
     * 清除按钮选中状态
     */
    public void clearSelection(){
        Button button = (Button)findViewById(R.id.material_factory);
        Button button1 = (Button)findViewById(R.id.product_factory);
        Button button2 = (Button)findViewById(R.id.transport_factory);
        Button button3 = (Button)findViewById(R.id.sales_factory);

        button.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        button1.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        button2.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));
        button3.setTextColor(getResources().getColor(R.color.material_drawer_primary_text));

    }
    /////////////////////////////////////////////////////////////////////////////
    //监听页面切换
    private class PageChangeListener implements View.OnClickListener{
        private int index=0;
        Button button = (Button)findViewById(R.id.material_factory);    //四类厂商的点击按妞
        Button button1 = (Button)findViewById(R.id.product_factory);
        Button button2 = (Button)findViewById(R.id.transport_factory);
        Button button3 = (Button)findViewById(R.id.sales_factory);
        public PageChangeListener(int i){
            index=i;
        }
        public void onClick(View view){
            viewPager.setCurrentItem(index);
            clearSelection();   //清除选中状态
            switch (index){
                case 0:
                    button.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 1:
                    button1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 2:
                    button2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case 3:
                    button3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    break;
                default:
                    break;
            }
        }
    }
    //页面切换时滚动条移动
    public void InitBar(){
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //设置滚动条宽度为屏幕的1/4
        int lineLength = metrics.widthPixels/4;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)viewbar.getLayoutParams();
        layoutParams.width = lineLength;
        viewbar.setLayoutParams(layoutParams);
    }
    //初始化ViewPager
    public void InitViewPager(){
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(materialFragment);        //将fragment添加入fragment列表
        fragmentArrayList.add(productFragment);
        fragmentArrayList.add(transportFragment);
        fragmentArrayList.add(salesFragment);
        viewPager.setAdapter(new Activity_General_Bottom_PagerAdapter(getSupportFragmentManager(),fragmentArrayList));
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int arg0,float arg1,int arg2){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)viewbar.getLayoutParams();
            if(currIndex == arg0){
                layoutParams.leftMargin = (int)(currIndex*viewbar.getWidth()+arg1*viewbar.getWidth());
            }else if(currIndex > arg0){
                layoutParams.leftMargin = (int)(currIndex*viewbar.getWidth()-(1-arg1)*viewbar.getWidth());
            }
            new PageChangeListener(currIndex).onClick(new View(FactoryActivity.this));
            viewbar.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageScrollStateChanged(int arg0){

        }
        @Override
        public void onPageSelected(int arg0){
            currIndex = arg0;
        }
    }
}
