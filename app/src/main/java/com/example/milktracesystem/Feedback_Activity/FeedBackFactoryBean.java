package com.example.milktracesystem.Feedback_Activity;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FeedBackFactoryBean {
    String corpName;
    float rate;
    LineGraphSeries<DataPoint> series;

    public FeedBackFactoryBean() {
    }

    public FeedBackFactoryBean(String corpName, float rate, LineGraphSeries<DataPoint> series) {
        this.corpName = corpName;
        this.rate = rate;
        this.series = series;
    }
}
