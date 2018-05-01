package com.example.milktracesystem.MainInterface;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.R;
import com.hhxplaying.neteasedemo.netease.activity.NewsDisplayActivity;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneralItemAdapter extends RecyclerView.Adapter<GeneralItemAdapter.GeneralItemViewHolder> {

    private ArrayList<HashMap<String,Object>> list;  //存储数据的列表

    public GeneralItemAdapter(ArrayList<HashMap<String,Object>> list) {
            this.list = list;
    }

    @Override
    public GeneralItemViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_general_list,
                parent,false);
        final GeneralItemViewHolder viewHolder = new GeneralItemViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition(); //获取位置
//                Toast.makeText(view.getContext(), "当前点击的位置：" + position, Toast.LENGTH_SHORT).show();
                switch(position){
                    case 0:
                        Intent intent = new Intent(parent.getContext(), NewsDisplayActivity.class);
                        intent.putExtra("NEWS_CONTENT_URL",(String) list.get(0).get("itemcontentUrl"));
                        intent.putExtra("type","news"); //区别于乳制品百科
                        parent.getContext().startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(parent.getContext(),NewsDisplayActivity.class);
                        intent1.putExtra("NEWS_CONTENT_URL",(String)list.get(1).get("itemcontentUrl"));
                        intent1.putExtra("type","baike");
                        parent.getContext().startActivity(intent1);

                        break;
                    default:
                        break;
                }

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GeneralItemViewHolder holder, int position) {
        Log.i("主界面列表","绑定视图，position = " + position);
        holder.textView.setText((String)list.get(position).get("itemText"));
        if(!(list.get(position).get("itemImage") instanceof String))
            holder.imageView.setImageResource((Integer)list.get(position).get("itemImage"));
        else
            Glide.with(holder.itemView.getContext()).load((String)list.
                    get(position).get("itemImage")).into(holder.imageView);

        if(!list.get(position).get("itemcontentUrl").equals(""))
            holder.contentUrl = (String) list.get(position).get("itemcontentUrl");

        holder.imgtitleText.setText((String)list.get(position).get("itemTitle"));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 主界面列表的viewholder
     */
    static class GeneralItemViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView textView;
        ImageView imageView;
        TextView imgtitleText;  //图片副标题
        String contentUrl;      //新闻内容的链接

        public GeneralItemViewHolder(View view){
            super(view);
            itemView = view;
            textView = (TextView)itemView.findViewById(R.id.item_general_text);
            imageView = (ImageView)itemView.findViewById(R.id.item_general_img);
            imgtitleText = (TextView)itemView.findViewById(R.id.item_general_img_title);
            contentUrl = "";
        }
    }
}
