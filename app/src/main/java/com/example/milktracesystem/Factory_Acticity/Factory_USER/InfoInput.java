package com.example.milktracesystem.Factory_Acticity.Factory_USER;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milktracesystem.Factory_Acticity.FactoryActivity;
import com.example.milktracesystem.R;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by 李畅 on 2017/9/23.
 */

/**
 * 用于企业用户输入产品的信息
 * 输入的产品以批次为单位
 * 上传的数据包括文字描述及图片
 * 之后更新，部分数据使用NFC获取
 */
public class InfoInput extends AppCompatActivity {
    private Spinner company_type_select;    //表单填写企业类别选择
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
                //  获取填写表单的Table 布局
                TableLayout tableLayout = (TableLayout)findViewById(R.id.company_fill_table);
                ViewParent parentView = tableLayout.getParent();        //总布局
                switch(position){
                    case 0: //原料厂商
                        //TODO 测试成功，但是需要放置重复加载
                        LayoutInflater layoutInflater = LayoutInflater.from(InfoInput.this);
                        View tableRow = layoutInflater.inflate(R.layout.info_input_material_table,null);
                        tableLayout.addView(tableRow);
                        break;
                    case 1: //乳制品生产商

                        break;
                    case 2: //物流运输商

                        break;
                    case 3: //零售批发商

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}