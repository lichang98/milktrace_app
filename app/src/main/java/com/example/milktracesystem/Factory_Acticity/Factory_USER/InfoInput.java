package com.example.milktracesystem.Factory_Acticity.Factory_USER;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.milktracesystem.MainInterface.DialogForChooseImgMethod;
import com.example.milktracesystem.MainInterface.Register;
import com.example.milktracesystem.R;
import com.lantouzi.wheelview.WheelView;
import com.mob.MobSDK;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.DatePicker;
import com.suke.widget.SwitchButton;
import com.xw.repo.BubbleSeekBar;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by 李畅 on 2017/9/23.
 */

/**
 * 用于企业用户输入产品的信息
 * 输入的产品以批次为单位
 * 上传的数据包括文字描述及图片
 * 之后更新，部分数据使用NFC获取
 */
public class InfoInput extends AppCompatActivity implements DialogForChooseImgMethod.NoticeDialogListener,
        View.OnClickListener,DateSelectDialog.Callback,BubbleSeekBar.OnProgressChangedListener,com.suke.widget.SwitchButton.OnCheckedChangeListener{
//    private Spinner company_type_select;    //表单填写企业类别选择
    private View currAddedView;     //当前动态加载的布局
    private View[] tableViews;      //备选布局
    private int currPosition = -1;  //当前加载的布局

    private ImageView materialUploadImg = null;    //原料生产企业上传检验照片
    private Uri materialUploadImgUri;       //原料企业上传照片保存的路径

    private static final int TAKEPHOTO = 1;
    private static final int CHOOSE_PHOTO=2;

    private android.support.design.widget.TextInputEditText facNameInputText;
    private ImageButton facNameSelect;
    private ArrayList<String> facTypeOptions;   //企业类别选择
    private ArrayList<ArrayList<String>> facNameOptions;    //企业名称选择
    private OptionsPickerView optionsPickerViewFac;     //企业选择picker

    private android.support.design.widget.TextInputEditText headpersonInputText;    //企业法人填写

    private android.support.design.widget.TextInputEditText facAddrInputText;   //企业地址填写
    private ImageButton facAddrSelectBtn;
    private ArrayList<String> facAddrCity;
    private ArrayList<ArrayList<String>> facAddrZone;
    private OptionsPickerView optionsPickerViewFacAddr; //企业地址picker

    private info.hoang8f.android.segmented.SegmentedGroup segmentedGroupFacType;    //企业类型选择

    //原料企业部分控件
    com.lantouzi.wheelview.WheelView wheelViewMaterialVolumn;   //原料企业供货量选择
    android.support.design.widget.TextInputEditText materialInfoInputTextTargetCorp;    //原料企业收货方企业名称
    private OptionsPickerView optionsPickerViewMaterialReceiver;
    private com.suke.widget.SwitchButton switchButtonMaterialCheckConclusion;   //原料出场检验结论
    private android.support.design.widget.TextInputEditText textInputEditTextMaterialOutTime;   //出场时间
    private ImageButton imageButtonOutTime; //出场时间选择按钮

    //乳制品生产企业部分控件
    private com.lantouzi.wheelview.WheelView wheelViewProductMaterialUse;   //原奶使用量
    private android.support.design.widget.TextInputEditText textInputEditTextProductTime;   //出场时间
    private ImageButton imageButtonProductTime; //产品生产时间
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProductItem1;    //配料1
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProductItem2;    //配料2
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProductItem3;
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProductItem4;
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProductItem5;
    private ImageButton imageButtonProductRoom;     //产品生产车间选择
    private ArrayList<String> productFacRoom;
    private ArrayList<ArrayList<String>> productWorkRoom;
    private OptionsPickerView optionsPickerViewProductRoom; //产品生产车间选择对话框
    private android.support.design.widget.TextInputEditText editTextProductRoom;
    private Button buttonProductImageUpload;        //点击选择生产验证表单的按钮
    private ImageView imageViewProductImage;        //上传的生产图片
    private Button buttonProductFormUpload;         //表单上传按钮
    private Uri uriProductImageUpload;      //用于存储生产企业上传图片的路径

    //物流运输企业
    private android.support.design.widget.TextInputEditText textInputEditTextDriverPhone;   //填写手机号码的输入框
    private android.support.design.widget.TextInputEditText textInputEditTextDriverPhoneCheck;  //填写验证码的输入框
    private Boolean isGetCertificateCode;   //判断是否已经获取到短信验证码
    private int     DOWNTIME=60;    //倒计时
    private Timer timer;
    private TimerTask timerTask;
    private Button buttonDriverPhoneCheck; //获取验证码的按钮
    private final String APPKEY="25a7fab0d14c9";    //短信验证的应用key
    private final String SECRETKEY="801db392b70c9bc037923702ca9779bb";
    private EventHandler eventHandler;  //用于短信验证的回调处理
    private Button buttonTransportSubmit;   //物流运输企业验证表单提交按钮

    //零售企业
    private android.support.design.widget.TextInputEditText textInputEditTextSaleOrg;   //零售企业选择
    private ImageButton imageButtonSaleOrgSelect;   //零售企业选择点击按钮
    private OptionsPickerView optionsPickerViewSaleCorp;

    private android.support.design.widget.TextInputEditText textInputEditTextBatchId;   //零售企业批次码选择
    private ImageButton imageButtonBatchIdSelect;   //零售企业批次码选择
    private OptionsPickerView optionsPickerViewBatchId; //批次码选择
    private ArrayList<String> optionsItems1;
    private ArrayList<ArrayList<String>> optionsItems2;

    private android.support.design.widget.TextInputEditText textInputEditTextInProductTime;//进货时间选择
    private ImageButton imageButtonInProductTimeSelect; //进货时间选择按钮
    private com.suke.widget.SwitchButton switchButtonProduct1;  //产品1是否进购的选择控件
    private com.suke.widget.SwitchButton switchButtonProduct2;//产品是否进购的选择控件
    private com.suke.widget.SwitchButton switchButtonProduct3;//产品是否进购的选择控件
    private com.suke.widget.SwitchButton switchButtonProduct4;//产品是否进购的选择控件
    private com.suke.widget.SwitchButton switchButtonProduct5;//产品是否进购的选择控件
    private com.suke.widget.SwitchButton switchButtonProduct6;//产品是否进购的选择控件
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct1;    //产品1的进购量
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct2; //产品2的进购量
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct3; //产品3的进购量
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct4; //产品4的进购量
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct5; //产品5的进购量
    private com.xw.repo.BubbleSeekBar bubbleSeekBarProduct6; //产品6的进购量

    private android.support.design.widget.TextInputEditText textInputEditTextContactPersonName; //联系人姓名
    private android.support.design.widget.TextInputEditText textInputEditTextContactPersonPhone;    //联系人电话
    private android.support.design.widget.TextInputEditText textInputEditTextCertificateCode;   //验证码
    private Button buttonGetCertificateCode;    //获取短信验证码button
    private EventHandler eventHandlerSale;  //零售企业短信验证回调处理
    private Button buttonSaleUpload;    //零售企业表单上传



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
        //FIXME
//        setSpinnerOnItemClickListener();        //设置spinner项目点击的监听器，用于动态改变布局
        //初始化备选的添加TableView
        MobSDK.init(this,APPKEY,SECRETKEY);    //初始化短信验证接口
        tableViews = new RelativeLayout[4];
        tableViews[0] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_material_table,null);
        tableViews[1] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_product_table,null);
        tableViews[2] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_transport_table,null);
        tableViews[3] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_sale_table,null);

        facNameInputText = (android.support.design.widget.TextInputEditText)
                findViewById(R.id.facname_input_text);  //企业名称选择
        facNameSelect = (ImageButton)findViewById(R.id.facname_input_btn);
        facNameSelect.setOnClickListener(this);

        headpersonInputText = (android.support.design.widget.TextInputEditText)findViewById(R.id.head_person_input_text);

        facAddrInputText = (android.support.design.widget.TextInputEditText)findViewById(R.id.facaddr_input_text);
        facAddrSelectBtn = (ImageButton)findViewById(R.id.facaddr_select_btn);
        facAddrSelectBtn.setOnClickListener(this);

        segmentedGroupFacType = (info.hoang8f.android.segmented.SegmentedGroup)findViewById(R.id.factype_segment);
        segmentedGroupFacType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //企业类型选择监听器
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.facTypeBtn1:
                        dynamicLoadView(0);
                        Toast.makeText(InfoInput.this, "选择了原料企业", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.facTypeBtn2:
                        dynamicLoadView(1);
                        Toast.makeText(InfoInput.this, "选择了生产企业", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.facTypeBtn3:
                        dynamicLoadView(2);
                        Toast.makeText(InfoInput.this, "选择了物流企业", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.facTypeBtn4:
                        dynamicLoadView(3);
                        Toast.makeText(InfoInput.this, "选择了零售企业", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 用于动态加载后面不同的布局
     * @param position  加载的布局在views 数组中的下标
     */
    public void dynamicLoadView(int position){
        if(position == currPosition)
            return;
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.info_input_rootlayout);

        if(currAddedView != null)       //先删除布局
            linearLayout.removeView(currAddedView);
        linearLayout.addView(tableViews[position]);
        currPosition = position;
        currAddedView = tableViews[position];
        initPartView(position);
    }

    /**
     * 用于初始化动态记载后的view 控件的控制
     * @param position
     */
    public void initPartView(final int position){
        switch (position){
            case 0:

                //原料供应企业处理
                wheelViewMaterialVolumn = (com.lantouzi.wheelview.WheelView)findViewById(R.id.material_volumn_select_wheel);
                List<String> itemsForVolumn = new ArrayList<>();
                for(int i=10;i<=100;++i){
                    itemsForVolumn.add(String.valueOf(i));
                }
                wheelViewMaterialVolumn.setItems(itemsForVolumn);
                wheelViewMaterialVolumn.setMaxSelectableIndex(90);
                //设置出货量选择器的监听器
                wheelViewMaterialVolumn.setOnWheelItemSelectedListener(new com.lantouzi.wheelview.WheelView.OnWheelItemSelectedListener(){
                    @Override
                    public void onWheelItemChanged(WheelView wheelView, int position) {

                    }

                    @Override
                    public void onWheelItemSelected(WheelView wheelView, int position) {
                        Toast.makeText(InfoInput.this, "选择了" + wheelView.getItems().get(position), Toast.LENGTH_SHORT).show();
                    }
                });
                //初始化收货方企业选择
                materialInfoInputTextTargetCorp = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.info_input_material_targetcorp_text);

                final ArrayList<String> optionsItems = new ArrayList<>();
                optionsItems.add("伊利");
                optionsItems.add("蒙牛");
                optionsItems.add("卫岗");
                optionsItems.add("光明");
                optionsPickerViewMaterialReceiver = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        Toast.makeText(InfoInput.this, "当前选择的企业名称是：" + optionsItems.get(options1), Toast.LENGTH_SHORT).show();
                        materialInfoInputTextTargetCorp.setText(optionsItems.get(options1));
                    }
                }).setTitleText("选择收货方")
                        .setTitleSize(20)
                        .setTitleColor(Color.BLACK)
                        .setBackgroundId(0x00000000)
                        .isCenterLabel(false)
                        .isDialog(true)
                        .isRestoreItem(true)
                        .build();
                optionsPickerViewMaterialReceiver.setPicker(optionsItems);
                ImageButton imageButtonMaterialInfoInputTargetSelect = (ImageButton)findViewById(R.id.info_input_material_select_imgbtn);
                imageButtonMaterialInfoInputTargetSelect.setOnClickListener(this);
                //初始化原料出场检验结论
                switchButtonMaterialCheckConclusion = (com.suke.widget.SwitchButton)findViewById(R.id.switchbtn_material_checkconclusion);
                switchButtonMaterialCheckConclusion.setChecked(true);
                switchButtonMaterialCheckConclusion.isChecked();
                switchButtonMaterialCheckConclusion.toggle();
                switchButtonMaterialCheckConclusion.setShadowEffect(true);
                switchButtonMaterialCheckConclusion.setOnCheckedChangeListener(new com.suke.widget.SwitchButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        //获取switch button 的选择的情况
                        Toast.makeText(InfoInput.this, "当前选择：" + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });
                //初始化原料出场时间选择控件
                textInputEditTextMaterialOutTime = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.material_outtime_textinput);
                imageButtonOutTime = (ImageButton)findViewById(R.id.material_outtimeselect_imgbtn);
                final DateSelectDialog dateSelectDialog = new DateSelectDialog();
                DateSelectDialog.Callback callback = new DateSelectDialog.Callback() {
                    @Override
                    public void onClick(String message) {
                        Toast.makeText(InfoInput.this, "当前选择的时间:" + message, Toast.LENGTH_SHORT).show();
                        textInputEditTextMaterialOutTime.setText(message);
                    }
                };
                imageButtonOutTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateSelectDialog.show(getSupportFragmentManager());
                    }
                });

                break;
            case 1:
                //乳制品生产企业处理
                wheelViewProductMaterialUse = (com.lantouzi.wheelview.WheelView)findViewById(R.id.product_material1use_wheel);
                List<String> itemForProductMaterialUse = new ArrayList<>();
                for(int i=100;i<1000;++i)
                    itemForProductMaterialUse.add(String.valueOf(i));
                wheelViewProductMaterialUse.setItems(itemForProductMaterialUse);
                wheelViewProductMaterialUse.setMaxSelectableIndex(900);
                //设置原料使用选择器的监听器
                wheelViewProductMaterialUse.setOnWheelItemSelectedListener(new com.lantouzi.wheelview.WheelView.OnWheelItemSelectedListener(){
                    @Override
                    public void onWheelItemChanged(WheelView wheelView, int position) {

                    }

                    @Override
                    public void onWheelItemSelected(WheelView wheelView, int position) {
                        Toast.makeText(InfoInput.this, "选择了：" + wheelViewProductMaterialUse.getItems().get(position), Toast.LENGTH_SHORT).show();
                    }
                });
                //产品生产时间选择
                textInputEditTextProductTime = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.product_time_edittext);
                imageButtonProductTime = (ImageButton)findViewById(R.id.product_time_imgbtn);
                final DateSelectDialog dateSelectDialogProduct = new DateSelectDialog();
                DateSelectDialog.Callback callback1 = new DateSelectDialog.Callback() {
                    @Override
                    public void onClick(String message) {
                        Toast.makeText(InfoInput.this, "当前选择的时间是："  + message, Toast.LENGTH_SHORT).show();
                        textInputEditTextProductTime.setText(message);
                    }
                };
                imageButtonProductTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dateSelectDialogProduct.show(getSupportFragmentManager());
                    }
                });
                //seekbar 设置
                bubbleSeekBarProductItem1 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.product_materialitem1_seekbar);
                bubbleSeekBarProductItem2 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.product_materialitem2_seekbar);
                bubbleSeekBarProductItem3 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.product_materialitem3_seekbar);
                bubbleSeekBarProductItem4 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.product_materialitem4_seekbar);
                bubbleSeekBarProductItem5 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.product_materialitem5_seekbar);
                bubbleSeekBarProductItem1.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProductItem2.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProductItem3.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProductItem4.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProductItem5.correctOffsetWhenContainerOnScrolling();

                bubbleSeekBarProductItem1.setOnProgressChangedListener(this);
                bubbleSeekBarProductItem2.setOnProgressChangedListener(this);
                bubbleSeekBarProductItem3.setOnProgressChangedListener(this);
                bubbleSeekBarProductItem4.setOnProgressChangedListener(this);
                bubbleSeekBarProductItem5.setOnProgressChangedListener(this);
                //设置ripple button 点击上传图片
                com.andexert.library.RippleView rippleViewImageUpload = (com.andexert.library.RippleView)findViewById(R.id.product_rippleview1);
                rippleViewImageUpload.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Toast.makeText(InfoInput.this, "ripple complete", Toast.LENGTH_SHORT).show();
                    }
                });
                //产品生产车间选择
                imageButtonProductRoom = (ImageButton)findViewById(R.id.product_room_select_imgbtn);
                imageButtonProductRoom.setOnClickListener(this);
                //产品生产验证图片上传处理
                buttonProductImageUpload = (Button)findViewById(R.id.product_photo_upload_btn); //点击上传图片的按钮
                imageViewProductImage = (ImageView)findViewById(R.id.product_upload_image); //显示图片的imageview
                buttonProductFormUpload = (Button)findViewById(R.id.product_form_upload_btn);   //表单上传按钮

                buttonProductImageUpload.setOnClickListener(this);
                buttonProductFormUpload.setOnClickListener(this);


                break;
            case 2:     //物流运输企业处理
                textInputEditTextDriverPhone = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.driver_phone_edittext);   //司机手机号码输入框
                textInputEditTextDriverPhoneCheck = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.driver_phone_checkedittext);  //验证码输入框
                buttonDriverPhoneCheck = (Button)findViewById(R.id.driver_phone_checkget_btn);  //获取验证码
                buttonDriverPhoneCheck.setOnClickListener(this);
                buttonTransportSubmit = (Button)findViewById(R.id.transport_submit_btn);    //物流运输企业提交按钮
                buttonTransportSubmit.setOnClickListener(this);

                break;
            case 3:     //零售企业处理
                textInputEditTextSaleOrg = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_orgcode_edittext);   //零售企业选择
                imageButtonSaleOrgSelect = (ImageButton)findViewById
                        (R.id.sale_orgacode_select_imgbtn);     //零售企业选择按钮
                imageButtonSaleOrgSelect.setOnClickListener(this);

                textInputEditTextBatchId = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_batchid_edittext);   //零售企业进货产品批次选择
                imageButtonBatchIdSelect = (ImageButton)findViewById
                        (R.id.sale_batchid_imgbtn);         //零售企业进货产品批次选择按钮
                imageButtonBatchIdSelect.setOnClickListener(this);

                textInputEditTextInProductTime = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_intime_edittext);    //零售企业进货时间选择
                imageButtonInProductTimeSelect = (ImageButton)findViewById
                        (R.id.sale_intime_imgbtn);      //零售企业进货时间选择按钮
                imageButtonInProductTimeSelect.setOnClickListener(this);

                //switchbutton 处理
                switchButtonProduct1 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct1_switchbtn);
                switchButtonProduct1.setChecked(false);
                switchButtonProduct1.toggle();
                switchButtonProduct1.setShadowEffect(true);
                switchButtonProduct1.setOnCheckedChangeListener(this);
                switchButtonProduct2 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct2_switchbtn);
                switchButtonProduct2.setChecked(false);
                switchButtonProduct2.toggle();
                switchButtonProduct2.setShadowEffect(true);
                switchButtonProduct2.setOnCheckedChangeListener(this);
                switchButtonProduct3 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct3_switchbtn);
                switchButtonProduct3.setChecked(false);
                switchButtonProduct3.toggle();
                switchButtonProduct3.setShadowEffect(true);
                switchButtonProduct3.setOnCheckedChangeListener(this);
                switchButtonProduct4 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct4_switchbtn);
                switchButtonProduct4.setChecked(false);
                switchButtonProduct4.toggle();
                switchButtonProduct4.setShadowEffect(true);
                switchButtonProduct4.setOnCheckedChangeListener(this);
                switchButtonProduct5 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct5_switchbtn);
                switchButtonProduct5.setChecked(false);
                switchButtonProduct5.toggle();
                switchButtonProduct5.setShadowEffect(true);
                switchButtonProduct5.setOnCheckedChangeListener(this);
                switchButtonProduct6 = (com.suke.widget.SwitchButton)findViewById(R.id.sale_inproduct6_switchbtn);
                switchButtonProduct6.setChecked(false);
                switchButtonProduct6.toggle();
                switchButtonProduct6.setShadowEffect(true);
                switchButtonProduct6.setOnCheckedChangeListener(this);

                //范围选择控件处理
                bubbleSeekBarProduct1 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct1_seekbar);
                bubbleSeekBarProduct1.setOnProgressChangedListener(this);
                bubbleSeekBarProduct2 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct2_seekbar);
                bubbleSeekBarProduct2.setOnProgressChangedListener(this);
                bubbleSeekBarProduct3 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct3_seekbar);
                bubbleSeekBarProduct3.setOnProgressChangedListener(this);
                bubbleSeekBarProduct4 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct4_seekbar);
                bubbleSeekBarProduct4.setOnProgressChangedListener(this);
                bubbleSeekBarProduct5 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct5_seekbar);
                bubbleSeekBarProduct5.setOnProgressChangedListener(this);
                bubbleSeekBarProduct6 = (com.xw.repo.BubbleSeekBar)findViewById(R.id.sale_inproduct6_seekbar);
                bubbleSeekBarProduct6.setOnProgressChangedListener(this);
                bubbleSeekBarProduct1.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProduct2.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProduct3.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProduct4.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProduct5.correctOffsetWhenContainerOnScrolling();
                bubbleSeekBarProduct6.correctOffsetWhenContainerOnScrolling();

                textInputEditTextContactPersonName = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_contact_person_phone_edittext);  //联系人姓名
                textInputEditTextContactPersonPhone = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_contact_person_phone_edittext);  //联系人电话
                buttonGetCertificateCode = (Button)
                        findViewById(R.id.sale_phone_getcertificatecode_btn);   //获取短信验证码的button
                textInputEditTextCertificateCode = (android.support.design.widget.TextInputEditText)
                        findViewById(R.id.sale_certificate_code_edittext);      //验证码输入
                buttonGetCertificateCode.setOnClickListener(this);
                buttonSaleUpload = (Button)findViewById(R.id.sale_upload_button);
                buttonSaleUpload.setOnClickListener(this);
                break;

        }
    }


    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        Toast.makeText(this, "当前的选择是" + isChecked, Toast.LENGTH_SHORT).show();
    }

    /**
     * 用于处理生产企业图片的上传
     */
    private void solveProductImageUpload(){
        File outputImage = new File(getExternalCacheDir(),"product_upload_img.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24){
            uriProductImageUpload = FileProvider.getUriForFile(InfoInput.this,
                    "com.example.milktracesystem.fileprovider",outputImage);

        }else{
            uriProductImageUpload = Uri.fromFile(outputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uriProductImageUpload);
        startActivityForResult(intent,TAKEPHOTO);

    }

    /**
     * 用于处理相机的返回数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TAKEPHOTO){
            if(resultCode == RESULT_OK){
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriProductImageUpload));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageViewProductImage.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        if(currPosition == 1){
            bubbleSeekBarProductItem1.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProductItem2.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProductItem3.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProductItem4.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProductItem5.correctOffsetWhenContainerOnScrolling();
        }else if(currPosition == 3){
            bubbleSeekBarProduct1.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProduct2.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProduct3.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProduct4.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProduct5.correctOffsetWhenContainerOnScrolling();
            bubbleSeekBarProduct6.correctOffsetWhenContainerOnScrolling();
        }
        Toast.makeText(this, "onProgressChanged " + progressFloat, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
        Toast.makeText(this, "onprogressaction up " + progress, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        Toast.makeText(this, "当前数值：" + progressFloat, Toast.LENGTH_SHORT).show();
    }

    /**
     * 日期选择对话框的回调函数
     * @param message
     */
    @Override
    public void onClick(String message) {
        Toast.makeText(this, "infoinput 获取到时间" + message, Toast.LENGTH_SHORT).show();
        if(currPosition == 0){
            textInputEditTextMaterialOutTime.setText(message);
        }else if(currPosition == 1){
            textInputEditTextProductTime.setText(message);
        }else if(currPosition == 3){
            textInputEditTextInProductTime.setText(message);
        }
    }


    /**
     * 监听器
     * @param view
     */

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.facname_input_btn:       //企业名称选择填写
                Toast.makeText(this, "选择企业名称", Toast.LENGTH_SHORT).show();
                initFacSelectOptions(); //初始化企业名称选择
                optionsPickerViewFac = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //获取选中的位置
                        String select = facTypeOptions.get(options1) + " 以及 " + facNameOptions
                                .get(options1).get(options2);
                        Toast.makeText(InfoInput.this, "选中内容：" + select, Toast.LENGTH_SHORT).show();
                        //将选中的文字显示在edittext 上
                        facNameInputText.setText(facNameOptions.get(options1).get(options2));

                    }
                }).setTitleText("企业选择")
                        .setContentTextSize(20)
                        .setDividerColor(Color.LTGRAY)
                        .setCancelColor(Color.BLUE)
                        .setTextColorCenter(Color.LTGRAY)
                        .isRestoreItem(true)
                        .isCenterLabel(false)
                        .setLabels("企业类型","企业名称","")
                        .setBackgroundId(0x00000000)
                        .isDialog(true)
                        .build();
                optionsPickerViewFac.setPicker(facTypeOptions,facNameOptions);
                optionsPickerViewFac.show();
                break;
            case R.id.facaddr_select_btn:   //企业地址选择
                Toast.makeText(this, "选择企业地址", Toast.LENGTH_SHORT).show();
                initFacAddrSelectOptions();        //初始化企业地址选择
                optionsPickerViewFacAddr = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //获取选中位置的内容
                        String select = facAddrCity.get(options1) + "以及" + facAddrZone.get(options1).get(options2);
                        Toast.makeText(InfoInput.this, "选中的地址为：" + select, Toast.LENGTH_SHORT).show();
                        facAddrInputText.setText(facAddrCity.get(options1)+facAddrZone.get(options1).get(options2));
                    }
                }).setTitleText("企业地址选择")
                        .setContentTextSize(20)
                        .setDividerColor(Color.LTGRAY)
                        .setCancelColor(Color.BLUE)
                        .setTextColorCenter(Color.LTGRAY)
                        .isRestoreItem(true)
                        .isCenterLabel(false)
                        .setLabels("市","区","")
                        .setBackgroundId(0x00000000)
                        .isDialog(true)
                        .build();

                //绑定数据
                optionsPickerViewFacAddr.setPicker(facAddrCity,facAddrZone);
                optionsPickerViewFacAddr.show();

                break;
            case R.id.info_input_material_select_imgbtn:
                optionsPickerViewMaterialReceiver.show();
                break;
            case R.id.product_room_select_imgbtn:       //产品生产车间选择
                editTextProductRoom = (android.support.design.widget.TextInputEditText)findViewById(R.id.product_room_edittext);
                productFacRoom = new ArrayList<>();
                productFacRoom.add("厂房1");
                productFacRoom.add("厂房2");
                productFacRoom.add("厂房3");
                productWorkRoom = new ArrayList<>();
                ArrayList<String> options2Items;
                for(int i=1;i<=3;++i){
                    options2Items = new ArrayList<>();
                    for(int j=1;j<=4;++j){
                        options2Items.add("车间" + String.valueOf(j));
                    }
                    productWorkRoom.add(options2Items);
                }


                optionsPickerViewProductRoom = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        Toast.makeText(InfoInput.this, "选择的厂房与车间号是："+
                                productFacRoom.get(options1)+productWorkRoom.get(options1).get(options2), Toast.LENGTH_SHORT).show();
                        editTextProductRoom.setText(productFacRoom.get(options1)+"-"+productWorkRoom.get(options1).get(options2));

                    }
                }).setTitleText("选择生产车间")
                        .setContentTextSize(20)
                        .setDividerColor(Color.LTGRAY)
                        .setCancelColor(Color.BLUE)
                        .setTextColorCenter(Color.LTGRAY)
                        .isRestoreItem(true)
                        .isCenterLabel(false)
                        .setBackgroundId(0x00000000)
                        .isDialog(true)
                        .build();
                optionsPickerViewProductRoom.setPicker(productFacRoom,productWorkRoom);
                optionsPickerViewProductRoom.show();
                break;
            case R.id.product_photo_upload_btn: //处理图片上传
                solveProductImageUpload();
                break;
            case R.id.product_form_upload_btn:  //处理表单上传

                break;
            case R.id.driver_phone_checkget_btn:    //处理手机验证
                isGetCertificateCode = Boolean.FALSE;
                DOWNTIME=60;
                timer = new Timer();      //设置定时任务
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    buttonDriverPhoneCheck.setText((--DOWNTIME)+"秒后重发");
                            }
                        });
                    }
                };
                timer.schedule(timerTask,1); //定时每隔1s

                Log.i("短信验证","准备开始验证");
                //短信验证发送与接收处理
                eventHandler = new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        Log.i("短信验证","data is :"+data.toString());
                        if(result == SMSSDK.RESULT_COMPLETE){
                            if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                                Log.i("短信验证","验证成功!");
                                uiToast("短信验证成功");
                                SMSSDK.unregisterEventHandler(eventHandler);
                            }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                Log.i("短信验证","获取验证码成功!");
                                Log.i("短信验证：","获取的data 的内容是：" + data.toString());
                                uiToast("获取验证码成功!");
                                timer.cancel();
                            }
                        }else{
                            //错误处理，包括验证失败的情况
                            ((Throwable)data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eventHandler);  //注册处理
                String phonenum;   //手机号码与验证码
                phonenum = textInputEditTextDriverPhone.getText().toString();
                if(phonenum.trim().equals("")){
                    Toast.makeText(this, "请先输入手机号", Toast.LENGTH_SHORT).show();
                    break;
                }
                Log.i("短信验证","输入的手机号码：["+phonenum+"]");
                SMSSDK.getVerificationCode("86",phonenum,null);

                break;
            case R.id.transport_submit_btn:         //物流运输企业表单提交按钮
                String varificationcode;        //用于输入的验证码
                String phonenum1 = textInputEditTextDriverPhone.getText().toString();
                varificationcode = textInputEditTextDriverPhoneCheck.getText().toString();
                SMSSDK.submitVerificationCode("86",phonenum1,varificationcode); //提交验证
                break;
            case R.id.sale_orgacode_select_imgbtn:  //选择零售企业
                final ArrayList<String> optionsItems = new ArrayList<>();
                optionsItems.add("大润发");
                optionsItems.add("苏果");
                optionsItems.add("家乐福");
                optionsItems.add("沃尔玛");
                optionsItems.add("联华");
                optionsItems.add("永辉");
                optionsItems.add("特易购");
                optionsItems.add("物美");
                optionsItems.add("农工商");
                optionsPickerViewSaleCorp = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        Toast.makeText(InfoInput.this, "当前选择的企业的名称是：" + optionsItems.get(options1), Toast.LENGTH_SHORT).show();
                        textInputEditTextSaleOrg.setText(optionsItems.get(options1));

                    }
                }).setTitleText("选择零售企业")
                        .setTitleSize(20)
                        .setTitleColor(Color.BLACK)
                        .setBackgroundId(0x00000000)
                        .isCenterLabel(false)
                        .isDialog(true)
                        .isRestoreItem(true)
                        .build();
                optionsPickerViewSaleCorp.setPicker(optionsItems);
                optionsPickerViewSaleCorp.show();

                break;
            case R.id.sale_batchid_imgbtn:      //零售企业批次选择
                //产品批次码由行政区划与追溯码构成
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

                optionsPickerViewBatchId = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        textInputEditTextBatchId.setText(optionsItems1.get(options1)+"-"+optionsItems2.get(options1).get(options2));
                    }
                }).setTitleText("选择批次")
                        .setTitleColor(Color.BLACK)
                        .setBackgroundId(0x00000000)
                        .isCenterLabel(false)
                        .isDialog(true)
                        .isRestoreItem(true)
                        .build();
                optionsPickerViewBatchId.setPicker(optionsItems1,optionsItems2);
                optionsPickerViewBatchId.show();

                break;
            case R.id.sale_intime_imgbtn:       //进货时间选择
                DateSelectDialog dateSelectDialogSale = new DateSelectDialog();

                DateSelectDialog.Callback callbackSale = new DateSelectDialog.Callback(){
                @Override
                public void onClick(final String message) {
                    Toast.makeText(InfoInput.this, "当前选择的时间是：" + message, Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textInputEditTextProductTime.setText(message);
                        }
                    });
                }
            };
                dateSelectDialogSale.show(getSupportFragmentManager());

                break;
            case R.id.sale_phone_getcertificatecode_btn:        //获取短信验证码的button
                isGetCertificateCode = Boolean.FALSE;
                DOWNTIME=60;
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonGetCertificateCode.setText((--DOWNTIME)+"秒后重发");
                            }
                        });
                    }
                };
                timer.schedule(timerTask,1000);
                eventHandlerSale = new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        if(result == SMSSDK.RESULT_COMPLETE){
                            if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                                uiToast("短信验证成功");
                                SMSSDK.unregisterEventHandler(eventHandlerSale);
                            }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                uiToast("获取验证码");
                                timer.cancel();
                            }
                        }else{
                            ((Throwable)data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eventHandlerSale);
                String phonenumSale = textInputEditTextContactPersonPhone.getText().toString();
                if(phonenumSale.trim().equals("")){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    break;
                }
                SMSSDK.getVerificationCode("86",phonenumSale,null);

                break;
            case R.id.sale_upload_button:       //零售企业表单提交按钮
                String verificationCode;
                String phonenumSale2 = textInputEditTextContactPersonPhone.getText().toString();
                verificationCode = textInputEditTextCertificateCode.getText().toString();
                SMSSDK.submitVerificationCode("86",phonenumSale2,verificationCode);
                break;
            default:
                break;
        }
    }

    /**
     * 用于在主线程中输出toast 的方法
     * @param msg
     */
    public void uiToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(InfoInput.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);    //释放处理
    }

    /**
     * 用于初始化企业名称的选择
     */
    public void initFacSelectOptions(){
        //初始化选项1
        facTypeOptions = new ArrayList<>();
        facTypeOptions.add("养殖企业");
        facTypeOptions.add("生产企业");
        facTypeOptions.add("物流运输企业");
        facTypeOptions.add("零售企业");

        //初始化选项2
        ArrayList<String> options2Item1= new ArrayList<>();
        options2Item1.add("现代牧业");
        options2Item1.add("原生态牧业");
        options2Item1.add("西部牧业");
        ArrayList<String> options2Item2 = new ArrayList<>();
        options2Item2.add("伊利");
        options2Item2.add("蒙牛");
        options2Item2.add("卫岗");
        ArrayList<String> options2Item3 = new ArrayList<>();
        options2Item3.add("顺丰");
        options2Item3.add("中铁快运");
        options2Item3.add("德邦物流");
        ArrayList<String> options2Item4 = new ArrayList<>();
        options2Item4.add("苏果");
        options2Item4.add("Lotus");
        options2Item4.add("家乐福");
        options2Item4.add("沃尔玛");

        facNameOptions = new ArrayList<>();
        facNameOptions.add(options2Item1);
        facNameOptions.add(options2Item2);
        facNameOptions.add(options2Item3);
        facNameOptions.add(options2Item4);
    }

    /**
     * 初始化企业地址选择的选项
     */
    public void initFacAddrSelectOptions(){
        facAddrCity = new ArrayList<>();
        facAddrCity.add("南京市");
        facAddrCity.add("上海市");
        facAddrCity.add("无锡市");
        //初始化选项2
        ArrayList<String> options2Item1 = new ArrayList<>();
        options2Item1.add("玄武区");
        options2Item1.add("秦淮区");
        options2Item1.add("江宁区");
        options2Item1.add("高淳区");
        ArrayList<String> options2Item2 = new ArrayList<>();
        options2Item2.add("崇明区");
        options2Item2.add("奉贤区");
        options2Item2.add("嘉定区");
        ArrayList<String> options2Item3 = new ArrayList<>();
        options2Item3.add("锡山区");
        options2Item3.add("滨湖区");

        facAddrZone = new ArrayList<>();
        facAddrZone.add(options2Item1);
        facAddrZone.add(options2Item2);
        facAddrZone.add(options2Item3);

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
        //FIXME
//        company_type_select = (Spinner)findViewById(R.id.company_type_edit);
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
//        company_type_select.setAdapter(company_type_adapter);
    }

    /**
     * 用于设置spinner 选项的监听，根据选择的厂商的类型，动态的改变布局，显示
     * 相应的字段
     */
//    public void setSpinnerOnItemClickListener(){
//        this.company_type_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //position 从0开始
//                TableLayout rootView = (TableLayout)findViewById(R.id.company_fill_table);    //TableLayout
//                if(currPosition != -1){     //当前有已加载的布局，删除
//                    rootView.removeView(currAddedView);
//                }
//                switch(position){
//                    case 0:             //加载原料厂商的布局
//                        currPosition=0;
//                        rootView.addView(tableViews[0]);
//                        currAddedView = tableViews[0];
//                        materialFacSolve();     //开始对原料企业的操作的处理
//                        break;
//                    case 1:             //加载乳制品生产企业的布局
//                        currPosition=1;
//                        rootView.addView(tableViews[1]);
//                        currAddedView = tableViews[1];
//                        productFacSolve();      //开始对乳制品生产企业的操作的处理
//                        break;
//                    case 2:             //加载物流运输企业的布局
//                        currPosition=2;
//                        rootView.addView(tableViews[2]);
//                        currAddedView = tableViews[2];
//                        break;
//                    case 3:             //加载零售企业的布局
//                        currPosition=3;
//                        rootView.addView(tableViews[3]);
//                        currAddedView = tableViews[3];
//                        break;
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }
//
//    /**
//     * 针对原料企业的所涉及的字段及操作的处理
//     */
//    public void materialFacSolve(){
//
//        Button buttonMaterialImgUpload = (Button)findViewById(R.id.material_infoInput_imgupload);   //原料企业布局中的上传检验照片的按钮
//        buttonMaterialImgUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogForChooseImgMethod dialogForChooseImgMethod = new DialogForChooseImgMethod();
//                dialogForChooseImgMethod.show(getFragmentManager(),"选择照片的方式");
//            }
//        });
//        //TODO
//
//    }

//    /**
//     * 针对乳制品生产企业所涉及的字段的操作的处理
//     */
//    public void productFacSolve(){
//
//        ImageButton imageButtonDateSelect = (ImageButton)findViewById(R.id.date_select_bt);     //生产日期选择按钮
//        ImageButton imageButtonOutFacDateSelect = (ImageButton)findViewById(R.id.prod_outfac_checktime_select); //出厂检验时间选择
//        imageButtonDateSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog.Builder builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
//                    @Override
//                    public void onPositiveActionClicked(DialogFragment fragment){
//
//                        DatePickerDialog datePickerDialog = (DatePickerDialog)fragment.getDialog();
//                        String date = datePickerDialog.getFormattedDate(SimpleDateFormat.getDateInstance());
//                        Toast.makeText(InfoInput.this, "Date is " + date, Toast.LENGTH_SHORT).show();
//                        super.onPositiveActionClicked(fragment);
//                    }
//
//                    @Override
//                    public void onNegativeActionClicked(DialogFragment fragment){
//                        Toast.makeText(InfoInput.this, "取消", Toast.LENGTH_SHORT).show();
//                        super.onNegativeActionClicked(fragment);
//                    }
//                };
//
//                builder.positiveAction("确定")
//                        .negativeAction("取消");
//                DialogFragment dialogFragmentMaterial = DialogFragment.newInstance(builder);
//                dialogFragmentMaterial.show(getSupportFragmentManager(),null);
//            }
//        });
//
//        //出厂检验时间选择
//        imageButtonOutFacDateSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Dialog.Builder builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light){
//                    @Override
//                    public void onPositiveActionClicked(DialogFragment fragment){
//
//                        DatePickerDialog datePickerDialog = (DatePickerDialog)fragment.getDialog();
//                        String date = datePickerDialog.getFormattedDate(SimpleDateFormat.getDateInstance());
//                        Toast.makeText(InfoInput.this, "Date is " + date, Toast.LENGTH_SHORT).show();
//                        super.onPositiveActionClicked(fragment);
//                    }
//
//                    @Override
//                    public void onNegativeActionClicked(DialogFragment fragment){
//                        Toast.makeText(InfoInput.this, "取消", Toast.LENGTH_SHORT).show();
//                        super.onNegativeActionClicked(fragment);
//                    }
//                };
//
//                builder.positiveAction("确定")
//                        .negativeAction("取消");
//                DialogFragment dialogFragmentMaterial = DialogFragment.newInstance(builder);
//                dialogFragmentMaterial.show(getSupportFragmentManager(),null);
//
//            }
//        });
//
//    }


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

//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//
//        if(materialUploadImg == null)
//            materialUploadImg = (ImageView)findViewById(R.id.material_upload_img);
//        switch(requestCode){
//            case TAKEPHOTO:
//                if(resultCode == RESULT_OK){
//                    try{
//                        Toast.makeText(this, "获取到图片的路径：" + materialUploadImgUri, Toast.LENGTH_SHORT).show();
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(materialUploadImgUri));
//                        //测试
////                        com.rengwuxian.materialedittext.MaterialEditText dirresult =
////                                (com.rengwuxian.materialedittext.MaterialEditText)findViewById(R.id.company_name_edit);
////                        dirresult.setText(materialUploadImgUri.toString());
//
//                        materialUploadImg.setImageBitmap(bitmap);
//
//                    }catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    }
//                }
//
//                break;
//
//            case CHOOSE_PHOTO:
//
//                if(resultCode == RESULT_OK){
//                    if(Build.VERSION.SDK_INT >= 19){
//                        handleImageOnKitKat(data);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//
//    }

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