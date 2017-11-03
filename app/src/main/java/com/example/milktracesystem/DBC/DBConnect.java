package com.example.milktracesystem.DBC;

import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 李畅 on 2017/7/19.
 *
 */

public class DBConnect{
    public Connection connection = null;
    //取得与MySQL数据库的连接，返回Connection
    public Connection getConnection(){
        try{
            String driverClass="com.mysql.jdbc.Driver";
            String url="jdbc:mysql://117.87.29.85:3306/db_design?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false";
            String username="root";
            String password="mysql";
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
