package com.example.milktracesystem.Feedback_Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.milktracesystem.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

/**
 * 用于显示企业记录的fragment
 */
public class FeedBackFactoryRecordFragment extends Fragment {
    private Context context;
    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback_factory,container,false);
        graphView = (GraphView)view.findViewById(R.id.feed_back_factory_graph1);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0,1),
                new DataPoint(1,5),
                new DataPoint(2,3),
                new DataPoint(3,2),
                new DataPoint(4,6)
        });
        graphView.addSeries(series);
        PointsGraphSeries<DataPointInterface> series1 = new PointsGraphSeries<>(new DataPointInterface[]{
                new DataPointInterface() {
                    @Override
                    public double getX() {
                        return 1.5;
                    }

                    @Override
                    public double getY() {
                        return 4.5;
                    }
                },
                new DataPointInterface() {
                    @Override
                    public double getX() {
                        return 2;
                    }

                    @Override
                    public double getY() {
                        return 4;
                    }
                }
        });
        graphView.addSeries(series1);
        return view;
    }
}
