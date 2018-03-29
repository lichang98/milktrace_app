package com.example.milktracesystem.Search_Activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dk.bleNfc.DeviceManager;
import com.dk.bleNfc.DeviceManagerCallback;
import com.dk.bleNfc.Scanner;
import com.dk.bleNfc.ScannerCallback;
import com.example.milktracesystem.R;

/**
 * Created by 李畅 on 2018/3/23.
 * 用于进行与蓝牙BLE模块的连接，传送数据
 */

public class BleModuleLink extends AppCompatActivity {
    private BluetoothDevice bluetoothDeviceLink=null;   //与手机相连接的蓝牙设备对象
    private DeviceManager deviceManager=null;            //设备管理
    private DeviceManagerCallback deviceManagerCallback = null;    //设备管理回调

    private EditText editText=null;
    private Button button=null;

    private Button buttonFindCardType=null;
    private EditText editTextCardTypeResult=null;

    private Button buttonDisConnectDevice=null;
    private EditText editTextDisDevice=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blemodulelink);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_ble_link);  //顶部ToolBar
        setSupportActionBar(toolbar);

        editText = (EditText)findViewById(R.id.editText34);     //显示数据的edittext
        button = (Button)findViewById(R.id.button6);        //启动搜索的按钮
        buttonFindCardType = (Button)findViewById(R.id.button7);        //点击识别卡片的按钮
        editTextCardTypeResult = (EditText)findViewById(R.id.editText35);
        buttonDisConnectDevice = (Button)findViewById(R.id.button8);    //点击断开设备的按钮
        editTextDisDevice = (EditText) findViewById(R.id.editText36);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForBLEModule();
            }
        });
        //确定当前识别的卡片的类型
        buttonFindCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findCardType();
            }
        });
        deviceManagerCallback = new DeviceManagerCallback() {
            //用于判断蓝牙设备是否连接成功
            @Override
            public void onReceiveConnectBtDevice(final boolean blnIsConnectSuc) {
                super.onReceiveConnectBtDevice(blnIsConnectSuc);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(blnIsConnectSuc)
                            Toast.makeText(BleModuleLink.this, "确认当前设备连接成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(BleModuleLink.this, "确定当前设备没有连接成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //用于返回卡片识别的结果
            @Override
            public void onReceiveRfnSearchCard(boolean blnIsSus, final int cardType, byte[] bytCardSn, byte[] bytCarATS) {
                super.onReceiveRfnSearchCard(blnIsSus, cardType, bytCardSn, bytCarATS);
                if(blnIsSus)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextCardTypeResult.append("卡片识别成功\n");
                            editTextCardTypeResult.append(String.valueOf(cardType));
                            if(getCardType(cardType) != null)
                                editTextCardTypeResult.append(getCardType(cardType));
                        }
                    });
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextCardTypeResult.append("卡片识别失败");
                        }
                    });

            }

            //断开设备的回调函数
            @Override
            public void onReceiveDisConnectDevice(boolean blnIsDisConnectDevice) {
                super.onReceiveDisConnectDevice(blnIsDisConnectDevice);
                if(blnIsDisConnectDevice)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextDisDevice.append("设备断开成功");
                        }
                    });
            }
        };
        deviceManager = new DeviceManager(this);
        deviceManager.setCallBack(deviceManagerCallback);
        //点击断开设备的按钮监听
        buttonDisConnectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviceManager.requestDisConnectDevice();
            }
        });
    }

    /**
     * 寻找连接BLE模块
     * @return  true 表示设别搜索连接成功
     */
    public boolean searchForBLEModule(){
        //创建搜索类对象
        final Scanner scanner= new Scanner(BleModuleLink.this, new ScannerCallback() {
            @Override
            public void onReceiveScanDevice(BluetoothDevice device, final int rssi, byte[] scanRecord) {
                super.onReceiveScanDevice(device, rssi, scanRecord);
                bluetoothDeviceLink = device;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.append("当前信号强度: " + rssi);     //显示当前信号强度
                    }
                });
            }

            @Override
            public void onScanDeviceStopped() {
                super.onScanDeviceStopped();
            }
        });

        if(!scanner.isScanning()){
            scanner.startScan(0);
            new Thread(new Runnable() {
                int count=5000;
                @Override
                public void run() {
                    while (bluetoothDeviceLink == null && count > 0 && scanner.isScanning()){
                        --count;
                        try {
                            Thread.sleep(1);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }

                    try{
                        Thread.sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    scanner.stopScan(); //停止搜索
                    if(bluetoothDeviceLink != null)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editText.append("当前连接成功\n");
                            }
                        });
                    else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editText.append("当前连接失败");
                            }
                        });
                }
            }).start();
        }
        return false;
    }


    /**
     * 确定当前识别的卡片的类型
     */
    public void findCardType(){
        if(deviceManager.isConnection()){
            Toast.makeText(this, "当前与设备正在连接中，可以识别卡片", Toast.LENGTH_SHORT).show();
        }
        else{
            String macAddr = bluetoothDeviceLink.getAddress();      //蓝牙设备地址
            Toast.makeText(this, "获取的蓝牙地址为 :" + macAddr, Toast.LENGTH_SHORT).show();
            deviceManager.requestConnectBleDevice(macAddr);     //根据蓝牙地址连接
            Toast.makeText(this, "当前与设备的连接断开", Toast.LENGTH_SHORT).show();
        }
        deviceManager.requestRfmSearchCard((byte)0x00); //自动寻卡
    }

    /**
     * 根据卡片对应的int 类型的标识，返回对应的字符串形式表示的卡片类型
     * @param cardType
     * @return
     */
    public String getCardType(int cardType){
        String result;
        switch(cardType){
            case DeviceManager.CARD_TYPE_ISO4443_A:
                result= "CPU 卡片，使用ISO4443_A协议";
                break;
            case DeviceManager.CARD_TYPE_ISO4443_B:
                result="ISO4443_B卡片，使用ISO14443B协议的卡片";
                break;
            case DeviceManager.CARD_TYPE_MIFARE:
                result="Mifare类型卡片";
                break;
            case DeviceManager.CARD_TYPE_DESFire:
                result="DESFire类型卡片";
                break;
            case DeviceManager.CARD_TYPE_ULTRALIGHT:
                result="ultralight 类型的卡片";
                break;
            default:
                result=null;
                break;
        }
        return result;
    }
}

//    public static final byte CARD_TYPE_DEFAULT = 0;
//    public static final byte CARD_TYPE_ISO4443_A = 1;
//    public static final byte CARD_TYPE_ISO4443_B = 2;
//    public static final byte CARD_TYPE_FELICA = 3;
//    public static final byte CARD_TYPE_MIFARE = 4;
//    public static final byte CARD_TYPE_ISO15693 = 5;
//    public static final byte CARD_TYPE_ULTRALIGHT = 6;
//    public static final byte CARD_TYPE_DESFire = 7;