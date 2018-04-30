package com.example.milktracesystem.Factory_Acticity.Sales_Factory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
import com.example.milktracesystem.MaterialDesignComps.materialedittext.floatbutton.PromotedActionsLibrary;
import com.example.milktracesystem.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class SalesFactoryActivity extends AppCompatActivity {
    private String salesfacname;
    private String productboughtname;
    private int boughtamount;
    private String boughttime;
    private String freshtill;
    private String phonenumber;

    private LocationClient locationClient;  //位置
    private TextView position;      //显示位置
    private StringBuilder current_position;
    private MapView mapView;

    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        //设置是否需要获取地址信息，文本显示
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        SDKInitializer.initialize(getApplicationContext()); //百度地图初始化，必须要在setContentView 之前！！
        setContentView(R.layout.salesfactory_form);
        mapView = (MapView)findViewById(R.id.sales_bdmap_view);
        baiduMap = mapView.getMap();    //获取baiduMap实例，对baidumap进行操作
        baiduMap.setMyLocationEnabled(true);        //显示自己的位置
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,
                true,null));

        EditText editText = (EditText)findViewById(R.id.editText26);
        EditText editText1 = (EditText)findViewById(R.id.editText27);
        EditText editText2= (EditText)findViewById(R.id.editText28);
        EditText editText3 = (EditText)findViewById(R.id.editText29);
        EditText editText4 = (EditText)findViewById(R.id.editText30);
        EditText editText5 = (EditText)findViewById(R.id.editText31);

        salesfacname = getIntent().getStringExtra("sales_fac_name");    //获取传递的名称参数
        editText.setText(salesfacname);
        editText.setFocusable(false);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }

        //获取位置信息

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(SalesFactoryActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(SalesFactoryActivity.this,Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(SalesFactoryActivity.this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SalesFactoryActivity.this,permissions,1);
        }else{
            requestLocation();
        }
        //百度地图点击事件
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(SalesFactoryActivity.this, "经纬度("+
                        latLng.latitude+","+latLng.longitude+")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Toast.makeText(SalesFactoryActivity.this, "位置信息："+mapPoi.getName()
                        +"经纬度：("+mapPoi.getPosition().longitude+","+
                        mapPoi.getPosition().latitude+")", Toast.LENGTH_SHORT).show();
                return true;
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
    public View.OnClickListener setFloatingButtonListener(final int id){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SalesFactoryActivity.this, "你点击的悬浮按钮的id 为： " + id, Toast.LENGTH_SHORT).show();
                //TODO
            }
        };
        return onClickListener;
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
                SalesFactoryActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
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
            SalesFactoryActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    position = (TextView)findViewById(R.id.sales_fac_position);
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
                    Toast.makeText(SalesFactoryActivity.this, "已连接WIFI", Toast.LENGTH_SHORT).show();
                    break;

                case LocationClient.CONNECT_HOT_SPOT_FALSE:
                    Toast.makeText(SalesFactoryActivity.this, "未连接WIFI", Toast.LENGTH_SHORT).show();
                    break;
                case LocationClient.CONNECT_HOT_SPOT_UNKNOWN:
                    Toast.makeText(SalesFactoryActivity.this, "连接WIFI状态未知", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }











}
