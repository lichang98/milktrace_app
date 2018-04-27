package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
import com.example.milktracesystem.Factory_Acticity.Transport_Factory.TransportFactory_Activity;
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
 * Created by 李畅 on 2017/5/9.
 */

public class TransportFragment extends android.support.v4.app.Fragment implements Callback{
//    private List<Transport_FacItem> transport_fac_list = new ArrayList<Transport_FacItem>();
    //注：        使用materialbean 的结构
    private List<MaterialFactoryListBean> listBeans = new ArrayList<>();
    private RecyclerView listView;
    private MFactoryAdapter transportFacAdapter;
    private RecyclerView.ItemDecoration itemDecoration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.transportfac_fragment,container,false);
//        InitTransfacList();
        transportFacAdapter = new MFactoryAdapter(listBeans);
        listView = (RecyclerView) view.findViewById(R.id.transport_fac_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(transportFacAdapter);
        //设置并初始化recyclerview 项目的装饰
        itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int childCount = parent.getChildCount();
                int left = parent.getPaddingLeft();
                int right = parent.getWidth()-parent.getPaddingRight();

                for(int i=0;i<childCount -1;++i){
                    View view1 = parent.getChildAt(i);
                    float top = view1.getBottom();
                    float bottom = view1.getBottom()+2;
                    Paint paint = new Paint();
                    paint.setColor(Color.LTGRAY);
                    c.drawRect(left,top,right,bottom,paint);
                }
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 2;
            }
        };
        listView.addItemDecoration(itemDecoration);
//        listView.setOnItemClickListener(new ListItemOnClickListener());
        //从后台获取列表数据
        getDataFromServer();

        return view;
    }


    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("从后台获取数据","向后台发送数据请求");
                HttpUtil.sendJsonData(HttpUtil.URL_TRANSPORT_FACTORY_MAINLIST,"","",TransportFragment.this);
            }
        }).start();
    }

    /**
     * implements  okhttp3 callback
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String data = response.body().string();
        listBeans = new Gson().fromJson(data,new TypeToken<List<MaterialFactoryListBean>>(){}.getType());
        Log.i("从后台获取数据","物流运输主列表获取的数据是：" + data);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //更新主列表中的数据
                transportFacAdapter = new MFactoryAdapter(listBeans);
                listView.setAdapter(transportFacAdapter);
            }
        });

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
        public MFactoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
            final MFactoryAdapter.ViewHolder holder = new MFactoryAdapter.ViewHolder(view);
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
        public void onBindViewHolder(MFactoryAdapter.ViewHolder holder, int position){
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

    //    /**
//     * 初始化生产厂商列表
//     */
//    private void InitTransfacList(){
//        transport_fac_list.clear();
//        Transport_FacItem yili = new Transport_FacItem("SF",R.drawable.google);
//        transport_fac_list.add(yili);
//        Transport_FacItem monmilk = new Transport_FacItem("FED_EX",R.drawable.google);
//        transport_fac_list.add(monmilk);
//        Transport_FacItem weigang = new Transport_FacItem("STO",R.drawable.google);
//        transport_fac_list.add(weigang);
//    }
//    /**
//     * 点击事件
//     */
//    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
//        @Override
//        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//            TransportFacAdapter transportFacAdapter = new TransportFacAdapter(view.getContext(),R.layout.product_item,listBeans);
//            String transfacname = listBeans.get(position).getCorpName();        //获取名称
//            //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
//           Intent intent = new Intent(view.getContext(),TransportFactory_Activity.class);
//            intent.putExtra("transportfac_name",transfacname);
//            startActivity(intent);
//        }
//    }

}
