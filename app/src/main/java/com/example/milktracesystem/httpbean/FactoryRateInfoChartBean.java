package com.example.milktracesystem.httpbean;


import java.io.Serializable;

/**
 * 该类为android 反馈界面中企业被产品投诉的次数的记录的类
 * 与企业信息与该类是1对多的关系
 */
public class FactoryRateInfoChartBean implements Serializable {
    private final static long serialVersionUID=6L;

    private String id;
    private int month;
    private int feedbackTimes;

    public FactoryRateInfoChartBean() {
        super();
    }

    public FactoryRateInfoChartBean(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getFeedbackTimes() {
        return feedbackTimes;
    }

    public void setFeedbackTimes(int feedbackTimes) {
        this.feedbackTimes = feedbackTimes;
    }

    @Override
    public String toString() {
        return "FactoryRateInfoChartBean{" +
                "id=" + id +
                ", month=" + month +
                ", feedbackTimes=" + feedbackTimes +
                '}';
    }
}
