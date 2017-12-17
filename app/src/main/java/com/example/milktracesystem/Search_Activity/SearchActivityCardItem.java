package com.example.milktracesystem.Search_Activity;

import java.io.Serializable;

/**
 * Created by 李畅 on 2017/12/17.
 * 该类为search activity 活动中显示的列表卡片的每一项中的显示的内容
 */

public class SearchActivityCardItem implements Serializable{

    private String text;    //显示的文字内容
    private int imgId;      //显示的图片的额资源号

    public SearchActivityCardItem(String text, int imgId) {
        this.text = text;
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
