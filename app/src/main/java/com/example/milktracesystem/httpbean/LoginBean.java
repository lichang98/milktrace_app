package com.example.milktracesystem.httpbean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用于android 与web端的连接json 使用的bean
 */
public class LoginBean implements Parcelable{
    private String username;
    private String passwd;

    public LoginBean() {
        super();
    }

    public LoginBean(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(passwd);
    }

    public static final Parcelable.Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel parcel) {
            return new LoginBean(parcel);
        }

        @Override
        public LoginBean[] newArray(int i) {
            return new LoginBean[i];
        }
    };

    public LoginBean(Parcel parcel){
        username = parcel.readString();
        passwd = parcel.readString();
    }
}
