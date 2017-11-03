package com.example.milktracesystem.Factory_Acticity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milktracesystem.R;

/**
 * Created by 李畅 on 2017/4/23.
 */

public class FactoryBottom extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.factorybottom_fragment,container,false);
        return view;
    }
}
