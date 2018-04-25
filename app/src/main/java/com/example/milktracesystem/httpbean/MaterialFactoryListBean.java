package com.example.milktracesystem.httpbean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 定义用于从后台获取企业主界面的数据的bean
 */
public class MaterialFactoryListBean implements Parcelable{
    //列表类包含图片在后台的路径的字符串与企业名称
    private String imageUrl;
    private String corpName;

    public MaterialFactoryListBean(String imageUrl, String corpName) {
        this.imageUrl = imageUrl;
        this.corpName = corpName;
    }

    public MaterialFactoryListBean() {
        super();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(corpName);
    }

    public static final Parcelable.Creator<MaterialFactoryListBean> CREATOR = new
            Creator<MaterialFactoryListBean>() {
                @Override
                public MaterialFactoryListBean createFromParcel(Parcel parcel) {
                    return new MaterialFactoryListBean(parcel);
                }

                @Override
                public MaterialFactoryListBean[] newArray(int i) {
                    return new MaterialFactoryListBean[i];
                }
            };

    public MaterialFactoryListBean(Parcel parcel){
        imageUrl = parcel.readString();
        corpName = parcel.readString();
    }
}
