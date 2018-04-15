package com.example.milktracesystem.Factory_Acticity.Factory_USER;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * 用于选择时间的对话框
 */
public class DateSelectDialog extends android.support.v4.app.DialogFragment{
    public interface Callback{
        void onClick(String message);   //返回选择后的数据,which 用于标识调用
    }

    private Callback callback;

    public void show(FragmentManager fragmentManager){
        show(fragmentManager,"选择时间");
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());  //对话框记载relativelayout
        cn.aigestudio.datepicker.views.DatePicker datePicker = new cn.aigestudio.datepicker.views.DatePicker(getActivity());
        datePicker.setDate(2018,4);
        //日期选择监听器
        datePicker.setOnDateSelectedListener(new cn.aigestudio.datepicker.views.DatePicker.OnDateSelectedListener(){
            @Override
            public void onDateSelected(List<String> date) {
                if(callback == null){
                    Toast.makeText(getActivity(), "datepicker回调为null", Toast.LENGTH_SHORT).show();
                }else{
                    String result="";
                    Iterator iterator = date.iterator();
                    while(iterator.hasNext()){
                        result+=iterator.next();
                        if(iterator.hasNext()){
                            result +="\n";
                        }
                    }
                    Toast.makeText(getActivity(), "当前选择的日期是：" + result, Toast.LENGTH_SHORT).show();
                    callback.onClick(result);
                }
            }
        });

        //将日期选择对话框添加到dialog中
        builder.setView(datePicker)
                .setPositiveButton("点击确定后返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Callback){
            callback = (Callback)context;
        }else{
            throw new RuntimeException(context.toString()+"must implment callback");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
