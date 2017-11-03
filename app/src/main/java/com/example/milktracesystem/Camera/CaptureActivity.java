package com.example.milktracesystem.Camera;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;


import com.example.milktracesystem.R;

/**
 *
 */
public class CaptureActivity extends Activity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    //为解决扫码横屏问题
//    private MyOrientationDetector myOrientationDetector;
 //   private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeScannerView = initializeContent();
        //解决横屏问题
//        MyOrientationDetector myOrientationDetector = new MyOrientationDetector(this);
  //      myOrientationDetector.setLastOrientation(getWindowManager().getDefaultDisplay().getRotation());
        //
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(com.example.milktracesystem.R.layout.zxing_capture);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //

        //
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //
     //   myOrientationDetector.disable();
        //
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * 解决扫码横屏问题
     * 定义内部类
     */
 /*   private class MyOrientationDetector extends OrientationEventListener {
        private int lastOrientation = -1;

        MyOrientationDetector(Context context) {
            super(context);
        }

        void setLastOrientation(int rotation) {
            switch (rotation) {
                case Surface.ROTATION_90:
                    lastOrientation = 270;
                    break;
                case Surface.ROTATION_270:
                    lastOrientation = 90;
                    break;
                default:
                    lastOrientation = -1;
            }
        }

        @Override
        public void onOrientationChanged(int orientation) {
            Log.d("Camera orientation", "orientation:" + orientation);
            if (orientation > 45 && orientation < 135) {
                orientation = 90;
            } else if (orientation > 225 && orientation < 315) {
                orientation = 270;
            } else {
                orientation = -1;
            }

            if((orientation == 90 && lastOrientation == 270) || (orientation == 270 && lastOrientation == 90)){
                Log.i("Camera orientation","orientation:"+orientation+"last orientation:"+lastOrientation);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                lastOrientation = orientation;
                Log.i("Camera orientation","SUCCEED");
            }

        }
    }*/


}
