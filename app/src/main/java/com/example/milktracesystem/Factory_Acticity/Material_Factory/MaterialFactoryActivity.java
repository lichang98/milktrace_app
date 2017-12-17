package com.example.milktracesystem.Factory_Acticity.Material_Factory;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.MaterialDesignComps.materialedittext.floatbutton.PromotedActionsLibrary;
import com.example.milktracesystem.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 李畅 on 2017/4/26.
 * 具体工厂信息填写界面
 */

public class MaterialFactoryActivity extends AppCompatActivity {
    //测试与MySQL数据库的直连
 /* private String db_url = "jdbc:mysql://localhost:3306/db_design?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false";
    private String user = "root";
    private String password="mysql";*/
    public LocationClient locationClient;     //位置
    private TextView position;      //显示位置
    private StringBuilder current_position;
    private MapView mapView;        //地图显示
    private ImageView cow_img;      //奶牛图片

    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;



    private String factory_name;
    private String address;
    private String material;
    private int amount;
    private String time;
    private String phone;
    private CreateTable createTable;        //创建工厂信息表
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener() );
        LocationClientOption option = new LocationClientOption();
        //设置是否需要获取地址信息，文本显示
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
         SDKInitializer.initialize(getApplicationContext()); //百度地图初始化，必须要在setContentView 之前！！
        setContentView(R.layout.materialfactory_layout);
        mapView = (MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();    //获取baiduMap实例，对baidumap进行操作
        baiduMap.setMyLocationEnabled(true);        //显示自己的位置
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,
                true,null));

        cow_img = (ImageView)findViewById(R.id.cow_img);    //获取cowimg 实例
        Intent intent = getIntent();
        factory_name = intent.getStringExtra("factory_name");
        EditText factory_name_text = (EditText)findViewById(R.id.editText);
        factory_name_text.setText(factory_name);
        final EditText editText1 = (EditText)findViewById(R.id.editText);     //获取文本输入组件
        final EditText editText2 = (EditText)findViewById(R.id.editText2);
        final EditText editText3 = (EditText)findViewById(R.id.editText3);
        final EditText editText4 = (EditText)findViewById(R.id.editText4);
        final EditText editText5 = (EditText)findViewById(R.id.editText5);
        final EditText editText6 = (EditText)findViewById(R.id.editText6);
        editText1.setFocusable(false);          //不可编辑状态
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }

        //创建提交按钮点击事件
        Button button = (Button)findViewById(R.id.button);  //提交按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的值
                try {
                    factory_name = editText1.getText().toString();
                    address = editText2.getText().toString();
                    material = editText3.getText().toString();
                    byte[] facname=factory_name.getBytes("ISO-8859-1");
                    byte[] add = address.getBytes("ISO-8859-1");
                    byte[] mater=material.getBytes("ISO-8859-1");
                    factory_name = new String(facname,"UTF-8");
                    address = new String (add,"UTF-8");
                    material=new String (mater,"UTF-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                amount=Integer.parseInt(editText4.getText().toString());    //整数
                time=editText5.getText().toString();
                phone=editText6.getText().toString();
                //创建表
                createTable = new CreateTable(MaterialFactoryActivity.this,"Factory_info.db",null,1);
                SQLiteDatabase db = createTable.getWritableDatabase();  //创建
                ContentValues values = new ContentValues();
                //数据入表
                values.put("factoryname",factory_name);
                values.put("address",address);
                values.put("material",material);
                values.put("amount",amount);
                values.put("time",time);
                values.put("phone",phone);
                db.insert("factory_info",null,values);  //插入数据*/
                Toast.makeText(MaterialFactoryActivity.this,"Save data succeed",Toast.LENGTH_SHORT).show();
            }
        });

        //获取位置信息

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MaterialFactoryActivity.this, Manifest.permission
        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MaterialFactoryActivity.this,Manifest.permission
        .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MaterialFactoryActivity.this,Manifest.permission
        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MaterialFactoryActivity.this,permissions,1);
        }else{
            requestLocation();
        }
        //百度地图点击事件
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MaterialFactoryActivity.this, "经纬度("+
                        latLng.latitude+","+latLng.longitude+")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Toast.makeText(MaterialFactoryActivity.this, "位置信息："+mapPoi.getName()
                        +"经纬度：("+mapPoi.getPosition().longitude+","+
                        mapPoi.getPosition().latitude+")", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //从Servlet获取数据
        //图片和文字的同时获取的测试成功
        //测试成功
        String requestUrl = "http://192.168.1.111:8080/JsonAndroid/factories/MaterialFacServlet";
        //获取servlet的数据并将数据解析，在UI Thread上将数据写入文本框
        HttpUtil.sendOkHttpRequest(requestUrl, "json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json_data = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MaterialFactoryActivity.this, "jsondata :" + json_data, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        //获取图片

        HttpUtil.sendOkHttpRequest(requestUrl ,"",new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            //直接使用Glide url方式获取Servlet的数据成功
            //服务器端使用OutputStream
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final URL url = new URL("http://192.168.1.111:8080/JsonAndroid/factories/MaterialFacServlet");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MaterialFactoryActivity.this)
                        .load(url)
                        .into(cow_img);
                    }
                });
            }
        });



        //显示悬浮按钮
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.floating_button_frame);//用于存储悬浮按钮的frame
        PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();   //使用库
        promotedActionsLibrary.setup(getApplicationContext(),frameLayout);
        //定义悬浮按钮显示的图标以及点击事件
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.edit),setFloatingButtonListener(0));
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.send),setFloatingButtonListener(1));
        promotedActionsLibrary.addItem(getResources().getDrawable(R.drawable.update),setFloatingButtonListener(2));
        //主按钮
        promotedActionsLibrary.addMainItem(getResources().getDrawable(R.drawable.open));

    }

    /**
     * 设置悬浮按钮的点击事件
     * @param id  点击按钮的位置
     */
    public View.OnClickListener setFloatingButtonListener(int id){
        //TODO
        return null;
    }

    private void navigateTo(BDLocation location){
        if(isFirstLocate){          //防止多次调用
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude()); //获取，存放经纬度值
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);        //显示精度
            baiduMap.animateMapStatus(update);              //移动至指定的位置
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.direction(location.getDirection());
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);   //将数据显示在地图上
    }

   @Override
     protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
    }
    //实时更新当前位置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//高精度
        int span = 1000;
        option.setScanSpan(span);   //定位请求间隔1s
        option.setIgnoreKillProcess(false); //stop 时kill进程
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        mapView = null;
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才可以使用本程序！",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            //文本显示方式
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude())
                    .append("\n");
            currentPosition.append("经度：").append(location.getLongitude())
                    .append("\n");
            currentPosition.append("国家：").append(location.getCountry())
            .append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if(location.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络\n");
            }
            current_position = new StringBuilder(currentPosition);

          // Log.i("The Location info:",currentPosition.toString());
            MaterialFactoryActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    position = (TextView)findViewById(R.id.position);
                    position.setText(current_position);
                }
            });
            //地图显示方式
          if(location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }


        }

        /**
         * 是否使用了热点
         * @param s  Connect Wifi Mac
         * @param i     Wifi State
         */
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            switch(i){
                case LocationClient.CONNECT_HOT_SPOT_TRUE:
                    Toast.makeText(MaterialFactoryActivity.this, "已连接WIFI", Toast.LENGTH_SHORT).show();
                    break;

                case LocationClient.CONNECT_HOT_SPOT_FALSE:
                    Toast.makeText(MaterialFactoryActivity.this, "未连接WIFI", Toast.LENGTH_SHORT).show();
                    break;
                case LocationClient.CONNECT_HOT_SPOT_UNKNOWN:
                    Toast.makeText(MaterialFactoryActivity.this, "连接WIFI状态未知", Toast.LENGTH_SHORT).show();
                    break;
            }
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
                MaterialFactoryActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }



}
