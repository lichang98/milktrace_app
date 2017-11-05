package com.example.milktracesystem.Camera;

import com.example.milktracesystem.R;
import com.journeyapps.barcodescanner.*;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by 李畅 on 2017/11/5.
 */

/**
 * 创建带margin的扫描框
 */
public class SmallCaptureActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent(){
        setContentView(R.layout.capture_small);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }
}
