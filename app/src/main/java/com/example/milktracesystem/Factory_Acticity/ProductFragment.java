package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.milktracesystem.Factory_Acticity.Product_Factory.ProductFactory_Activity;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.R;
import com.example.milktracesystem.httpbean.MaterialFactoryListBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 李畅 on 2017/5/5.
 */

public class ProductFragment extends android.support.v4.app.Fragment implements Callback{
//    private List<Product_FacItem> productfac_list = new ArrayList<Product_FacItem>();
    //注：由于结构相同，生产企业主界面列表使用原料企业主列表结构bean
    private List<MaterialFactoryListBean> listBeans = new ArrayList<>();
    private ListView listView;
    private ProductFacAdapter productFacAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.product_fragment,container,false);
//        InitProfacList();
        productFacAdapter = new ProductFacAdapter(view.getContext(),R.layout.product_item,listBeans);
        listView = (ListView)view.findViewById(R.id.productfac_list);
        listView.setAdapter(productFacAdapter);
        listView.setOnItemClickListener(new ListItemOnClickListener());
        //从后台获取列表数据
        Log.i("从后台获取数据","发送json请求");
        getListData();
        return view;
    }

    /**
     * implments Callback of okhttp3
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
    }

    /**
     *  implments Callback of okhttp3
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String data = response.body().string(); //获取后台返回的数据
        //将数据解析到list 中
        listBeans = new Gson().fromJson(data,new TypeToken<List<MaterialFactoryListBean>>(){}.getType());
        Log.i("从后台获取数据","获取到的数据是：" + data);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在主线程中更新数据
                productFacAdapter = new ProductFacAdapter(getContext(),R.layout.product_item,listBeans);
                listView.setAdapter(productFacAdapter);
            }
        });
    }

    /**
     * 在线程中获取生产企业主列表中的数据
     */
    public void getListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendJsonData(HttpUtil.URL_PRODUCT_FACTORY_MAINLIST,"","",ProductFragment.this);
            }
        }).start();
    }
//    /**
//     * 初始化生产厂商列表
//     */
//    private void InitProfacList(){
//        listBeans.clear();
//        for(int i=0;i<9;++i){
//            Product_FacItem yili = new Product_FacItem("伊利",R.drawable.yili);
//            productfac_list.add(yili);
//            Product_FacItem monmilk = new Product_FacItem("蒙牛",R.drawable.monmilk);
//            productfac_list.add(monmilk);
//            Product_FacItem weigang = new Product_FacItem("卫岗",R.drawable.weigang);
//            productfac_list.add(weigang);
//        }
//
//    }
    /**
     * 点击事件
     */
    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
           ProductFacAdapter productFacAdapter = new ProductFacAdapter(view.getContext(),R.layout.product_item,listBeans);
            String productfacname = listBeans.get(position).getCorpName();        //获取名称
      //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
            Intent intent = new Intent(view.getContext(),ProductFactory_Activity.class);
            intent.putExtra("productfacname",productfacname);
            startActivity(intent);
        }
    }
}
