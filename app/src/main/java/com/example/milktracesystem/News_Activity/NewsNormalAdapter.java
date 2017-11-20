package com.example.milktracesystem.News_Activity;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.R;

import java.util.List;

/**
 * Created by 李畅 on 2017/7/16.
 */

public class NewsNormalAdapter extends RecyclerView.Adapter<NewsNormalAdapter.ViewHolder>{
    private Context context;
    private List<NewsNormalItem> newsNormalItemList;    //新闻项
    private MyNewsItemClickListener myNewsItemClickListener;    //卡片项的点击事件监听器

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView newsPic;
        TextView newsTitle;
        MyNewsItemClickListener clickListener;

        public ViewHolder(View view, final MyNewsItemClickListener myNewsItemClickListener){
            super(view);
            cardView = (CardView)view;
            newsPic = (ImageView)view.findViewById(R.id.news_normal_img);
            newsTitle = (TextView)view.findViewById(R.id.news_normal_text);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view,getAdapterPosition());
                }
            });
        }
    }

    public NewsNormalAdapter (List<NewsNormalItem> newsNormalItemList){
        this.newsNormalItemList = newsNormalItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.news_normal_item,
                parent,false);
        return new ViewHolder(view,myNewsItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NewsNormalItem newsNormalItem = newsNormalItemList.get(position);
        holder.newsTitle.setText(newsNormalItem.getNews_content());
        Glide.with(context).load(newsNormalItem.getNews_img_id()).into(holder.newsPic);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "position:"+position, Toast.LENGTH_SHORT).show();
            }
        });
//        //为viewHolder中的图片添加监听器
//        holder.newsPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "position:"+position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        //为ViewHolder中的文字标题添加监听器
//        holder.newsTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "position:"+position, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return newsNormalItemList.size();
    }

    /**
     * 用于提供外部设计监听器的方法
     * @param listener
     */
    public void setOnItemClickListener(MyNewsItemClickListener listener){
        this.myNewsItemClickListener = listener;
    }
}
