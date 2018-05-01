package com.example.milktracesystem.Feedback_Activity;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.milktracesystem.Camera.BarcodeEncoder;
import com.example.milktracesystem.Factory_Acticity.Factory_USER.InfoInput;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.MainInterface.DialogForChooseImgMethod;
import com.example.milktracesystem.MainInterface.Fragment_General;
import com.example.milktracesystem.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 李畅 on 2017/9/16.
 */

public class FeedBackActivity extends AppCompatActivity{
    private ViewPager viewPager;    //主界面viewpager
    private Fragment fragmentFeedBackCustomer;      //消费者反馈界面
    private Fragment fragmentFeedBackFactory;   //企业投诉记录展示界面
    private ArrayList<Fragment> fragmentArrayList;  //fragment 数组
    private PagerSlidingTabStrip pagerSlidingTabStrip;  //顶部tab
    private ArrayList<String> fragmentTitles;   //fragment 标题

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
//        takePhoto = (Button)findViewById(R.id.takephotos);  //拍照按钮
//        picture = (ImageView)findViewById(R.id.picture);        //显示拍照后的照片
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
        viewPager = (ViewPager)findViewById(R.id.feed_back_viewpager);
        //添加fragment 到view pager中
        fragmentFeedBackCustomer = new FeedBackCustomerFragment();
        fragmentFeedBackFactory = new FeedBackFactoryRecordFragment();
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(fragmentFeedBackCustomer);
        fragmentArrayList.add(fragmentFeedBackFactory);
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("消费者");
        fragmentTitles.add("企业被投诉记录");
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentArrayList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentArrayList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentTitles.get(position);
            }
        });
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        pagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.feed_back_tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        //设置tab 的属性
        pagerSlidingTabStrip.setUnderlineColor(Color.WHITE);
        pagerSlidingTabStrip.setUnderlineHeight(10);
        pagerSlidingTabStrip.setIndicatorColor(Color.rgb(0,238,238));
        pagerSlidingTabStrip.setIndicatorHeight(10);
        pagerSlidingTabStrip.setTabPaddingLeftRight(50);
        pagerSlidingTabStrip.setTextSize(50);
        pagerSlidingTabStrip.setTextColor(Color.rgb(100,149,237));


        //     chooseFromAlbum = (Button)findViewById(R.id.choose_from_album);
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //创建File对象，用于存储拍照后的照片
//                File outputImage = new File (Environment.getExternalStorageDirectory(),"output_image.jpg");
//                Toast.makeText(FeedBackActivity.this, "照片将自动存储在手机中", Toast.LENGTH_SHORT).show();
//                try{
//                    if(outputImage.exists()){
//                        outputImage.delete();
//                    }
//                    outputImage.createNewFile();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                imageUri = Uri.fromFile(outputImage);
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//                startActivityForResult(intent,TAKE_PHOTO);
//            }
//        });
        //生成二维码
//        Button btGenerateBarCode = (Button)findViewById(R.id.produce_barcode_bt);   //点击生成二维码的按钮
//        final ImageView imgBarCode = (ImageView)findViewById(R.id.img_generated_barcode);
//        btGenerateBarCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                try{
//                    Bitmap bitmap = barcodeEncoder.encodeBitmap("milk_trace",BarcodeFormat.QR_CODE,100,100);
//                    imgBarCode.setImageBitmap(bitmap);
//                }catch (WriterException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        content_code = (EditText)findViewById(R.id.content_code);
//        generate_code_map = (Button)findViewById(R.id.generate_codemap);
//        generate_code_map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String content = content_code.getText().toString(); //获取输入的信息，产生二维码
//                if(null == content || content.equals(""))
//                    Toast.makeText(FeedBackActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
//                else{
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    try{
//                        Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE,200,200);
//                        picture.setImageBitmap(bitmap);
//                    }catch (WriterException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
   /*     chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
            }
        });*/

    }


    /**
     * 自定义viewpager 滑动监听器
     * TODO
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
                FeedBackActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
