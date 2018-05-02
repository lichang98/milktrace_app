package com.example.milktracesystem.Feedback_Activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.milktracesystem.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * 在反馈界面的企业部分，显示企业信用、被投诉记录信息的列表的adapter
 */
public class FeedBackFactoryAdapter extends RecyclerView.Adapter<FeedBackFactoryAdapter.ViewHolder>{

    private List<FeedBackFactoryBean> feedBackFactoryBeanList;
    /**
     * 该适配器使用的viewholder
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView textViewCorpName;
        MaterialRatingBar materialRatingBar;
        GraphView graphView;

        public ViewHolder(View view){
            super(view);
            itemView = view;
            textViewCorpName = (TextView)itemView.findViewById(R.id.feedback_factory_listitem_text);
            materialRatingBar = (MaterialRatingBar)itemView.findViewById(R.id.feedback_factory_listitem_ratingbar);
            graphView = (GraphView)itemView.findViewById(R.id.feedback_factory_listitem_graph);

        }
    }


    /**
     * adapter 的构造函数
     * @param feedBackFactoryBeanList
     */
    public FeedBackFactoryAdapter(List<FeedBackFactoryBean> feedBackFactoryBeanList) {
        this.feedBackFactoryBeanList = feedBackFactoryBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feedback_factory_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedBackFactoryBean feedBackFactoryBean = feedBackFactoryBeanList.get(position);
        //将数据绑定到控件上
        holder.textViewCorpName.setText(feedBackFactoryBean.corpName);
        holder.materialRatingBar.setRating(feedBackFactoryBean.rate);   //设置评级
        holder.graphView.addSeries(feedBackFactoryBean.series);     //设置图表数据
    }

    @Override
    public int getItemCount() {
        return feedBackFactoryBeanList.size();
    }


}
