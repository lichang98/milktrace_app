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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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

import com.example.milktracesystem.Camera.BarcodeEncoder;
import com.example.milktracesystem.Factory_Acticity.Factory_USER.InfoInput;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.MainInterface.DialogForChooseImgMethod;
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

public class FeedBackActivity extends AppCompatActivity implements DialogForChooseImgMethod.NoticeDialogListener{
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=2;
    public static final int CHOOSE_PHOTO=3;
    private Button takePhoto;
    private Button generate_code_map;//生成二维码
    private Button bUpload;         //上传按钮
    private EditText content_code;
    private ImageView picture;          //显示拍照后的照片
    private Uri imageUri;
    private Uri imgTakePhotoUri;        //拍照时存储图片的uri
    private String imgTakePhotoPath;    //拍照时存储图片的路径
    //private Button chooseFromAlbum;
    private EditText feedback;      //反馈文本输入框
    private byte[] bytearray;

    private GridView gridView;          //网格缩略图
    private final int IMAGE_OPEN=4; //打开图片标记
    private String pathImage;       //选择图片路径
    private Bitmap bmp;
    private ArrayList<HashMap<String,Object>> imageItem;
    private SimpleAdapter simpleAdapter;        //适配器

    //界面修改后控件
    private EditText editTextFeedBackName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
//        takePhoto = (Button)findViewById(R.id.takephotos);  //拍照按钮
//        picture = (ImageView)findViewById(R.id.picture);        //显示拍照后的照片
        bUpload = (Button)findViewById(R.id.button3);       //上传按钮
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取控件对象
        gridView = (GridView)findViewById(R.id.gridView1);
        //载入默认图片加号
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.gridview_addpic);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("itemImage",bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,imageItem,R.layout.griditem_addpic,
                new String[]{"itemImage"},new int[] {R.id.imageView1});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if(view instanceof ImageView &&  o instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap)o);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
        //监听GridView点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(imageItem.size() == 10){
                    Toast.makeText(FeedBackActivity.this,"图片数量已满",Toast.LENGTH_LONG).show();
                }
                else if(position == 0){

                    //弹出选择图片方式的对话框
                    DialogForChooseImgMethod dialogForChooseImgMethod = new DialogForChooseImgMethod();
                    dialogForChooseImgMethod.show(getFragmentManager(),"选择照片的方式");

                    Toast.makeText(FeedBackActivity.this,"添加图片",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore
//                            .Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent,IMAGE_OPEN);
                }
                else{
                    dialog(position);
                }
            }
        });
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }
        //数据上传提交按钮监听
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picture.setDrawingCacheEnabled(true);      //打开drawing cache
                Bitmap bitmap = Bitmap.createBitmap(picture.getDrawingCache());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);  //将bitmap 压缩成png格式图像
                bytearray = byteArrayOutputStream.toByteArray();
            //尝试直接使用本地的png图片上传，排除问题点
           /*     final Drawable drawable = getResources().getDrawable(R.drawable.start);*/
           //     int bytes = bitmap.getByteCount();      //获取bitmap的字节数
            //    ByteBuffer byteBuffer = ByteBuffer.allocate(bytes);
             //   bitmap.copyPixelsToBuffer(byteBuffer);  //将bitmap 拷贝到bytebuffer

         /*       try{
         测试成功代码
                    final byte[] dt = drawable.toString().getBytes("utf-8");      //将byteBuffer转换为byte[]
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    picture.setImageBitmap(bitmap);     //测试成功，图片上传成功，不过不要在上传
                    //过程中更改图片
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    bytearray = byteArrayOutputStream.toByteArray();
                    Log.d("bytearry:",new String(bytearray));
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }*/

                //在 新线程中上传数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpUtil.postByOkHttp(HttpUtil.MEDIA_TYPE_PNG,bytearray,
//                                "http://192.168.1.111:8080/JsonAndroid/factories/MaterialFacServlet"
//                                ,HttpUtil.POST_FEEDBACK_BITMAP);
//                    }
//                }).start();
                picture.setDrawingCacheEnabled(false); //关闭drawing cache
            }
        });




    }


    /**
     * 用于选择照片方式的对话框的接口方法
     * @param dialog
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        Date date = new Date();
        String time = date.getMonth()+"_"+date.getDay()+"_"+date.getHours();

        imgTakePhotoPath = new String(getExternalCacheDir()+"/"+"material_check_upload"+time+".jpg");
        File outputImage = new File(getExternalCacheDir(),"material_check_upload"+time+".jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24){
            imgTakePhotoUri = FileProvider.getUriForFile(FeedBackActivity.this,"com.example.milktracesystem.fileprovider",outputImage);
        }else{
            imgTakePhotoUri = Uri.fromFile(outputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgTakePhotoUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    /**
     * 打开相册选择图片
     * @param dialog
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,IMAGE_OPEN);
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imgTakePhotoUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imgTakePhotoUri);
                    startActivityForResult(intent,CROP_PHOTO);      //照片裁剪
                }
                break;
            case CROP_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgTakePhotoUri));
                        pathImage = imgTakePhotoPath;       //拍照后图片存放的路径
//                        picture.setImageBitmap(bitmap);     //将照片放在picture ImageView中


                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT >= 19){
                        //4.4及以上系统使用这个方法
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下系统
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case IMAGE_OPEN:
                super.onActivityResult(requestCode, resultCode, data);
                //打开图片
                if(resultCode==RESULT_OK) {
                    Uri uri = data.getData();
                    if (!TextUtils.isEmpty(uri.getAuthority())) {
                        //查询选择图片
                        Cursor cursor = getContentResolver().query(
                                uri,
                                new String[] { MediaStore.Images.Media.DATA },
                                null,
                                null,
                                null);
                        //返回 没找到选择图片
                        if (null == cursor) {
                            return;
                        }
                        //光标移动至开头 获取图片路径
                        cursor.moveToFirst();
                        pathImage = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close(); //curson关闭
                    }
                }  //end if 打开图片
                break;
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
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

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
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
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"未能成功获取照片",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 网格上传图片方式
     */
    @Override
    protected  void onResume(){
        super.onResume();
        GridAddImage(); //GridView添加图片
    }
    protected  void GridAddImage(){
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("itemImage",addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,imageItem,R.layout.griditem_addpic,
                    new String[]{"itemImage"},new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
    * Dialog对话框提示用户删除操作
    * position为删除图片位置
    */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedBackActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
