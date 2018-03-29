package com.example.milktracesystem.Factory_Acticity.Factory_USER;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.milktracesystem.MainInterface.DialogForChooseImgMethod;
import com.example.milktracesystem.MainInterface.Register;
import com.example.milktracesystem.R;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.DatePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by 李畅 on 2017/9/23.
 */

/**
 * 用于企业用户输入产品的信息
 * 输入的产品以批次为单位
 * 上传的数据包括文字描述及图片
 * 之后更新，部分数据使用NFC获取
 */
public class InfoInput extends AppCompatActivity implements DialogForChooseImgMethod.NoticeDialogListener{
    private Spinner company_type_select;    //表单填写企业类别选择
    private View currAddedView;     //当前动态加载的布局
    private View[] tableViews;      //备选布局
    private int currPosition = -1;  //当前加载的布局


    private ImageView materialUploadImg = null;    //原料生产企业上传检验照片
    private Uri materialUploadImgUri;       //原料企业上传照片保存的路径

    private static final int TAKEPHOTO = 1;
    private static final int CHOOSE_PHOTO=2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_input);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_inputinfo);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
        InitSpinnerCompanyType();   //初始化Spinner，厂商类别选择
        setSpinnerOnItemClickListener();        //设置spinner项目点击的监听器，用于动态改变布局
        //初始化备选的添加TableView
        tableViews = new TableLayout[4];
        tableViews[0] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_material_table,null);
        tableViews[1] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_product_table,null);
        tableViews[2] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_transport_table,null);
        tableViews[3] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_sale_table,null);


    }


    @Override
    public void onDialogPositiveClick(android.app.DialogFragment dialog) {
        takePhoto();
    }

    @Override
    public void onDialogNegativeClick(android.app.DialogFragment dialog) {

        choosePhoto();
    }

    //在OptionMenu中显示图片
    private void setIconVisible(Menu menu, boolean flag) {
        if (menu != null) {
            try {
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.general_toolbar, menu);
        setIconVisible(menu, true);
        return true;
    }

    //菜单子项的操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }
    /**
     * 初始化表单填写的厂商类别选择的控件
     */
    private void InitSpinnerCompanyType(){
        //填写表单企业类别的适配器
        company_type_select = (Spinner)findViewById(R.id.company_type_edit);
        ArrayList<String> type_list = new ArrayList<>();
        type_list.add("原料厂商");
        type_list.add("乳制品生产商");
        type_list.add("物流运输");
        type_list.add("零售批发");
        //适配器
        ArrayAdapter<String> company_type_adapter = new ArrayAdapter<String>(this,android.R.layout
                .simple_spinner_dropdown_item,type_list);
        //设置样式
        company_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        company_type_select.setAdapter(company_type_adapter);
    }

    /**
     * 用于设置spinner 选项的监听，根据选择的厂商的类型，动态的改变布局，显示
     * 相应的字段
     */
    public void setSpinnerOnItemClickListener(){
        this.company_type_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //position 从0开始
                TableLayout rootView = (TableLayout)findViewById(R.id.company_fill_table);    //TableLayout
                if(currPosition != -1){     //当前有已加载的布局，删除
                    rootView.removeView(currAddedView);
                }
                switch(position){
                    case 0:             //加载原料厂商的布局
                        currPosition=0;
                        rootView.addView(tableViews[0]);
                        currAddedView = tableViews[0];
                        materialFacSolve();     //开始对原料企业的操作的处理
                        break;
                    case 1:             //加载乳制品生产企业的布局
                        currPosition=1;
                        rootView.addView(tableViews[1]);
                        currAddedView = tableViews[1];
                        productFacSolve();      //开始对乳制品生产企业的操作的处理
                        break;
                    case 2:             //加载物流运输企业的布局
                        currPosition=2;
                        rootView.addView(tableViews[2]);
                        currAddedView = tableViews[2];
                        break;
                    case 3:             //加载零售企业的布局
                        currPosition=3;
                        rootView.addView(tableViews[3]);
                        currAddedView = tableViews[3];
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 针对原料企业的所涉及的字段及操作的处理
     */
    public void materialFacSolve(){

        Button buttonMaterialImgUpload = (Button)findViewById(R.id.material_infoInput_imgupload);   //原料企业布局中的上传检验照片的按钮
        buttonMaterialImgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForChooseImgMethod dialogForChooseImgMethod = new DialogForChooseImgMethod();
                dialogForChooseImgMethod.show(getFragmentManager(),"选择照片的方式");
            }
        });
        //TODO

    }

    /**
     * 针对乳制品生产企业所涉及的字段的操作的处理
     */
    public void productFacSolve(){

        ImageButton imageButtonDateSelect = (ImageButton)findViewById(R.id.date_select_bt);     //生产日期选择按钮
        ImageButton imageButtonOutFacDateSelect = (ImageButton)findViewById(R.id.prod_outfac_checktime_select); //出厂检验时间选择
        imageButtonDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.Builder builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment){

                        DatePickerDialog datePickerDialog = (DatePickerDialog)fragment.getDialog();
                        String date = datePickerDialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                        Toast.makeText(InfoInput.this, "Date is " + date, Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment){
                        Toast.makeText(InfoInput.this, "取消", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("确定")
                        .negativeAction("取消");
                DialogFragment dialogFragmentMaterial = DialogFragment.newInstance(builder);
                dialogFragmentMaterial.show(getSupportFragmentManager(),null);
            }
        });

        //出厂检验时间选择
        imageButtonOutFacDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog.Builder builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment){

                        DatePickerDialog datePickerDialog = (DatePickerDialog)fragment.getDialog();
                        String date = datePickerDialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                        Toast.makeText(InfoInput.this, "Date is " + date, Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment){
                        Toast.makeText(InfoInput.this, "取消", Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("确定")
                        .negativeAction("取消");
                DialogFragment dialogFragmentMaterial = DialogFragment.newInstance(builder);
                dialogFragmentMaterial.show(getSupportFragmentManager(),null);

            }
        });

    }


    /**
     * 启动相机拍照
     */
    public void takePhoto(){
        File outputImage = new File(getExternalCacheDir(),"material_check_upload.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

//        if(Build.VERSION.SDK_INT >= 24){
//            materialUploadImgUri = FileProvider.getUriForFile(InfoInput.this,"com.example.milktracesystem.fileprovider",outputImage);
//        }else{
//            materialUploadImgUri = Uri.fromFile(outputImage);
//        }
        materialUploadImgUri = Uri.fromFile(outputImage);
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,materialUploadImgUri);
        startActivityForResult(intent,TAKEPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(materialUploadImg == null)
            materialUploadImg = (ImageView)findViewById(R.id.material_upload_img);
        switch(requestCode){
            case TAKEPHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Toast.makeText(this, "获取到图片的路径：" + materialUploadImgUri, Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(materialUploadImgUri));
                        //测试
                        com.rengwuxian.materialedittext.MaterialEditText dirresult =
                                (com.rengwuxian.materialedittext.MaterialEditText)findViewById(R.id.company_name_edit);
                        dirresult.setText(materialUploadImgUri.toString());

                        materialUploadImg.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }

                break;

            case CHOOSE_PHOTO:

                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 选择照片
     * @param data
     */
    @TargetApi(19)
    public void handleImageOnKitKat(Intent data){

        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];        //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果不是document类型的id,则使用普通的方法
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片的路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            materialUploadImg.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"未能成功获取照片",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 选择照片上传
     */
    public void choosePhoto(){

        if(ContextCompat.checkSelfPermission(InfoInput.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(InfoInput.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            openAlbum();
        }
    }

    public void openAlbum(){

        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){

            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this, "你已取消选择照片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }



}