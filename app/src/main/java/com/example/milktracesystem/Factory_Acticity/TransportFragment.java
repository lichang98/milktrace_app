package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.milktracesystem.Factory_Acticity.TransportFacAdapter;
import com.example.milktracesystem.Factory_Acticity.Transport_FacItem;
import com.example.milktracesystem.Factory_Acticity.Transport_Factory.TransportFactory_Activity;
import com.example.milktracesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class TransportFragment extends android.support.v4.app.Fragment{
    private List<Transport_FacItem> transport_fac_list = new ArrayList<Transport_FacItem>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.transportfac_fragment,container,false);
        InitTransfacList();
        TransportFacAdapter transportFacAdapter = new TransportFacAdapter(view.getContext(),R.layout.transport_fac_item,transport_fac_list);
        ListView listView = (ListView)view.findViewById(R.id.transport_fac_list);
        listView.setAdapter(transportFacAdapter);
        listView.setOnItemClickListener(new ListItemOnClickListener());
        return view;
    }

    /**
     * 初始化生产厂商列表
     */
    private void InitTransfacList(){
        transport_fac_list.clear();
        Transport_FacItem yili = new Transport_FacItem("SF",R.drawable.google);
        transport_fac_list.add(yili);
        Transport_FacItem monmilk = new Transport_FacItem("FED_EX",R.drawable.google);
        transport_fac_list.add(monmilk);
        Transport_FacItem weigang = new Transport_FacItem("STO",R.drawable.google);
        transport_fac_list.add(weigang);
    }
    /**
     * 点击事件
     */
    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            TransportFacAdapter transportFacAdapter = new TransportFacAdapter(view.getContext(),R.layout.product_item,transport_fac_list);
            String transfacname = transport_fac_list.get(position).getFactory_name();        //获取名称
            //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
           Intent intent = new Intent(view.getContext(),TransportFactory_Activity.class);
            intent.putExtra("transportfac_name",transfacname);
            startActivity(intent);
        }
    }

}
