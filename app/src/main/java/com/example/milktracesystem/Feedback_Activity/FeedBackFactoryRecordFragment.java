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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.example.milktracesystem.HttpUtil.HttpUtil;
import com.example.milktracesystem.R;
import com.example.milktracesystem.httpbean.FactoryRateInfoBean;
import com.example.milktracesystem.httpbean.FactoryRateInfoChartBean;
import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 用于显示企业记录的fragment
 * 显示为列表的形式，每一个表项包括企业名称、企业信用评级、以及一个图表
 */
public class FeedBackFactoryRecordFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;  //投诉反馈界面的设计由recycler view构成
    private FeedBackFactoryAdapter feedBackFactoryAdapter;  //用于反馈界面显示企业信息的adapter
    private List<FeedBackFactoryBean> feedBackFactoryBeanList;
    private GroupListener groupListener;
    private StickyDecoration stickyDecoration;

    private Callback callback;  //okhttp3 后台数据获取的结果回调
//    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_factory,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.feedback_factory_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //向列表中添加数据
        feedBackFactoryBeanList = new ArrayList<>();

        callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将获取到的json 格式的数据转换为factoryrateinfobean list 类型的数据
                String data = response.body().string();
//                feedBackFactoryBeanList = new Gson().fromJson(data,new TypeToken<List<FactoryRateInfoBean>>(){}.getType());
                List<FactoryRateInfoBean> factoryRateInfoBeanList = new Gson().fromJson(data,new TypeToken<List<FactoryRateInfoBean>>(){}.getType());
                //将获取到的数据的对应的字段复制到当前使用的list中
                feedBackFactoryBeanList = new ArrayList<>();
                for(final FactoryRateInfoBean factoryRateInfoBean: factoryRateInfoBeanList){
                    Collections.sort(factoryRateInfoBean.getChartBeanList(), new Comparator<FactoryRateInfoChartBean>() {
                        @Override
                        public int compare(FactoryRateInfoChartBean factoryRateInfoChartBean, FactoryRateInfoChartBean t1) {
                            return factoryRateInfoChartBean.getMonth()-t1.getMonth();
                        }
                    });
                    FeedBackFactoryBean tmp = new FeedBackFactoryBean();
                    tmp.title = factoryRateInfoBean.getFacType();
                    tmp.rate = factoryRateInfoBean.getCorpRate();
                    tmp.corpName = factoryRateInfoBean.getCorpName();
                    tmp.series = new LineGraphSeries<>();
                    //复制图表的数据
                    for(FactoryRateInfoChartBean chartBean : factoryRateInfoBean.getChartBeanList()){
                        DataPoint tmp2 = new DataPoint(chartBean.getMonth(),chartBean.getFeedbackTimes());
                        tmp.series.appendData(tmp2,false,100);
                    }
                    feedBackFactoryBeanList.add(tmp);
                }
                Log.i("反馈界面","从后台获取的反馈界面的企业的数据是：" + feedBackFactoryBeanList.toString());
                notifyDataChanged();    //对界面的数据进行更新
            }
        };

        initListData();
        groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                return feedBackFactoryBeanList.get(position).title;
            }
        };
        stickyDecoration = StickyDecoration.Builder.init(groupListener)
                .setGroupBackground(Color.parseColor("#48BDFF"))
                .setGroupTextColor(Color.BLACK)
                .setDivideColor(Color.parseColor("#CCCCCC"))
                .setTextSideMargin(DensityUtil.dip2px(getContext(),1))
                .build();
        feedBackFactoryAdapter = new FeedBackFactoryAdapter(feedBackFactoryBeanList);
        //为recyclerview 添加decoration 以及adapter
        recyclerView.setAdapter(feedBackFactoryAdapter);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(stickyDecoration);


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
     * 用于在获取后台数据完成后，对界面上的数据进行更新
     */
    private void notifyDataChanged(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                feedBackFactoryAdapter = new FeedBackFactoryAdapter(feedBackFactoryBeanList);
                recyclerView.setAdapter(feedBackFactoryAdapter);
            }
        });
    }

    /**
     * 用于初始化反馈部分的企业的列表数据
     */
    public void initListData(){
        //FIXME 从后台数据库获取图表列表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendJsonData(HttpUtil.URL_FEEDBACK_FACTORY_CHARTDATA,"","",callback);
            }
        }).start();

//        feedBackFactoryBeanList = new ArrayList<>();
//
//       FeedBackFactoryBean feedBackFactoryBean1 = new FeedBackFactoryBean();
//       feedBackFactoryBean1.title="原料企业";
//       feedBackFactoryBean1.corpName="测试企业名称";
//       feedBackFactoryBean1.rate=3.5f;
//       feedBackFactoryBean1.series = new LineGraphSeries<>(new DataPoint[]{
//               new DataPoint(0,1), new DataPoint(1,5),
//               new DataPoint(2,3),new DataPoint(3,2),
//               new DataPoint(4,6)
//       });
//       feedBackFactoryBeanList.add(feedBackFactoryBean1);
//
//       feedBackFactoryBean1 = new FeedBackFactoryBean();
//       feedBackFactoryBean1.title="原料企业";
//       feedBackFactoryBean1.corpName="测试企业名称2";
//       feedBackFactoryBean1.rate=5.0f;
//       feedBackFactoryBean1.series= new LineGraphSeries<>(new DataPoint[]{
//               new DataPoint(1,5),new DataPoint(2,0),
//               new DataPoint(3,0),new DataPoint(4,1),
//               new DataPoint(5,0),new DataPoint(6,1)
//       });
//
//       feedBackFactoryBeanList.add(feedBackFactoryBean1);
//
//       feedBackFactoryBean1 = new FeedBackFactoryBean();
//       feedBackFactoryBean1.title="生产企业";
//       feedBackFactoryBean1.corpName="测试企业名称3";
//       feedBackFactoryBean1.rate=1.3f;
//       feedBackFactoryBean1.series = new LineGraphSeries<>(new DataPoint[]{
//               new DataPoint(1,6),new DataPoint(2,4),
//               new DataPoint(3,4),new DataPoint(4,4),
//               new DataPoint(5,2)
//       });
//
//       feedBackFactoryBeanList.add(feedBackFactoryBean1);
//
//       feedBackFactoryBean1 = new FeedBackFactoryBean();
//       feedBackFactoryBean1.title="生产企业";
//       feedBackFactoryBean1.corpName="测试企业名称4";
//       feedBackFactoryBean1.rate=4.8f;
//       feedBackFactoryBean1.series = new LineGraphSeries<>(new DataPoint[]{
//               new DataPoint(1,0),new DataPoint(2,1),
//               new DataPoint(3,1),new DataPoint(4,1),
//               new DataPoint(5,0)
//       });
//
//       feedBackFactoryBeanList.add(feedBackFactoryBean1);
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
