package com.example.milktracesystem.Factory_Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milktracesystem.Factory_Acticity.Material_Factory.MaterialFactoryActivity;
import com.example.milktracesystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李畅 on 2017/4/23.
 */

public class MaterialFragment extends android.support.v4.app.Fragment {
    private List<MFactory> factoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.material_fragment,container,false);
        InitMFac(); //初始化厂商数据
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.materialfactories_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MFactoryAdapter adapter = new MFactoryAdapter(factoryList);
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void InitMFac(){
        factoryList.clear();
        for(int i=0;i<3;i++){
            MFactory mFactory = new MFactory("辉山",R.drawable.huishan);
            factoryList.add(mFactory);
            MFactory mFactory1 = new MFactory("圣牧",R.drawable.modernfarm);
            factoryList.add(mFactory1);
        }
    }


    /**
     * 内部类,厂商信息适配器
     */
    public class MFactoryAdapter extends RecyclerView.Adapter<MFactoryAdapter.ViewHolder>{
        private List<MFactory> mFactoryList;
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
        public MFactoryAdapter(List<MFactory> mFactoryList1){
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
                    MFactory mFactory = mFactoryList.get(position);
                    String mfactoryname = mFactory.getName();        //获取名称
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
            MFactory mFactory = mFactoryList.get(position);
            holder.mfacImageview.setImageResource(mFactory.getImageId());
            holder.mfacname.setText(mFactory.getName());
        }
        @Override
        public int getItemCount(){
            return mFactoryList.size();
        }
    }

    /**
     * 内部类,厂商类
     */
    public class MFactory{
        private String name;    //厂商名称
        private int imageId;    //图片id
        public MFactory(String name,int imageId){
            this.name = name;
            this.imageId = imageId;
        }
        public String getName(){
            return this.name;
        }
        public int getImageId(){
            return this.imageId;
        }
    }
}
