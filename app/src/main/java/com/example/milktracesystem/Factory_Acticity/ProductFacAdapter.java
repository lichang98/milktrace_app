package com.example.milktracesystem.Factory_Acticity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milktracesystem.Factory_Acticity.Product_FacItem;
import com.example.milktracesystem.R;

import java.util.List;

/**
 * Created by 李畅 on 2017/5/5.
 */

public class ProductFacAdapter extends ArrayAdapter<Product_FacItem> {
    private int resourceId;
    public ProductFacAdapter(Context context, int textResourceId, List<Product_FacItem> objects){
        super(context,textResourceId,objects);
        resourceId = textResourceId;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        Product_FacItem product_facItem = getItem(position);
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
        productfacImage.setImageResource(product_facItem.getImageID());
        productfacText.setText(product_facItem.getFactory_name());
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
