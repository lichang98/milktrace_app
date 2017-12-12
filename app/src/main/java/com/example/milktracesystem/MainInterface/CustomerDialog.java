package com.example.milktracesystem.MainInterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.milktracesystem.R;

/**
 * 针对消费者注册用户显示的简单的用户注册的对话框
 * Created by 李畅 on 2017/12/12.
 */

public class CustomerDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //设置对话框的布局
        builder.setView(inflater.inflate(R.layout.register_customer,null))
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "点击了登录按钮", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "点击了取消按钮", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
