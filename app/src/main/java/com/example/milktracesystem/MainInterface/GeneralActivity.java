package com.example.milktracesystem.MainInterface;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.stephentuso.welcome.WelcomeHelper;
import com.tmall.ultraviewpager.IUltraIndicatorBuilder;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.UltraViewPagerAdapter;
import com.tmall.ultraviewpager.UltraViewPagerView;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.alibaba.android.vlayout.*;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李畅 on 2017/9/16.
 */

public class GeneralActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;      //侧边滑出菜单栏
//    private static ViewPager viewPager;
    private WelcomeHelper welcomeHelper;    //欢迎界面，仅在首次打开软件时
    private UltraViewPager ultraViewPager;      //viewpager
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
//        public MyHandler(GeneralActivity activity){
//            weakReference = new WeakReference<>(activity);
//        }
//        @Override
//        public void handleMessage(Message msg){
//            if(weakReference.get()!=null){
//                index++;
//                if(index > imageIds.length)
//                    index %= imageIds.length;
//                viewPager.setCurrentItem(index);
//            }
//            super.handleMessage(msg);
//        }
    }
    private android.support.v7.widget.CardView cardViewDataAccu;
    private android.support.v7.widget.CardView cardViewProductSearch;
    private android.support.v7.widget.CardView cardViewFeedback;
    private android.support.v7.widget.CardView cardViewNewsPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);  //顶部ToolBar
        setSupportActionBar(toolbar);
        //欢迎界面初始化
        welcomeHelper = new WelcomeHelper(GeneralActivity.this,Activity_Launch.class);
        welcomeHelper.show(savedInstanceState);

        ultraViewPager = (com.tmall.ultraviewpager.UltraViewPager)findViewById(R.id.main_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        ultraViewPager.setAdapter(new UltraPagerAdapter(true));
        ultraViewPager.setMultiScreen(0.6f);
        ultraViewPager.setItemRatio(1.0f);
        ultraViewPager.setMaxHeight(300);
        ultraViewPager.setPageTransformer(false,new UltraDepthScaleTransformer());
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator().setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()));
        ultraViewPager.getIndicator().setIndicatorPadding(20);
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        ultraViewPager.getIndicator().build();
        ultraViewPager.setInfiniteLoop(true);
        ultraViewPager.setAutoScroll(2000);


        cardViewDataAccu = (android.support.v7.widget.CardView)findViewById(R.id.cardview_general_data_accu);
        cardViewDataAccu.setCardBackgroundColor(getResources().getColor(R.color.colorSwitchThumbNormal));
        cardViewDataAccu.setCardElevation(30);
        cardViewDataAccu.setRadius(10);
        cardViewFeedback = (android.support.v7.widget.CardView)findViewById(R.id.cardview_general_feedback);
        cardViewFeedback.setCardBackgroundColor(getResources().getColor(R.color.colorSwitchThumbNormal));
        cardViewFeedback.setCardElevation(30);
        cardViewFeedback.setRadius(10);
        cardViewProductSearch = (android.support.v7.widget.CardView)findViewById(R.id.cardview_general_productsearch);
        cardViewProductSearch.setBackgroundColor(getResources().getColor(R.color.colorSwitchThumbNormal));
        cardViewProductSearch.setCardElevation(30);
        cardViewProductSearch.setRadius(10);
        cardViewNewsPolicy = (android.support.v7.widget.CardView)findViewById(R.id.cardview_general_newspolicy);
        cardViewNewsPolicy.setBackgroundColor(getResources().getColor(R.color.colorSwitchThumbNormal));
        cardViewNewsPolicy.setCardElevation(30);
        cardViewNewsPolicy.setRadius(10);
        //数据采集
        cardViewDataAccu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralActivity.this,InfoInput.class));
            }
        });
        //产品查询
        cardViewProductSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralActivity.this,SearchActivity.class));
            }
        });
        //问题产品投诉
        cardViewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralActivity.this,FeedBackActivity.class));
            }
        });
        //新闻政策
        cardViewNewsPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneralActivity.this,com.hhxplaying.neteasedemo.netease.activity.MainActivity.class));
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO 预加载头条新闻的图片与标题到主界面
                Connection connection = Jsoup.connect("http://www.chinadairy.net/");
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");

                String imgUrl = "";
                String title = "";
                String contentUrl = "";
                try {
                    final Document document = connection.get();
                    Elements elements = document.getElementsByClass("pic");
                    Element ele = elements.get(0).children().select("a").get(0);
                    imgUrl = ele.select("img").attr("src");
                    title = ele.attr("title");
                    contentUrl = ele.attr("href");  //新闻内容的url 链接
                    notifyGeneralList(imgUrl,title,contentUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        initView();
//        initData();
//        addListener();
//        initRadioButton(imageIds.length);
//        startSwitch();
        //fragment 企业信息展示，查询碎片界面点击事件
//        ImageButton imageButton1 = (ImageButton)findViewById(R.id.imageButton);     //厂商产品链信息
//        ImageButton imageButton2 = (ImageButton)findViewById(R.id.imageButton4);       //产品信息查询
//        ImageButton imageButton3 = (ImageButton)findViewById(R.id.imageButton3);        //投诉建议
//        ImageButton imageButton4 = (ImageButton)findViewById(R.id.imageButton2);       //新闻政策类界面
//        imageButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 修改后的，通过该入口仅进入企业数据采集入口
//                Intent intent = new Intent(GeneralActivity.this,InfoInput.class);
//                startActivity(intent);
//
//               // Intent intent = new Intent(GeneralActivity.this, FactoryActivity.class);
////                showSingleChoiceDialog();       //弹出对话框，选择进入哪个Activity,1普通消费者入口，0企业用户
//             /*   if(choice == 1){
//                    startActivity(intent);
//                }else{
//                    intent = null;
//                    intent = new Intent(GeneralActivity.this, InfoInput.class);
//                    startActivity(intent);
//                }*/
//            }
//        });
//        imageButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GeneralActivity.this,SearchActivity.class);
//                startActivity(intent);
//            }
//        });
//        imageButton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(GeneralActivity.this, FeedBackActivity.class);
//                startActivity(intent);
//            }
//        });
//        //FIXME jumo to reference project module
//        imageButton4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(GeneralActivity.this,com.hhxplaying.neteasedemo.netease.activity.MainActivity.class);
//                startActivity(intent);
//            }
//        });
        //DrawerLayout
        drawerLayout = (DrawerLayout)findViewById(R.id.drawable_layout);
        //使用第三方库添加侧边划出栏
        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(GeneralActivity.this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem().withName("用户")
                .withEmail("user@gmail.com").withIcon(R.drawable.profile3))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1)
                .withName("我的反馈记录").withIcon(R.drawable.history);
        Drawer drawerResult = new DrawerBuilder().withActivity(GeneralActivity.this)
                .withAccountHeader(headerResult)
                .addDrawerItems(item1, new DividerDrawerItem())
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return false;
                    }
                })
                .withToolbar(toolbar)
                .build();
//        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);    //侧边滑出栏
        ActionBar actionbar= getSupportActionBar();
//        if(actionbar != null){
//            actionbar.setDisplayHomeAsUpEnabled(true);
//            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        }
//        navigationView.setCheckedItem(R.id.nav_mail);       //添加navigationView子项
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                drawerLayout.closeDrawers();//关闭drawerlayout
//                return true;
//            }
//        });

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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeHelper.onSaveInstanceState(outState);
    }

    /**
     * 用于在网络资源加载完成后提示界面数据显示
     * @param imgUrl        显示新闻图片的URL
     * @param title         显示新闻图片的标题
     * @param contentUrl    具体的新闻内容的url
     */
    public void notifyGeneralList(final String imgUrl,final String title,final String contentUrl){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HashMap<String,Object>> list = new ArrayList<>();
                HashMap<String,Object> map = new HashMap<>(),map1 = new HashMap<>();
                map.put("itemText","头条新闻");
                map.put("itemImage",imgUrl);
                map.put("itemTitle",title);
                map.put("itemcontentUrl",contentUrl);   //新闻内容的网址
                map1.put("itemText","知识答题");
                map1.put("itemImage",R.drawable.milk_questions);
                map1.put("itemTitle","");
                map1.put("itemcontentUrl","");

                list.add(map);
                list.add(map1);
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.general_recyclerview);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GeneralActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                GeneralItemAdapter generalItemAdapter = new GeneralItemAdapter(list);
                recyclerView.setAdapter(generalItemAdapter);
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        super.onDraw(c, parent, state);
                        int childCount = parent.getChildCount();
                        int left,top,right,bottom;
                        View child = parent.getChildAt(0);
                        //获取分割线的高度
                        Drawable drawable = getResources().getDrawable(R.drawable.bg_title_bar);
                        int drawableHeight = drawable.getIntrinsicHeight();
                        left = parent.getLeft();
                        right = parent.getRight();
                        for(int i=1;i<=childCount;++i){
                            top = child.getTop() - drawableHeight/3*2;
                            bottom = child.getTop() - drawableHeight/3;
                            drawable.setBounds(left,top,right,bottom);
                            drawable.draw(c);
                            child = parent.getChildAt(i);
                        }
                    }

                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.top = 100;
                    }
                });
            }
        });
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
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                //用户登录按钮
                Intent intent = new Intent(GeneralActivity.this,Login.class);
                startActivity(intent);
                break;
//            case R.id.scan:
//                Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
//                new IntentIntegrator(GeneralActivity.this).initiateScan();  //启动扫描
//                break;
//            case R.id.more:
//                Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.about_us:
                Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(GeneralActivity.this,AboutusActivity.class);
                startActivity(intent1);
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
//    public void initView(){
//        viewPager = (ViewPager)findViewById(R.id.show_pages);
//        group = (RadioGroup) findViewById(R.id.radio_group);
//    }
//    /**
//     * 初始化数据
//     */
//    public void initData(){
//        imageViewList = new ArrayList<>();
//        viewPager.setAdapter(pagerAdapter);
//        myHandler = new MyHandler(GeneralActivity.this);
//    }
//    /**
//     * 添加监听
//     */
//    public void addListener(){
//        viewPager.addOnPageChangeListener(onPageChangeListener);
//        viewPager.setOnTouchListener(onTouchListener);
//    }
//    /**
//     * 图片轮播
//     */
//    public void startSwitch(){
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(isContinue){
//                    if(imageIds.length != 1)
//                        myHandler.sendEmptyMessage(1);
//                }
//            }
//        },3000,3500);
//    }
//
//    /**
//     * 根据图片的个数初始化按钮
//     */
//    private void initRadioButton(int length){
//        for(int i=0;i<length;++i){
//            ImageView imageView = new ImageView(this);
//            if(length==1){
//                imageView.setVisibility(View.GONE);
//                return;
//            }
//            imageView.setPadding(20,0,0,0);     //设置按钮间距
//            group.addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams
//                    .WRAP_CONTENT);
//            group.getChildAt(0).setEnabled(false);
//        }
//    }
//
//    /**
//     * 根据是否触摸决定是否轮播
//     */
//    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch(motionEvent.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                case MotionEvent.ACTION_MOVE:
//                    isContinue=false;
//                    break;
//                default:
//                    isContinue=true;
//            }
//            if(imageIds.length == 1){
//                return true;
//            }
//            return false;
//        }
//    };
//
//    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
//        @Override
//        public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
//        }
//        @Override
//        public void onPageSelected(int position){
//            index = position; //当前位置赋值给索引
//            setCurrentDot(index%imageIds.length);
//        }
//        @Override
//        public void onPageScrollStateChanged(int state){
//
//        }
//    };
//
//    /**
//     * 设置对应位置按钮的状态
//     * @param i
//     */
//    private void setCurrentDot(int i){
//        if(group.getChildAt(i)!=null){
//            group.getChildAt(i).setEnabled(false);
//        }
//        if(group.getChildAt(preIndex) != null){
//            group.getChildAt(preIndex).setEnabled(true);
//            preIndex = i;
//        }
//    }
//
//    PagerAdapter pagerAdapter = new PagerAdapter() {
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//        @Override
//        public Object instantiateItem(ViewGroup container,int position){
//            position = position % imageIds.length;  //position 不能大于图片集合的长度
//            ImageView imageView = new ImageView(GeneralActivity.this);
//            imageView.setImageResource(imageIds[position]);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            container.addView(imageView);
//            imageViewList.add(imageView);
//            return imageView;
//        }
//        @Override
//        public void destroyItem(ViewGroup container,int position,Object object){
//            container.removeView(imageViewList.get(position));
//        }
//    };
//    @Override
//    public void onStart(){
//        super.onStart();
//    }
//    @Override
//    public void onStop(){
//        super.onStop();
//        //页面销毁的时候取消定时器
//        if(timer != null){
//            preIndex=0;
//            index=0;
//            timer.cancel();
//        }
//    }
}
