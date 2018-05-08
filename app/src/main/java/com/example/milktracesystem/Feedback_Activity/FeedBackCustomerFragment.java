package com.example.milktracesystem.Feedback_Activity;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.milktracesystem.MainInterface.DialogForChooseImgMethod;
import com.example.milktracesystem.R;
import com.mob.MobSDK;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static android.app.Activity.RESULT_OK;

/***
 * 用于用户投诉产品的fragment
 *
 */

public class FeedBackCustomerFragment extends Fragment  implements View.OnClickListener{
    private Context context;

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
    private EditText editTextFeedBackPhone; //反馈人电话
    private EditText editTextFeedBackCheckNum;  //短信验证码填写
    private Button buttonFeedBackGetCheckNumButton; //获取短信验证码的按钮
    private final String APPKEY="258529026d387";    //短信验证的应用key
    private final String SECRETKEY="e8e691c8eb87a99e3d9c1f3ce1c16dc6";
    private EventHandler eventHandler;  //短信验证eventhandler
    private Button buttonFeedBackExpired;   //过期产品选择
    private RippleView rippleViewProductExpired;
    private Button buttonFeedBackSick;      //质量问题产品选择
    private RippleView rippleViewSick;
    private Button buttonFeedBackPrice; //关于产品价格问题的投诉
    private RippleView rippleViewPrice;

    private android.support.design.widget.TextInputEditText editTextFacSelect;  //投诉的企业名称
    private ImageButton imageButtonFacSelect;   //选择投诉的企业
    private OptionsPickerView optionsPickerViewFacSelect;

    private android.support.design.widget.TextInputEditText editTextBatchId;    //产品批次选择
    private ImageButton imageButtonBatchIdSelect;   //选择投诉产品的批次码
    private OptionsPickerView optionsPickerViewBatchId;
    private ArrayList<String> optionsItems1;
    private ArrayList<ArrayList<String>> optionsItems2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_customer,container,false);
        context = view.getContext();
        bUpload = (Button)view.findViewById(R.id.button3);       //上传按钮
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取控件对象
        gridView = (GridView)view.findViewById(R.id.gridView1);
        //载入默认图片加号
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.gridview_addpic);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("itemImage",bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(context,imageItem,R.layout.griditem_addpic,
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
                    Toast.makeText(context,"图片数量已满",Toast.LENGTH_LONG).show();
                }
                else if(position == 0){

                    //弹出选择图片方式的对话框
                    DialogForChooseImgMethod dialogForChooseImgMethod = new DialogForChooseImgMethod();
                    dialogForChooseImgMethod.show(getActivity().getFragmentManager(),"选择照片的方式");

                    Toast.makeText(context,"添加图片",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore
//                            .Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent,IMAGE_OPEN);
                }
                else{
                    dialog(position);
                }
            }
        });

        buttonFeedBackExpired = (Button)view.findViewById(R.id.feedback_expired_product_btn);
        buttonFeedBackExpired.setOnClickListener(this);
        rippleViewProductExpired = (RippleView)view.findViewById(R.id.feedback_ripple1);
        rippleViewProductExpired.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ColorDrawable colorDrawable = (ColorDrawable) buttonFeedBackExpired.getBackground();
                int cl = colorDrawable.getColor();
                if(cl == Color.parseColor("#9C9C9C")){
                    buttonFeedBackExpired.setBackgroundColor(Color.parseColor("#483D8B"));
                }else{
                    buttonFeedBackExpired.setBackgroundColor(Color.parseColor("#9C9C9C"));
                }

            }
        });

        buttonFeedBackSick = (Button)view.findViewById(R.id.feedback_sick_btn);
        buttonFeedBackSick.setOnClickListener(this);
        rippleViewSick = (RippleView)view.findViewById(R.id.feedback_ripple2);
        rippleViewSick.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ColorDrawable colorDrawable = (ColorDrawable)buttonFeedBackSick.getBackground();
                int color = colorDrawable.getColor();
                if(color == Color.parseColor("#9C9C9C")){
                    buttonFeedBackSick.setBackgroundColor(Color.parseColor("#483D8B"));
                }else{
                    buttonFeedBackSick.setBackgroundColor(Color.parseColor("#9C9C9C"));
                }
            }
        });

        buttonFeedBackPrice = (Button)view.findViewById(R.id.feedback_price_btn);
        buttonFeedBackPrice.setOnClickListener(this);
        rippleViewPrice = (RippleView)view.findViewById(R.id.feedback_ripple3);
        rippleViewPrice.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                ColorDrawable colorDrawable = (ColorDrawable)buttonFeedBackPrice.getBackground();
                int color = colorDrawable.getColor();
                if(color == Color.parseColor("#9C9C9C")){
                    buttonFeedBackPrice.setBackgroundColor(Color.parseColor("#483D8B"));
                }else{
                    buttonFeedBackPrice.setBackgroundColor(Color.parseColor("#9C9C9C"));
                }
            }
        });

        editTextFacSelect = (android.support.design.widget.TextInputEditText)
                view.findViewById(R.id.feedback_facselect_edittext);
        imageButtonFacSelect = (ImageButton)view.findViewById(R.id.feedback_facselect_imagebtn);
        imageButtonFacSelect.setOnClickListener(this);

        editTextBatchId = (android.support.design.widget.TextInputEditText)
                view.findViewById(R.id.feedback_batchid_edittext);
        imageButtonBatchIdSelect = (ImageButton)view.findViewById(R.id.feedback_batchid_imagebtn);
        imageButtonBatchIdSelect.setOnClickListener(this);


        bUpload.setOnClickListener(this);
        //数据上传提交按钮监听
//        bUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                picture.setDrawingCacheEnabled(true);      //打开drawing cache
//                Bitmap bitmap = Bitmap.createBitmap(picture.getDrawingCache());
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);  //将bitmap 压缩成png格式图像
//                bytearray = byteArrayOutputStream.toByteArray();
//                //尝试直接使用本地的png图片上传，排除问题点
//                /*     final Drawable drawable = getResources().getDrawable(R.drawable.start);*/
//                //     int bytes = bitmap.getByteCount();      //获取bitmap的字节数
//                //    ByteBuffer byteBuffer = ByteBuffer.allocate(bytes);
//                //   bitmap.copyPixelsToBuffer(byteBuffer);  //将bitmap 拷贝到bytebuffer
//
//         /*       try{
//         测试成功代码
//                    final byte[] dt = drawable.toString().getBytes("utf-8");      //将byteBuffer转换为byte[]
//                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    picture.setImageBitmap(bitmap);     //测试成功，图片上传成功，不过不要在上传
//                    //过程中更改图片
//                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
//                    bytearray = byteArrayOutputStream.toByteArray();
//                    Log.d("bytearry:",new String(bytearray));
//                }catch (UnsupportedEncodingException e){
//                    e.printStackTrace();
//                }*/
//
//                //在 新线程中上传数据
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        HttpUtil.postByOkHttp(HttpUtil.MEDIA_TYPE_PNG,bytearray,
////                                "http://192.168.1.111:8080/JsonAndroid/factories/MaterialFacServlet"
////                                ,HttpUtil.POST_FEEDBACK_BITMAP);
////                    }
////                }).start();
//                picture.setDrawingCacheEnabled(false); //关闭drawing cache
//            }
//        });

        //customer feedback上传图片或者是拍照选择对话框
        DialogForChooseImgMethod.noticeDialogListener= new DialogForChooseImgMethod.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                onMyDialogPositiveClick(dialog);  //调用本地的方法
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {
                onMyDialogNegativeClick(dialog);
            }
        };

        editTextFeedBackName = (EditText)view.findViewById(R.id.feedback_name_edit);
        editTextFeedBackPhone = (EditText)view.findViewById(R.id.feedback_phone_edit);
        editTextFeedBackCheckNum = (EditText)view.findViewById(R.id.feedback_check_num_edit);
        buttonFeedBackGetCheckNumButton = (Button)view.findViewById(R.id.feedback_check_num_get);
        MobSDK.init(context,APPKEY,SECRETKEY);      //短信验证初始化

        return view;
    }

    /**
     * 控件点击监听事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.feedback_check_num_get:       //获取短信验证码
                eventHandler = new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        if(result == SMSSDK.RESULT_COMPLETE){   //如果当前操作成功
                            if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                                SMSSDK.unregisterEventHandler(eventHandler);
                            }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                uiToast("验证码获取成功！");

                            }
                        }else{
                            uiToast("短信验证失败！");
                            ((Throwable)data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eventHandler);      //注册处理
                String phoneNum = editTextFeedBackPhone.getText().toString();
                if(phoneNum.trim().equals("")){
                    Toast.makeText(context, "请先输入手机号码", Toast.LENGTH_SHORT).show();
                    break;
                }
                SMSSDK.getVerificationCode("86",phoneNum,null); //获取短信验证码


                break;
            case R.id.button3:      //提交按钮
                String verificationCode = editTextFeedBackCheckNum.getText().toString();
                String phoneNum1 = editTextFeedBackPhone.getText().toString();
                SMSSDK.submitVerificationCode("86",phoneNum1,verificationCode); //提交验证


                break;
            case R.id.feedback_facselect_imagebtn:      //投诉的企业选择
                final ArrayList<String> optionsItems = new ArrayList<>();
                optionsItems.add("伊利");
                optionsItems.add("蒙牛");
                optionsItems.add("卫岗");
                optionsItems.add("光明");
                optionsPickerViewFacSelect = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        editTextFacSelect.setText(optionsItems.get(options1));
                    }
                }).setTitleText("选择投诉产品生产企业")
                        .setTitleSize(14)
                        .setTitleColor(Color.BLACK)
                        .isCenterLabel(false)
                        .isDialog(true)
                        .isRestoreItem(true)
                        .build();
                optionsPickerViewFacSelect.setPicker(optionsItems);
                optionsPickerViewFacSelect.show();

                break;
            case R.id.feedback_batchid_imagebtn:
                optionsItems1 = new ArrayList<>();
                optionsItems1.add("320102");
                optionsItems1.add("320104");
                optionsItems1.add("320105");
                optionsItems1.add("320106");
                optionsItems2 = new ArrayList<>();
                ArrayList<String> optionsItem21 = new ArrayList<>();
                optionsItem21.add("01213-011");
                optionsItem21.add("01213-012");
                optionsItem21.add("01213-013");
                optionsItem21.add("01213-014");
                optionsItem21.add("01213-015");

                ArrayList<String> optionsItem22 = new ArrayList<>();
                optionsItem22.add("01221-011");
                optionsItem22.add("01221-012");
                optionsItem22.add("01221-013");
                optionsItem22.add("01221-014");
                optionsItem22.add("01221-015");
                optionsItem22.add("01221-016");
                optionsItem22.add("01221-017");

                ArrayList<String> optionsItem23 = new ArrayList<>();
                optionsItem23.add("01222-012");
                optionsItem23.add("01222-013");
                optionsItem23.add("01222-014");
                optionsItem23.add("01222-015");
                optionsItem23.add("01222-016");
                optionsItem23.add("01222-017");

                ArrayList<String> optionsItem24 = new ArrayList<>();
                optionsItem24.add("01237-202");
                optionsItem24.add("01237-203");
                optionsItem24.add("01237-204");
                optionsItem24.add("01237-205");
                optionsItem24.add("01237-206");

                optionsItems2.add(optionsItem21);
                optionsItems2.add(optionsItem22);
                optionsItems2.add(optionsItem23);
                optionsItems2.add(optionsItem24);

                optionsPickerViewBatchId = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        editTextBatchId.setText(optionsItems1.get(options1)+"-"+optionsItems2.get(options1).get(options2));
                    }
                }).setTitleText("选择投诉的产品批次")
                        .setTitleSize(14)
                        .setTitleColor(Color.BLACK)
                        .isCenterLabel(false)
                        .isDialog(true)
                        .isRestoreItem(true)
                        .build();
                optionsPickerViewBatchId.setPicker(optionsItems1,optionsItems2);
                optionsPickerViewBatchId.show();

                break;
            default:
                break;


        }
    }

    /**
     * 用于在ui 线程上显示toast
     * @param msg
     */
    public void uiToast(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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


    /**
     * 网格上传图片方式
     */
    @Override
    public  void onResume(){
        super.onResume();
        GridAddImage(); //GridView添加图片
    }

    protected  void GridAddImage(){
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("itemImage",addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(context,imageItem,R.layout.griditem_addpic,
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



    /**
     * 用于选择照片方式的对话框的接口方法
     * @param dialog
     */

    public void onMyDialogPositiveClick(DialogFragment dialog) {
        Date date = new Date();
        String time = date.getMonth()+"_"+date.getDay()+"_"+date.getHours();

        imgTakePhotoPath = new String(context.getExternalCacheDir()+"/"+"material_check_upload"+time+".jpg");
        File outputImage = new File(context.getExternalCacheDir(),"material_check_upload"+time+".jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24){
            imgTakePhotoUri = FileProvider.getUriForFile(context,"com.example.milktracesystem.fileprovider",outputImage);
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
    public void onMyDialogNegativeClick(DialogFragment dialog) {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,IMAGE_OPEN);
    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
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
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imgTakePhotoUri));
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
                        Cursor cursor = getActivity().getContentResolver().query(
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
        if(DocumentsContract.isDocumentUri(context,uri)){
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
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
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
            Toast.makeText(context,"未能成功获取照片",Toast.LENGTH_LONG).show();
        }
    }
    //
//    @Override
//    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
//        final View view = inflater.inflate(R.layout.material_fragment,container,false);
////        InitMFac(); //初始化厂商数据
//        recyclerView = (RecyclerView)view.findViewById(R.id.materialfactories_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new MFactoryAdapter(factoryListBeanList);
//        recyclerView.setAdapter(adapter);
}
