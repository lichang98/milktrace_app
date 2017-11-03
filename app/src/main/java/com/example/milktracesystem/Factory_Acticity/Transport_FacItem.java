package com.example.milktracesystem.Factory_Acticity;

/**
 * Created by 李畅 on 2017/5/9.
 */

public class Transport_FacItem {
    private String factory_name;
    private int ImageID;

    public Transport_FacItem(String factory_name,int imageID){
        this.factory_name = factory_name;
        this.ImageID = imageID;
    }
    public String getFactory_name(){
        return factory_name;
    }
    public int getImageID(){
        return ImageID;
    }
}
