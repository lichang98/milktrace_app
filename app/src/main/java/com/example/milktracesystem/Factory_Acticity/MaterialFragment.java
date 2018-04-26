package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.Factory_Acticity.Factory_USER.DateSelectDialog;
import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
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
 * Created by 李畅 on 2017/4/23.
 */

public class MaterialFragment extends android.support.v4.app.Fragment {
//    private List<MFactory> factoryList = new ArrayList<>();
    private List<MaterialFactoryListBean> factoryListBeanList = new ArrayList<>();  //用于接收从后台传递过来的主列表数据
    private Callback callback;
    private MFactoryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.material_fragment,container,false);
//        InitMFac(); //初始化厂商数据
        recyclerView = (RecyclerView)view.findViewById(R.id.materialfactories_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MFactoryAdapter(factoryListBeanList);
        recyclerView.setAdapter(adapter);
        //启动线程从后台获取数据
        //回调函数，返回结果
        callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String getData = response.body().string();  //获取返回的字符串
                Log.i("后台数据","从后台获取的原料企业主列表的数据是："+getData);
                factoryListBeanList = new Gson().fromJson(getData,new TypeToken<List<MaterialFactoryListBean>>(){}.getType());
                //TODO 通知主界面更新数据
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new MFactoryAdapter(factoryListBeanList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        };
        getMainListData();  //从后台获取数据

        return view;
    }
//    private void InitMFac(){
//        factoryListBeanList.clear();
//        //TODO cong 后台获取企业名称与图标
//
//
//
////        for(int i=0;i<3;i++){
////            MFactory mFactory = new MFactory("辉山",R.drawable.huishan);
////            factoryList.add(mFactory);
////            MFactory mFactory1 = new MFactory("圣牧",R.drawable.modernfarm);
////            factoryList.add(mFactory1);
////        }
//    }


    /**
     * 从后台获取数据
     */
    public void getMainListData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "向服务器请求主列表数据", Toast.LENGTH_SHORT).show();
                    }
                });
                HttpUtil.sendJsonData(HttpUtil.URL_MATERIAL_FACTORY_MAINLIST,HttpUtil.FACTORY_MATERIAL_LIST,"",callback);
            }
        }).start();
    }

    /**
     * 内部类,厂商信息适配器
     */
    public class MFactoryAdapter extends RecyclerView.Adapter<MFactoryAdapter.ViewHolder>{
//        private List<MFactory> mFactoryList;
        private List<MaterialFactoryListBean> mFactoryList;
        class ViewHolder extends RecyclerView.ViewHolder{
            View MFacView;
            ImageView mfacImageview;
            TextView mfacname;
            public ViewHolder(View view){
                super(view);
                MFacView = view;
                mfacImageview = (ImageView)view.findViewById(R.id.productfac_imag); //
                mfacname = (TextView)view.findViewById(R.id.productfac_name);
            }
        }
        public MFactoryAdapter(List<MaterialFactoryListBean> mFactoryList1){
            mFactoryList = mFactoryList1;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.MFacView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    MaterialFactoryListBean mFactory = mFactoryList.get(position);
                    String mfactoryname = mFactory.getCorpName();        //获取名称
                    //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
                    Intent intent = new Intent(view.getContext(),MaterialFactoryActivity.class);
                    intent.putExtra("factory_name",mfactoryname);
                    startActivity(intent);
                }
            });
            return holder;
        }
        @Override
        public void onBindViewHolder(ViewHolder holder,int position){
            MaterialFactoryListBean mFactory = mFactoryList.get(position);
            //TODO 改为使用GLIDE
            Log.i("列表数据加载","当前加载的位置：" + position+"当前加载的image url="+mFactoryList.get(position).getImageUrl());
            holder.mfacname.setText(mFactoryList.get(position).getCorpName());
            Glide.with(getActivity()).load(mFactoryList.get(position).getImageUrl()).into(holder.mfacImageview);
//            holder.mfacImageview.setImageResource(mFactory.getImageId());
//            holder.mfacname.setText(mFactory.getName());
        }
        @Override
        public int getItemCount(){
            return mFactoryList.size();
        }
    }


    /**
     * 修改后的厂商类
     * 属性包含厂商名称与厂商图片在后台中的url 路径
     */
//    public class MFactory{
//        private String imageUrl;
//        private String corpName;
//
//        public MFactory(String imageUrl, String corpName) {
//            this.imageUrl = imageUrl;
//            this.corpName = corpName;
//        }
//
//        public MFactory() {
//            super();
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//        public String getCorpName() {
//            return corpName;
//        }
//
//        public void setCorpName(String corpName) {
//            this.corpName = corpName;
//        }
//    }

    /**
     * 内部类,厂商类
     */
//    public class MFactory{
//        private String name;    //厂商名称
//        private int imageId;    //图片id
//        public MFactory(String name,int imageId){
//            this.name = name;
//            this.imageId = imageId;
//        }
//        public String getName(){
//            return this.name;
//        }
//        public int getImageId(){
//            return this.imageId;
//        }
//    }
}
