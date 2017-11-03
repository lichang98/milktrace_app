package com.example.milktracesystem.Factory_Acticity.Sales_Factory;

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
 * Created by 李畅 on 2017/5/9.
 */

public class SalesFactoryActivity extends AppCompatActivity {
    private String salesfacname;
    private String productboughtname;
    private int boughtamount;
    private String boughttime;
    private String freshtill;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salesfactory_form);

        EditText editText = (EditText)findViewById(R.id.editText26);
        EditText editText1 = (EditText)findViewById(R.id.editText27);
        EditText editText2= (EditText)findViewById(R.id.editText28);
        EditText editText3 = (EditText)findViewById(R.id.editText29);
        EditText editText4 = (EditText)findViewById(R.id.editText30);
        EditText editText5 = (EditText)findViewById(R.id.editText31);

        salesfacname = getIntent().getStringExtra("sales_fac_name");    //获取传递的名称参数
        editText.setText(salesfacname);
        editText.setFocusable(false);
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
                SalesFactoryActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }
}
