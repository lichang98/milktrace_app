package com.example.milktracesystem.MainInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.milktracesystem.DBC.DBConnect;
import com.example.milktracesystem.Factory_Acticity.FactoryActivity;
import com.example.milktracesystem.Factory_Acticity.Factory_USER.InfoInput;
import com.example.milktracesystem.Factory_Acticity.Product_Factory.ProductFactory_Activity;
import com.example.milktracesystem.Feedback_Activity.FeedBackActivity;
import com.example.milktracesystem.News_Activity.NewsActivity;
import com.example.milktracesystem.R;
import com.example.milktracesystem.Search_Activity.SearchActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李畅 on 2017/9/16.
 */

public class GeneralActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;      //侧边滑出菜单栏
    private static ViewPager viewPager;
    private RadioGroup group;
    private static int[] imageIds={R.drawable.showmilk1,R.drawable.showmilk2,R.drawable.showmilk3};
    private List<ImageView> imageViewList;
    //当前及上一个索引位置
    private static int index=0,preIndex=0;
    private boolean isContinue=true;
    //轮播定时器
    private Timer timer = new Timer();
    private MyHandler myHandler;
    public static class MyHandler extends android.os.Handler{
        private WeakReference<GeneralActivity> weakReference;
        public MyHandler(GeneralActivity activity){
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg){
            if(weakReference.get()!=null){
                index++;
                if(index > imageIds.length)
                    index %= imageIds.length;
                viewPager.setCurrentItem(index);
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  //顶部ToolBar
        setSupportActionBar(toolbar);

        initView();
        initData();
        addListener();
        initRadioButton(imageIds.length);
        startSwitch();
        //fragment 企业信息展示，查询碎片界面点击事件
        ImageButton imageButton1 = (ImageButton)findViewById(R.id.imageButton);     //厂商产品链信息
        ImageButton imageButton2 = (ImageButton)findViewById(R.id.imageButton4);       //产品信息查询
        ImageButton imageButton3 = (ImageButton)findViewById(R.id.imageButton3);        //投诉建议
        ImageButton imageButton4 = (ImageButton)findViewById(R.id.imageButton2);       //新闻政策类界面
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改后的，通过该入口仅进入企业数据采集入口
                Intent intent = new Intent(GeneralActivity.this,InfoInput.class);
                startActivity(intent);

               // Intent intent = new Intent(GeneralActivity.this, FactoryActivity.class);
//                showSingleChoiceDialog();       //弹出对话框，选择进入哪个Activity,1普通消费者入口，0企业用户
             /*   if(choice == 1){
                    startActivity(intent);
                }else{
                    intent = null;
                    intent = new Intent(GeneralActivity.this, InfoInput.class);
                    startActivity(intent);
                }*/
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeneralActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(GeneralActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeneralActivity.this,NewsActivity.class);
                startActivity(intent);
            }
        });
        //DrawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawable_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);    //侧边滑出栏
        ActionBar actionbar= getSupportActionBar();
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navigationView.setCheckedItem(R.id.nav_mail);       //添加navigationView子项
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();//关闭drawerlayout
                return true;
            }
        });

        //连接数据库测试
      /*  try{
            DBConnect dbConnect = new DBConnect();
            dbConnect.getConnection();
            if(dbConnect.connection == null)
                Toast.makeText(this, "NULL Pointer to DB", Toast.LENGTH_SHORT).show();
            Statement statement = dbConnect.connection.createStatement();
            Toast.makeText(this, "成功连接", Toast.LENGTH_SHORT).show();
            ResultSet resultSet = statement.executeQuery("select * from teacher");
            Toast.makeText(this, "成功获取结果集", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "the first record is :"+resultSet.getString(1), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    /**
     *
     * 在主界面的信息采集(第一个)点击后弹出对话框，选择进入哪类界面
     * 提供企业以及消费者用户的入口
     * @return 返回值为0 表示企业用户，为1 表示普通用户
     */
    private int choice;
    @Deprecated
    private void showSingleChoiceDialog(){
        final String[] types = {"企业入口","消费者入口"};
        choice = -1;
        AlertDialog.Builder singleChioceDialog = new AlertDialog.Builder(GeneralActivity.this);
        singleChioceDialog.setTitle("选择进入的界面");
        //点击事件，获取用户点击的选项
        singleChioceDialog.setSingleChoiceItems(types, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });
        singleChioceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(choice != -1){
                    Toast.makeText(GeneralActivity.this, "你选择了"+choice, Toast.LENGTH_SHORT).show();
                    if(choice == 1){            //进入消费者入口
                        Intent intent = new Intent(GeneralActivity.this,FactoryActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(GeneralActivity.this,InfoInput.class);
                    startActivity(intent);
                }
            }
        });
        singleChioceDialog.show();
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
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "扫描结果："+result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
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
                new IntentIntegrator(GeneralActivity.this).initiateScan();  //启动扫描
                break;
            case R.id.more:
                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:         //侧边滑出栏
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
/////////////////////////////////////////////////////////////////////////////
    /**
     * 初始化轮播控件
     */
    public void initView(){
        viewPager = (ViewPager)findViewById(R.id.show_pages);
        group = (RadioGroup) findViewById(R.id.radio_group);
    }
    /**
     * 初始化数据
     */
    public void initData(){
        imageViewList = new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);
        myHandler = new MyHandler(GeneralActivity.this);
    }
    /**
     * 添加监听
     */
    public void addListener(){
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setOnTouchListener(onTouchListener);
    }
    /**
     * 图片轮播
     */
    public void startSwitch(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(isContinue){
                    if(imageIds.length != 1)
                        myHandler.sendEmptyMessage(1);
                }
            }
        },3000,3500);
    }

    /**
     * 根据图片的个数初始化按钮
     */
    private void initRadioButton(int length){
        for(int i=0;i<length;++i){
            ImageView imageView = new ImageView(this);
            if(length==1){
                imageView.setVisibility(View.GONE);
                return;
            }
            imageView.setPadding(20,0,0,0);     //设置按钮间距
            group.addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams
                    .WRAP_CONTENT);
            group.getChildAt(0).setEnabled(false);
        }
    }

    /**
     * 根据是否触摸决定是否轮播
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue=false;
                    break;
                default:
                    isContinue=true;
            }
            if(imageIds.length == 1){
                return true;
            }
            return false;
        }
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
        }
        @Override
        public void onPageSelected(int position){
            index = position; //当前位置赋值给索引
            setCurrentDot(index%imageIds.length);
        }
        @Override
        public void onPageScrollStateChanged(int state){

        }
    };

    /**
     * 设置对应位置按钮的状态
     * @param i
     */
    private void setCurrentDot(int i){
        if(group.getChildAt(i)!=null){
            group.getChildAt(i).setEnabled(false);
        }
        if(group.getChildAt(preIndex) != null){
            group.getChildAt(preIndex).setEnabled(true);
            preIndex = i;
        }
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container,int position){
            position = position % imageIds.length;  //position 不能大于图片集合的长度
            ImageView imageView = new ImageView(GeneralActivity.this);
            imageView.setImageResource(imageIds[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            imageViewList.add(imageView);
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            container.removeView(imageViewList.get(position));
        }
    };
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
        //页面销毁的时候取消定时器
        if(timer != null){
            preIndex=0;
            index=0;
            timer.cancel();
        }
    }
}
