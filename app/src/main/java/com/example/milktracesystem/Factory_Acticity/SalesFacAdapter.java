package com.example.milktracesystem.Factory_Acticity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milktracesystem.Factory_Acticity.Sales_FacItem;
import com.example.milktracesystem.R;

import java.util.List;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class SalesFacAdapter extends ArrayAdapter <Sales_FacItem>{
    private int resourceId;
    public SalesFacAdapter(Context context, int textResourceId, List<Sales_FacItem> objects){
        super(context,textResourceId,objects);
        resourceId = textResourceId;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        Sales_FacItem transport_facItem = getItem(position);
        //View view1 = LayoutInflater.from(getContext()).inflate(resourceId,null);
        View view1;
        ViewHolder viewHolder;
        if(convertView == null){
            view1 = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.facImage = (ImageView)view1.findViewById(R.id.productfac_imag);
            viewHolder.facName = (TextView)view1.findViewById(R.id.productfac_name);
            view1.setTag(viewHolder);   //将viewHolder存储在view中
        }else{
            view1 = convertView;
            viewHolder = (ViewHolder) view1.getTag();
        }
/*        viewHolder.facImage.setImageResource(transport_facItem.getImageID());
        viewHolder.facName.setText(transport_facItem.getFactory_name());*/
        ImageView salesfacimage = (ImageView)view1.findViewById(R.id.sales_fac_image);
        TextView salesfactext = (TextView)view1.findViewById(R.id.sales_fac_text);
        salesfacimage.setImageResource(transport_facItem.getImageID());
        salesfactext.setText(transport_facItem.getFactory_name());
        return view1;
    }

    /**
     * 内部类，viewHolder
     */
    class ViewHolder{
        ImageView facImage;
        TextView facName;
    }
}
