package com.example.milktracesystem.MainInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by 李畅 on 2017/12/12.
 */

public class DialogForChooseImgMethod extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("选择拍照或者是从手机中选择照片")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        noticeDialogListener.onDialogPositiveClick(DialogForChooseImgMethod.this);
                        Toast.makeText(getActivity(), "拍照", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("选择照片", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        noticeDialogListener.onDialogNegativeClick(DialogForChooseImgMethod.this);
                        Toast.makeText(getActivity(), "选择照片", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }

    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    NoticeDialogListener noticeDialogListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            noticeDialogListener = (NoticeDialogListener)activity;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}
