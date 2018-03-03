package com.example.milktracesystem.Search_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.milktracesystem.Camera.SmallCaptureActivity;
import com.example.milktracesystem.News_Activity.OnWheelChangedListener;
import com.example.milktracesystem.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by 李畅 on 2017/5/6.
 */

/**
 * 查询功能
 */
public class SearchActivity extends AppCompatActivity  {
    public final static int ALBUM_REQUEST_CODE=1;
    public final static int CROP_REQUEST=2;
    public final static int CAMERA_REQUEST_CODE=3;
    public static String SAVED_IMAGE_DIR_PATH=Environment.getExternalStorageDirectory().getPath()
            +"/MilkTrace/camera/";
    String cameraPath;
    private Button chooseFromAlbum;
    private ImageButton takePhoto;
    private ImageView bar;
    private EditText category;
    private EditText barcodeinfo;
    private Button  chooseBarcode;


    private RecyclerView recyclerView;
    private List<SearchActivityCardItem> cardItemList;
    private SearchActivityCardAdapter cardAdapter;



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "扫描结果："+result.getContents(), Toast.LENGTH_LONG).show();
//                barcodeinfo.setText(result.getContents());
                //通过获取的网页的网址，通过intent发送的WebShow 活动，并在WebShow活动中显示
//                Intent intent = new Intent(SearchActivity.this,WebShow.class);
//                intent.putExtra("webUrl",result.getContents());     //将网址放在intent中传递
//                startActivity(intent);
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        takePhoto = (ImageButton)findViewById(R.id.scan_bar);
//        category = (EditText)findViewById(R.id.editCategory);
//        barcodeinfo = (EditText)findViewById(R.id.barcodeinfo); //显示二维码信息的文本框
//        chooseBarcode = (Button)findViewById(R.id.choose_barcode);
//        takePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentIntegrator integrator = new IntentIntegrator(SearchActivity.this);
//                integrator.setOrientationLocked(false);         //根据sensor调整方向
//                integrator.setCaptureActivity(SmallCaptureActivity.class);  //使用带边框的扫描框
//                integrator.initiateScan();
//            }
//        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }


        cardItemList = new ArrayList<>();
        cardItemList.add(new SearchActivityCardItem("扫描产品二维码",R.drawable.scan2));
        cardItemList.add(new SearchActivityCardItem("浏览企业信息",R.drawable.view_facs));

        recyclerView = (RecyclerView)findViewById(R.id.search_card_list);
        cardAdapter = new SearchActivityCardAdapter(cardItemList,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);

    }//onCreate


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
                SearchActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }


}
