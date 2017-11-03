package com.example.milktracesystem.Factory_Acticity.Product_Factory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milktracesystem.R;

import java.lang.reflect.Method;

/**
 * Created by 李畅 on 2017/5/5.
 */

public class ProductFactory_Activity extends AppCompatActivity {
    private String factory_name;
    private String product_name;
    private int amount;
    private String batchId;     //批次码
    private String phone;
    private String email;
    private String address;

     @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productfactory_form);
       Intent intent = getIntent();
         EditText editText1 = (EditText)findViewById(R.id.editText7);    //依次对应输入
         EditText editText2 = (EditText)findViewById(R.id.editText9);
         EditText editText3 = (EditText)findViewById(R.id.editText10);
         EditText editText4 = (EditText)findViewById(R.id.editText12);
         EditText editText5 = (EditText)findViewById(R.id.editText13);
         EditText editText6 = (EditText)findViewById(R.id.editText14);
         EditText editText7 = (EditText)findViewById(R.id.editText5);
        factory_name = intent.getStringExtra("productfacname");
        editText1.setText(factory_name);
        editText1.setFocusable(false);
         Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
         setSupportActionBar(toolbar);
         ActionBar actionBar = getSupportActionBar();
         if(actionBar != null){
             actionBar.setDisplayHomeAsUpEnabled(true);
             actionBar.setHomeAsUpIndicator(R.drawable.backup);
         }
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
                ProductFactory_Activity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
