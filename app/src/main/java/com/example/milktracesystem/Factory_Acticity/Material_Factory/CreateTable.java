package com.example.milktracesystem.Factory_Acticity.Material_Factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 创建工厂信息的表的类
 * Created by 李畅 on 2017/5/2.
 */

public class CreateTable extends SQLiteOpenHelper {
    public static final String CREATE_FACTORY="create table factory_info" +
            "( factoryname varchar(20) primary key," +
            "address varchar(20),material varchar(20),amount integer," +
            "time varchar(20),phone varchar(20) )";
    private Context mcontext;
    public CreateTable(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_FACTORY);
        Toast.makeText(mcontext,"Create table succeed",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
