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
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
import com.example.milktracesystem.Factory_Acticity.Sales_Factory.SalesFactoryActivity;
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

public class SalesFragment extends android.support.v4.app.Fragment implements Callback{
//    private List<Sales_FacItem> sales_fac_list = new ArrayList<Sales_FacItem>();
    //注：    由于主界面列表的结构相同，因此使用与原料企业相同的bean
    private List<MaterialFactoryListBean> listBeans = new ArrayList<>();
    private MFactoryAdapter salesFacAdapter;
    private RecyclerView listView;
    private RecyclerView.ItemDecoration itemDecoration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.salesfac_fragment,container,false);
//        InitTransfacList();
        salesFacAdapter = new MFactoryAdapter(listBeans);
        listView = (RecyclerView) view.findViewById(R.id.sales_fac_list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(salesFacAdapter);
//        listView.setOnItemClickListener(new ListItemOnClickListener());

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
        //从后台获取数据
        getDataFromServer();
        return view;
    }


    /**
     * 在线程中获取后台的数据
     */
    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("从后台获取数据","销售企业发送数据获取请求");
                HttpUtil.sendJsonData(HttpUtil.URL_SALES_FACTORY_MAINLIST,"","",SalesFragment.this);
            }
        }).start();
    }

    /**
     * implements okhttp3 callback
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String data = response.body().string();
        Log.i("从后台获取数据","从后台获取到的销售企业的数据是：" + data);
        //获取到后台列表数据
        listBeans = new Gson().fromJson(data,new TypeToken<List<MaterialFactoryListBean>>(){}.getType());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //更新界面列表数据
                salesFacAdapter = new MFactoryAdapter(listBeans);
                listView.setAdapter(salesFacAdapter);
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
            final ViewHolder holder = new MFactoryAdapter.ViewHolder(view);
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


    //
//    /**
//     * 初始化生产厂商列表
//     */
//    private void InitTransfacList(){
//        sales_fac_list.clear();
//        Sales_FacItem yili = new Sales_FacItem("Walmart",R.drawable.google);
//        sales_fac_list.add(yili);
//        Sales_FacItem monmilk = new Sales_FacItem("Lotus",R.drawable.google);
//        sales_fac_list.add(monmilk);
//        Sales_FacItem weigang = new Sales_FacItem("carryfour",R.drawable.google);
//        sales_fac_list.add(weigang);
//    }
//    /**
//     * 点击事件
//     */
//    private class ListItemOnClickListener implements AdapterView.OnItemClickListener{
//        @Override
//        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//            SalesFacAdapter salesFacAdapter = new SalesFacAdapter(view.getContext(),R.layout.product_item,listBeans);
//            String salesfacname = listBeans.get(position).getCorpName();        //获取名称
//            //      Map<String,String> map = (Map<String,String>) productFacAdapter.getItem(position);
//            Intent intent = new Intent(view.getContext(),SalesFactoryActivity.class);
//            intent.putExtra("sales_fac_name",salesfacname);
//            startActivity(intent);
//        }
//    }
}
