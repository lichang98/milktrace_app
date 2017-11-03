package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.milktracesystem.Factory_Acticity.SalesFacAdapter;
import com.example.milktracesystem.Factory_Acticity.Sales_FacItem;
import com.example.milktracesystem.Factory_Acticity.Sales_Factory.SalesFactoryActivity;
import com.example.milktracesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class SalesFragment extends android.support.v4.app.Fragment{
    private List<Sales_FacItem> sales_fac_list = new ArrayList<Sales_FacItem>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.salesfac_fragment,container,false);
        InitTransfacList();
        SalesFacAdapter salesFacAdapter = new SalesFacAdapter(view.getContext(),R.layout.sales_fac_item,sales_fac_list);
        ListView listView = (ListView)view.findViewById(R.id.sales_fac_list);
        listView.setAdapter(salesFacAdapter);
        listView.setOnItemClickListener(new ListItemOnClickListener());
        return view;
    }

    /**
     * 初始化生产厂商列表
     */
    private void InitTransfacList(){
        sales_fac_list.clear();
        Sales_FacItem yili = new Sales_FacItem("Walmart",R.drawable.common_google_signin_btn_icon_dark_normal);
        sales_fac_list.add(yili);
        Sales_FacItem monmilk = new Sales_FacItem("Lotus",R.drawable.common_google_signin_btn_icon_dark_focused);
        sales_fac_list.add(monmilk);
        Sales_FacItem weigang = new Sales_FacItem("carryfour",R.drawable.common_google_signin_btn_icon_light_disabled);
        sales_fac_list.add(weigang);
    }
    /**
     * 点击事件
     */
    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            SalesFacAdapter salesFacAdapter = new SalesFacAdapter(view.getContext(),R.layout.product_item,sales_fac_list);
            String salesfacname = sales_fac_list.get(position).getFactory_name();        //获取名称
            //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
            Intent intent = new Intent(view.getContext(),SalesFactoryActivity.class);
            intent.putExtra("sales_fac_name",salesfacname);
            startActivity(intent);
        }
    }
}
