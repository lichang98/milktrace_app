package com.example.milktracesystem.httpbean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示在反馈界面中企业反馈投诉记录的数据
 */
public class FactoryRateInfoBean implements Serializable {
    private final static long serialVersionUID=5L;

    private String id;
    private String corpName;
    private String facType;         //企业的类型
    private float corpRate;         //企业的评级 0-5 浮点数
    //    private Map<Integer,Integer> chartData;         //用于图表显示的数据<月份，被投诉的次数>
    private List<FactoryRateInfoChartBean> chartBeanList;   //用于图表显示的数据的list

    public FactoryRateInfoBean() {
        super();
        chartBeanList = new ArrayList<>();
    }

    public FactoryRateInfoBean(String id, String corpName, String facType, float corpRate, List<FactoryRateInfoChartBean> chartBeanList) {
        this.id = id;
        this.corpName = corpName;
        this.facType = facType;
        this.corpRate = corpRate;
        this.chartBeanList = chartBeanList;
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

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public float getCorpRate() {
        return corpRate;
    }

    public void setCorpRate(float corpRate) {
        this.corpRate = corpRate;
    }

    public List<FactoryRateInfoChartBean> getChartBeanList() {
        return chartBeanList;
    }

    public void setChartBeanList(List<FactoryRateInfoChartBean> chartBeanList) {
        this.chartBeanList = chartBeanList;
    }

    @Override
    public String toString() {
        return "FactoryRateInfoBean{" +
                "id=" + id +
                ", corpName='" + corpName + '\'' +
                ", facType='" + facType + '\'' +
                ", corpRate=" + corpRate +
                ", chartBeanList=" + chartBeanList +
                '}';
    }
}

