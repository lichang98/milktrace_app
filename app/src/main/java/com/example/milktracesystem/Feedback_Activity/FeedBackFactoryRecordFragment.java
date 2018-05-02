package com.example.milktracesystem.Feedback_Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milktracesystem.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示企业记录的fragment
 * 显示为列表的形式，每一个表项包括企业名称、企业信用评级、以及一个图表
 */
public class FeedBackFactoryRecordFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;  //投诉反馈界面的设计由recycler view构成
    private FeedBackFactoryAdapter feedBackFactoryAdapter;  //用于反馈界面显示企业信息的adapter
    private List<FeedBackFactoryBean> feedBackFactoryBeanList;
//    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_factory,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.feedback_factory_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //向列表中添加数据
        initListData();
        feedBackFactoryAdapter = new FeedBackFactoryAdapter(feedBackFactoryBeanList);
        //为recyclerview 添加decoration 以及adapter
        recyclerView.setAdapter(feedBackFactoryAdapter);
        recyclerView.addItemDecoration(itemDecoration);
//        graphView = (GraphView)view.findViewById(R.id.feed_back_factory_graph1);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//                new DataPoint(0,1),
//                new DataPoint(1,5),
//                new DataPoint(2,3),
//                new DataPoint(3,2),
//                new DataPoint(4,6)
//        });
//        //重新设置坐标轴的标签
//        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if(isValueX){
//                    return super.formatLabel(value,isValueX) + "月";
//                }else{
//                    return super.formatLabel(value,isValueX) + "次";
//                }
//
//            }
//
//        });
//        graphView.addSeries(series);
        return view;
    }

    /**
     * 用于初始化反馈部分的企业的列表数据
     */
    public void initListData(){
        feedBackFactoryBeanList = new ArrayList<>();

       FeedBackFactoryBean feedBackFactoryBean1 = new FeedBackFactoryBean();
       feedBackFactoryBean1.corpName="测试企业名称";
       feedBackFactoryBean1.rate=3.5f;
       feedBackFactoryBean1.series = new LineGraphSeries<>(new DataPoint[]{
               new DataPoint(0,1), new DataPoint(1,5),
               new DataPoint(2,3),new DataPoint(3,2),
               new DataPoint(4,6)
       });
       feedBackFactoryBeanList.add(feedBackFactoryBean1);

       feedBackFactoryBean1 = new FeedBackFactoryBean();
       feedBackFactoryBean1.corpName="测试企业名称2";
       feedBackFactoryBean1.rate=5.0f;
       feedBackFactoryBean1.series= new LineGraphSeries<>(new DataPoint[]{
               new DataPoint(1,5),new DataPoint(2,0),
               new DataPoint(3,0),new DataPoint(4,1),
               new DataPoint(5,0),new DataPoint(6,1)
       });

       feedBackFactoryBeanList.add(feedBackFactoryBean1);
    }


    /**
     * 定义recycler view 的装饰器
     */
    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            final int left = parent.getLeft();
            final int right = parent.getRight();
            final int childCount = parent.getChildCount();
            for(int i=0;i<childCount;++i){
                final View childView = parent.getChildAt(i);
                final int bottom = childView.getTop();
                final int top = bottom-5;
                Paint paint = new Paint();
                paint.setColor(Color.LTGRAY);
                c.drawRect(left,top,right,bottom,paint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if(position != 0){
                outRect.top = 5;    //分割线的高度
            }
        }
    };


}
