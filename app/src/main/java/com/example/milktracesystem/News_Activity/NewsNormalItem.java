package com.example.milktracesystem.News_Activity;

/**
 * Created by 李畅 on 2017/7/16.
 */

public class NewsNormalItem {
    private String news_content;    //新闻列表项，文字
    private int news_img_id;        //新闻列表项，图片

    public NewsNormalItem(String news_content,int news_img_id){
        this.news_content = news_content;
        this.news_img_id = news_img_id;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public int getNews_img_id() {
        return news_img_id;
    }

    public void setNews_img_id(int news_img_id) {
        this.news_img_id = news_img_id;
    }
}
