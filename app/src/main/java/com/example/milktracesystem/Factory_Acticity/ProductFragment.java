package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.milktracesystem.Factory_Acticity.Product_Factory.ProductFactory_Activity;
import com.example.milktracesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李畅 on 2017/5/5.
 */

public class ProductFragment extends android.support.v4.app.Fragment {
    private List<Product_FacItem> productfac_list = new ArrayList<Product_FacItem>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.product_fragment,container,false);
        InitProfacList();
        ProductFacAdapter productFacAdapter = new ProductFacAdapter(view.getContext(),R.layout.product_item,productfac_list);
        ListView listView = (ListView)view.findViewById(R.id.productfac_list);
        listView.setAdapter(productFacAdapter);
        listView.setOnItemClickListener(new ListItemOnClickListener());
        return view;
    }

    /**
     * 初始化生产厂商列表
     */
    private void InitProfacList(){
        productfac_list.clear();
        for(int i=0;i<9;++i){
            Product_FacItem yili = new Product_FacItem("伊利",R.drawable.yili);
            productfac_list.add(yili);
            Product_FacItem monmilk = new Product_FacItem("蒙牛",R.drawable.monmilk);
            productfac_list.add(monmilk);
            Product_FacItem weigang = new Product_FacItem("卫岗",R.drawable.weigang);
            productfac_list.add(weigang);
        }

    }
    /**
     * 点击事件
     */
    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
           ProductFacAdapter productFacAdapter = new ProductFacAdapter(view.getContext(),R.layout.product_item,productfac_list);
            String productfacname = productfac_list.get(position).getFactory_name();        //获取名称
      //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
            Intent intent = new Intent(view.getContext(),ProductFactory_Activity.class);
            intent.putExtra("productfacname",productfacname);
            startActivity(intent);
        }
    }
}
