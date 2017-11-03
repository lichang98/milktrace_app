package com.example.milktracesystem.Factory_Acticity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.milktracesystem.Factory_Acticity.Transport_FacItem;
import com.example.milktracesystem.R;

import java.util.List;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class TransportFacAdapter extends ArrayAdapter <Transport_FacItem>{
    private int resourceId;
    public TransportFacAdapter(Context context, int textResourceId, List<Transport_FacItem> objects){
        super(context,textResourceId,objects);
        resourceId = textResourceId;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
       Transport_FacItem transport_facItem = getItem(position);
        //View view1 = LayoutInflater.from(getContext()).inflate(resourceId,null);
        View view1;
        ViewHolder viewHolder;
        if(convertView == null){
            view1 = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.facImage = (ImageView)view1.findViewById(R.id.productfac_imag);
            viewHolder.facName = (TextView)view1.findViewById(R.id.productfac_name);
            view1.setTag(viewHolder);   //重新获取viewHolder
        }else{
            view1 = convertView;
            viewHolder = (ViewHolder)view1.getTag();
        }
     /*   viewHolder.facImage.setImageResource(transport_facItem.getImageID());
        viewHolder.facName.setText(transport_facItem.getFactory_name());*/
       ImageView transfacImage = (ImageView)view1.findViewById(R.id.transport_fac_image);
        TextView transfacText = (TextView)view1.findViewById(R.id.transport_fac_text);
        transfacImage.setImageResource(transport_facItem.getImageID());
       transfacText.setText(transport_facItem.getFactory_name());
        return view1;
    }
    /**
     * 内部类viewHolder
     */
    class ViewHolder{
        ImageView facImage;
        TextView facName;
    }
}
