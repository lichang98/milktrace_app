package com.example.milktracesystem.Search_Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.milktracesystem.Camera.SmallCaptureActivity;
import com.example.milktracesystem.R;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;


/**
 * Created by 李畅 on 2017/12/17.
 * 该类作为searchActivity 中卡片布局的适配器
 */

public class SearchActivityCardAdapter extends RecyclerView.Adapter<SearchActivityCardAdapter.SearchCardViewItemViewHolder>{

    private List<SearchActivityCardItem> items;
    private Context context;

    public SearchActivityCardAdapter(List<SearchActivityCardItem> items,Context context){

        this.items = items;
        this.context = context;
    }

    //自定义ViewHolder类
    static class SearchCardViewItemViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textView;
        ImageView imageView;

        public SearchCardViewItemViewHolder(final View itemView){

            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.search_card_view);
            textView = (TextView)itemView.findViewById(R.id.search_card_view_text);
            imageView = (ImageView)itemView.findViewById(R.id.search_card_view_image);
            textView.setBackgroundColor(Color.argb(20,0,0,0));

        }
    }

    @Override
    public SearchCardViewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_activity_carditem,parent,false);
        SearchCardViewItemViewHolder searchCardViewItemViewHolder = new SearchCardViewItemViewHolder(view);
        return searchCardViewItemViewHolder;
    }

    /**
     * 绑定
     * 在此方法内设置组件的点击事件
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(SearchCardViewItemViewHolder holder, final int position) {

        holder.textView.setText(items.get(position).getText());
        holder.imageView.setImageResource(items.get(position).getImgId());
        //对卡片的整体设置点击事件监听
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (position){
                    //扫描二维码
                    case 0:
                        Toast.makeText(context, "点击扫描", Toast.LENGTH_SHORT).show();
                        IntentIntegrator integrator = new IntentIntegrator((Activity)context);
                        integrator.setOrientationLocked(false);     //根据seneor 调整方向
                        integrator.setCaptureActivity(SmallCaptureActivity.class);
                        integrator.initiateScan();
                        break;
                    case 1:

                        Toast.makeText(context, "点击搜索", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
