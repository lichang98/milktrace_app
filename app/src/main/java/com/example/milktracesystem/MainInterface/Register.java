package com.example.milktracesystem.MainInterface;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.milktracesystem.R;

/**
 * Created by 李畅 on 2017/12/11.
 */

/**
 * 用于注册界面
 * 用于选择用户的类型，消费者、原料企业、生产企业、运输企业、销售企业
 */
public class Register extends AppCompatActivity{

    private RadioGroup radioGroup;
    private RadioButton radioButtonCustomer,
    radioButtonMaterial,radioButtonProduct,radioButtonTransport,
    radioButtonSale;    //代表不同用户类型的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_fac);  //标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.backup);
        }

        initRadios();

    }

    /**
     * 初始按钮组件
     */
    public void initRadios(){

        radioButtonCustomer = (RadioButton)findViewById(R.id.radioButton);
        radioButtonMaterial = (RadioButton)findViewById(R.id.radioButton3);
        radioButtonProduct = (RadioButton)findViewById(R.id.radioButton4);
        radioButtonTransport = (RadioButton)findViewById(R.id.radioButton5);
        radioButtonSale = (RadioButton)findViewById(R.id.radioButton6);

        radioGroup = (RadioGroup)findViewById(R.id.register_radiogroup);
        radioGroup.check(R.id.radioButton);

        //设置按钮组选择监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Toast.makeText(Register.this, "you have click " + i, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
