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
                boolean[] isHasAdded = new boolean[4];      //用于标记spinner的选择是否已选择过，方式重复加载
                View[] tableRows = new View[4];     //用于存放不同的选择动态加载的不同的布局
                View curr_add_table_row = null;        //当前动态加载的布局
                //需要根据不同种类的厂商显示不同的输入字段值
                switch(position){
                    case 0: //原料厂商
                        //TODO 测试成功，但是需要放置重复加载
                        LayoutInflater layoutInflater = LayoutInflater.from(InfoInput.this);
                        if(!isHasAdded[0]){         //如果不是重复点击此项，先将之前动态加载的布局删除，之后再加载新的
                            if(curr_add_table_row != null){         //如果之前已经加载过了

                            }
                        }
//                        View tableRow = layoutInflater.inflate(R.layout.info_input_material_table,null);
//                        tableLayout.addView(tableRow);
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

    /**
     * 用于控制根据spinner选择的不同动态加载布局的方法
     * @param position  选择的位置
     * @param currView 当前加载的布局，首次调用时为null
     * @param currViewId 当前加载的布局的编号，首次调用时为-1
     * @param tableRows 用于存储动态加载的布局
     */

    public void dynamicAddViewControl(int position,View currView,int currViewId,View[] tableRows){
        //初始化动态加载的资源
        if(tableRows[0] == null){       //表示所有的动态加载的资源还未加载
            tableRows[0] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_material_table,null);
            tableRows[1] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_product_table,null);
            tableRows[2] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_transport_table,null);
            tableRows[3] = LayoutInflater.from(InfoInput.this).inflate(R.layout.info_input_sale_table,null);
        }
        TableLayout tableLayout = (TableLayout)findViewById(R.id.company_fill_table);   //上级布局
        if(currView == null){       //如果是首次加载
            currView = tableRows[position];     //动态加载子布局
            currViewId = position;              //标记当前的选择
            tableLayout.addView(currView);      //添加
        }else{
            //非首次加载
            //先删除子布局
            //只有
            //TODO 
        }

    }
}