package com.example.milktracesystem.Factory_Acticity;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by 李畅 on 2017/5/31.
 */

public class Activity_General_Bottom_PagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    public Activity_General_Bottom_PagerAdapter (FragmentManager fragmentManager,ArrayList<Fragment>list){
        super(fragmentManager);
        this.list = list;
    }
    //获得页面的个数
    @Override
    public int getCount(){
        return list.size();
    }
    //得到每个页面
    @Override
    public Fragment getItem(int arg0){
        return list.get(arg0);
    }
}
