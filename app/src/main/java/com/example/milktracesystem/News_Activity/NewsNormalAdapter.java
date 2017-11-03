package com.example.milktracesystem.News_Activity;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.milktracesystem.R;

import java.util.List;

/**
 * Created by 李畅 on 2017/7/16.
 */

public class NewsNormalAdapter extends RecyclerView.Adapter<NewsNormalAdapter.ViewHolder>{
    private Context context;
    private List<NewsNormalItem> newsNormalItemList;    //新闻项

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView newsPic;
        TextView newsTitle;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            newsPic = (ImageView)view.findViewById(R.id.news_normal_img);
            newsTitle = (TextView)view.findViewById(R.id.news_normal_text);
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsNormalItem newsNormalItem = newsNormalItemList.get(position);
        holder.newsTitle.setText(newsNormalItem.getNews_content());
        Glide.with(context).load(newsNormalItem.getNews_img_id()).into(holder.newsPic);

    }

    @Override
    public int getItemCount() {
        return newsNormalItemList.size();
    }
}

/*public class NewsNormalAdapter extends ArrayAdapter<NewsNormalItem> {
    private int resourceId;
    public NewsNormalAdapter(Context context, int textResourceId, List<NewsNormalItem> objects){
        super(context,textResourceId,objects);
        resourceId = textResourceId;
    }
    @Override
    public View getView(int position, View cconvertView, ViewGroup parent){
        NewsNormalItem newsNormalItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(cconvertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            //绑定界面与view
            viewHolder.newsImg = (ImageView)view.findViewById(R.id.news_normal_img);
            viewHolder.newsText = (TextView)view.findViewById(R.id.news_normal_text);
        }else{
            view = cconvertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        ImageView news_normal_img = (ImageView)view.findViewById(R.id.news_normal_img);
        TextView news_normal_text = (TextView)view.findViewById(R.id.news_normal_text);
        news_normal_img.setImageResource(newsNormalItem.getNews_img_id());
        news_normal_text.setText(newsNormalItem.getNews_content());
        return view;
    }*/

    /**
     * 内部类，ViewHolder，用于绑定界面与代码
     */
/*    class ViewHolder{
        ImageView newsImg;
        TextView newsText;
    }
}*/
