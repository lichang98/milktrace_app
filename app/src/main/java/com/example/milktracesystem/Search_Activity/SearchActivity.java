package com.example.milktracesystem.Search_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.milktracesystem.News_Activity.OnWheelChangedListener;
import com.example.milktracesystem.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Method;
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

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "扫描结果："+result.getContents(), Toast.LENGTH_LONG).show();
                barcodeinfo.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        takePhoto = (ImageButton)findViewById(R.id.scan_bar);
        category = (EditText)findViewById(R.id.editCategory);
        barcodeinfo = (EditText)findViewById(R.id.barcodeinfo); //显示二维码信息的文本框
        chooseBarcode = (Button)findViewById(R.id.choose_barcode);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(SearchActivity.this).initiateScan();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }




        chooseBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建会话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this,
                        R.style.AlertDialogCustom);
                final AlertDialog dialog = builder.create();
                dialog.setTitle("编码选择：");
                //创建布局
                final LinearLayout linearLayout = new LinearLayout(SearchActivity.this);

                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                final WheelView category1 = new WheelView(SearchActivity.this);
                category1.setVisibleItems(5);

                category1.setCyclic(true);
                final CategoryItem item = new CategoryItem();
                category1.setAdapter(new ArrayWheelAdapter<String>(item.category_str1));
                final WheelView category2 = new WheelView(SearchActivity.this);
                category2.setVisibleItems(5);
                category2.setCyclic(true);
                category2.setAdapter(new ArrayWheelAdapter<String>(item.category_str2[0]));
                //创建参数
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.height = 500;          //设置高度
                layoutParams.weight = 1;

                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                // layoutParams2.weight = (float)0.6;
                layoutParams2.gravity = Gravity.RIGHT;
                layoutParams2.leftMargin = 10;
                layoutParams2.height = 500;
                layoutParams2.weight = 1;
                linearLayout.addView(category1,layoutParams);
                linearLayout.addView(category2,layoutParams2);
                //为category1添加监听
                category1.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheel, int oldValue, int newValue) {
                        category2.setAdapter(new ArrayWheelAdapter<String>(item.category_str2[newValue]));
                        category2.setAdapter(new ArrayWheelAdapter<String>(item.category_str2[newValue]));
                        category2.setCurrentItem(item.category_str2[newValue].length/2);
                    }
                });
                //为会话创建确定按钮
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cat1 = item.category_str1[category1.getCurrentItem()];
                        String cat2 = item.category_str2[category1.getCurrentItem()]
                                [category2.getCurrentItem()];
                        category.setText(cat1+" - " +cat2);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.setView(linearLayout);
                builder.setView(linearLayout);
                builder.show();
            }
        });



   /*    takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  //             Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //            startActivityForResult(intent,1);
                //普通拍照功能实现
             String state = Environment.getExternalStorageState();
                if(state.equals(Environment.MEDIA_MOUNTED)){
                    cameraPath = SAVED_IMAGE_DIR_PATH+System.currentTimeMillis()+".png";
                    Intent intent = new Intent();

                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    String out_file_path = SAVED_IMAGE_DIR_PATH;
                    File dir = new File(out_file_path);
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    Uri uri = Uri.fromFile(new File(cameraPath));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                    startActivityForResult(intent,CAMERA_REQUEST_CODE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"请确认已插入SD卡",Toast.LENGTH_SHORT).show();
                }

            }
        });*/

   //测试打开webview
        Button startWebView = (Button)findViewById(R.id.start_webview);
        startWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,WebShow.class);
                startActivity(intent);
            }
        });

    }//onCreate

    /**
     * 滚轮控件条目
     */
    class CategoryItem{
        public String category_str1[] = new String[]{"0001","0003","0005","0007"};
        public String category_str2[][] = new String[][]{
                new String[]{"ZY001","ZT291","FT45"},
                new String[]{"MS929","JK818","PL991"},
                new String[]{"MG91","GV811","BH12"},
                new String[]{"CV910","HU810","LO911"}
        };
    }
 /*  @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE)
                Log.d("camera path","path="+cameraPath);
        }
    }

    public static  String startCamera(Activity activity,int requestCode){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if(!outDir.exists()){
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis()+".jpg");
            Uri uri = Uri.fromFile(outFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
            activity.startActivityForResult(intent,requestCode);
            return outFile.getAbsolutePath();
        }
        else{
            Toast.makeText(activity,"请确认插入SD卡",Toast.LENGTH_SHORT).show();
            return null;
        }
    }*/

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
