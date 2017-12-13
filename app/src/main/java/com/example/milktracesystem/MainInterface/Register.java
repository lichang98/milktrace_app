package com.example.milktracesystem.MainInterface;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.DialogInterface;
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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.milktracesystem.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by 李畅 on 2017/12/11.
 */

/**
 * 用于注册界面
 * 用于选择用户的类型，消费者、原料企业、生产企业、运输企业、销售企业
 */
public class Register extends AppCompatActivity implements DialogForChooseImgMethod.NoticeDialogListener{

    private RadioGroup radioGroup;
    private RadioButton radioButtonCustomer,
    radioButtonMaterial,radioButtonProduct,radioButtonTransport,
    radioButtonSale;    //代表不同用户类型的按钮
    private Button buttonUploadImg;         //企业注册用户上传资质照片的点击按钮
    private ImageView imgUpload;
    private Uri imgUri;
    private Button buttonRegisterSubmit;    //TODO 注册用户信息提交按钮,处理提交

    private static UserType USERTYPE;       //通过用户的注册选择不同的用户类型

    private static final int TAKEPHOTO = 1;
    private static final int CHOOSE_PHOTO=2;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }

        initRadios(savedInstanceState);

        imgUpload = (ImageView)findViewById(R.id.register_uploadimg);
        buttonUploadImg = (Button)findViewById(R.id.button5);
        buttonUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogForChooseImgMethod dialogForChooseImgMethod = new DialogForChooseImgMethod();
                dialogForChooseImgMethod.show(getFragmentManager(),"选择照片的方式");
            }
        });

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        takePhoto();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        choosePhoto();
    }

    /**
     * 启动相机拍照
     */
    public void takePhoto(){
        File outputImage = new File(getExternalCacheDir(),"register_upload.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24){
            imgUri = FileProvider.getUriForFile(Register.this,"com.example.milktracesystem.fileprovider",outputImage);
        }else{
            imgUri = Uri.fromFile(outputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
        startActivityForResult(intent,TAKEPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        switch(requestCode){
            case TAKEPHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                        imgUpload.setImageBitmap(bitmap);
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
            imgUpload.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"未能成功获取照片",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始按钮组件
     */
    public void initRadios(final Bundle savedInstanceState){

        radioButtonCustomer = (RadioButton)findViewById(R.id.radioButton);
        radioButtonMaterial = (RadioButton)findViewById(R.id.radioButton3);
        radioButtonProduct = (RadioButton)findViewById(R.id.radioButton4);
        radioButtonTransport = (RadioButton)findViewById(R.id.radioButton5);
        radioButtonSale = (RadioButton)findViewById(R.id.radioButton6);

        radioGroup = (RadioGroup)findViewById(R.id.register_radiogroup);

        //设置按钮组选择监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Toast.makeText(Register.this, "you have click " + i, Toast.LENGTH_SHORT).show();
                //如果选择的是消费者类型，弹出简单注册对话框
                switch(i){
                    case R.id.radioButton:
                        CustomerDialog customerDialog = new CustomerDialog();
                        customerDialog.show(getFragmentManager(),"customer register");
                        USERTYPE = UserType.CUSTOMER;
                        break;
                    case R.id.radioButton3:

                        USERTYPE = UserType.MATERIALFAC;
                        break;
                    case R.id.radioButton4:
                        USERTYPE = UserType.PRODUCTFAC;
                        break;
                    case R.id.radioButton5:
                        USERTYPE = UserType.TRANSPORTFAC;
                        break;
                    case R.id.radioButton6:
                        USERTYPE = UserType.SALEFAC;
                        break;
                }


            }
        });

    }

    /**
     * 选择照片上传
     */
    public void choosePhoto(){

        if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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


    /**
     * 用户类型
     */
    enum UserType{

        CUSTOMER,MATERIALFAC,PRODUCTFAC,TRANSPORTFAC,SALEFAC    //不同的用户类型
    }

}
