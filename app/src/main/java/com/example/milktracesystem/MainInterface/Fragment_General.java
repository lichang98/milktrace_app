package com.example.milktracesystem.MainInterface;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.milktracesystem.Factory_Acticity.FactoryActivity;
import com.example.milktracesystem.Feedback_Activity.FeedBackActivity;
import com.example.milktracesystem.News_Activity.NewsActivity;
import com.example.milktracesystem.R;
import com.example.milktracesystem.Search_Activity.SearchActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李畅 on 2017/5/22.
 */

public class Fragment_General extends android.support.v4.app.Fragment {
    private  View view;
    private static ViewPager viewPager;
    private RadioGroup group;
    private int[] imageIds={R.drawable.showmilk1,R.drawable.showmilk2,R.drawable.showmilk3};
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
                viewPager.setCurrentItem(index);
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_general_info,container,false);
        initView();
        initData();
        addListener();
        initRadioButton(imageIds.length);
        startSwitch();
        //fragment 企业信息展示，查询碎片界面点击事件
        ImageButton imageButton1 = (ImageButton)view.findViewById(R.id.imageButton);     //厂商产品链信息
        ImageButton imageButton2 = (ImageButton)view.findViewById(R.id.imageButton4);       //产品信息查询
        ImageButton imageButton3 = (ImageButton)view.findViewById(R.id.imageButton3);        //投诉建议
        ImageButton imageButton4 = (ImageButton)view.findViewById(R.id.imageButton2);       //新闻政策类界面
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),FactoryActivity.class);
                startActivity(intent);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view.getContext(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 初始化轮播控件
     */
    public void initView(){
        viewPager = (ViewPager)view.findViewById(R.id.show_pages);
        group = (RadioGroup) view.findViewById(R.id.radio_group);
    }
    /**
     * 初始化数据
     */
    public void initData(){
        imageViewList = new ArrayList<>();
        viewPager.setAdapter(pagerAdapter);
        myHandler = new MyHandler((GeneralActivity) view.getContext());
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
            ImageView imageView = new ImageView(view.getContext());
            if(length==1){
                imageView.setVisibility(View.GONE);
                return;
            }
            imageView.setPadding(20,0,0,0);     //设置按钮间距
            group.addView(imageView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams
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
            ImageView imageView = new ImageView(view.getContext());
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
 //           timer.cancel();
        }
    }
}
