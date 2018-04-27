package com.example.milktracesystem.Factory_Acticity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.Factory_Acticity.Product_FacItem;
import com.example.milktracesystem.R;
import com.example.milktracesystem.httpbean.MaterialFactoryListBean;

import java.util.List;

/**
 * Created by 李畅 on 2017/5/5.
 * 注：   由于与原料企业的主界面的列表结构相同，使用原料企业的bean
 */

public class ProductFacAdapter extends ArrayAdapter<MaterialFactoryListBean> {
    private int resourceId;
    public ProductFacAdapter(Context context, int textResourceId, List<MaterialFactoryListBean> objects){
        super(context,textResourceId,objects);
        resourceId = textResourceId;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        MaterialFactoryListBean product_facItem = getItem(position);
       // View view1 = LayoutInflater.from(getContext()).inflate(resourceId,null);
        View view1;
        ViewHolder viewHolder;
        if(convertView == null){
            view1 = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.FacImage = (ImageView)view1.findViewById(R.id.productfac_imag);
            viewHolder.FacName = (TextView)view1.findViewById(R.id.productfac_name);
        }else{
            view1 = convertView;
            viewHolder = (ViewHolder)view1.getTag();    //重新获取viewHolder
        }
      /*  viewHolder.FacImage.setImageResource(product_facItem.getImageID());
        viewHolder.FacName.setText(product_facItem.getFactory_name());*/
      ImageView productfacImage = (ImageView)view1.findViewById(R.id.productfac_imag);
        TextView productfacText = (TextView)view1.findViewById(R.id.productfac_name);
        //文字直接添加，图片使用glide url
        productfacText.setText(product_facItem.getCorpName());
        Glide.with(getContext()).load(product_facItem.getImageUrl()).into(productfacImage);
//        productfacImage.setImageResource(product_facItem.getImageID());
//        productfacText.setText(product_facItem.getFactory_name());
        return view1;
    }
    /**
     * 内部类viewHolder
     */
    class ViewHolder{
        ImageView FacImage;
        TextView FacName;
    }
}
