package com.lib.book.bookreader.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager {

    /**
     * 连接MySQL
     * */
    public static Connection openConnection(String driver_url,String database_user,String database_password){

        Connection conn = null;

        try {
            String driver_class="com.mysql.jdbc.Driver";
            Class.forName(driver_class);
            conn = DriverManager.getConnection(driver_url,database_user,database_password);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Success<<<<<<<<<<<<<<<<<<<<<<<");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>not found class<<<<<<<<<<<<<<<<<<<<<<<");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>SQL Error<<<<<<<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Faild<<<<<<<<<<<<<<<<<<<<<<<");
        }
        return conn;
    }

    /**
     * 执行
     * */
    public static boolean execSQL(Connection conn, String sql){
        boolean execResult = false;
        if(conn == null){
            return execResult;
        }

        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            if(stmt != null){
                execResult = stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            execResult = false;
        }
        return execResult;
    }

    /**
     * 查询
     * */
    public static ArrayList<HashMap<String,Object>> query(Connection conn, String sql, String querydata, String querytag){
        ArrayList<HashMap<String,Object>> datalist = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> map;
        if(conn == null){
            return null;
        }

        Statement stmt = null;
        ResultSet res = null;

        try{
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            if(res != null && res.first()){
                int queryColumnIndex = res.findColumn(querydata);
                while(!res.isAfterLast()){
                    map = new HashMap<String,Object>();
                    if(querytag.equals("num")){
                        map.put("num",res.getInt(queryColumnIndex));
                    }else if(querytag.equals("bookname")){
                        map.put("bookname",res.getString(queryColumnIndex));
                    }else if(querytag.equals("name")){
                        map.put("name",res.getString(queryColumnIndex));
                    }
                    datalist.add(map);
                    res.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(res != null){
                    res.close();
                    res = null;
                }

                if(stmt != null){
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return datalist;
    }

    /**
     * 查询图书信息
     * */
    public static String querybookinfo(Connection conn,String sql,String querydata){
        String data = null;
        if(conn == null){
            return null;
        }

        Statement stmt = null;
        ResultSet res = null;

        try{
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            if(res != null && res.first()){
                int dataColumnIndex = res.findColumn(querydata);

                while(!res.isAfterLast()){
                    data = res.getString(dataColumnIndex);
                    res.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(res != null){
                    res.close();
                    res = null;
                }

                if(stmt != null){
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * 查询用户
     * */
    public static HashMap<String,Object> queryuser(Connection conn,String sql){
        HashMap<String,Object> map = null;
        if(conn == null){
            return null;
        }

        Statement stmt = null;
        ResultSet res = null;

        try{
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            if(res != null && res.first()){
                int userColumnIndex = res.findColumn("username");
                int pwdColumnIndex = res.findColumn("password");

                while(!res.isAfterLast()){
                    map = new HashMap<String,Object>();
                    map.put("username",res.getString(userColumnIndex));
                    map.put("passwd",res.getString(pwdColumnIndex));
                    res.next();
                }
            }else {
                map = new HashMap<String,Object>();
                map.put("username","");
                map.put("passwd","");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(res != null){
                    res.close();
                    res = null;
                }

                if(stmt != null){
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 查询火热图书信息
     * */
    public static int queryhot(Connection conn,String sql){
        int hotbook = 0;
        if(conn == null){
            return -1;
        }

        Statement stmt = null;
        ResultSet res = null;

        try{
            stmt = conn.createStatement();
            res = stmt.executeQuery(sql);
            if(res != null && res.first()){
                int hotColumnIndex = res.findColumn("book_hot");
                while(!res.isAfterLast()){
                    hotbook = res.getInt(hotColumnIndex);
                    res.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(res != null){
                    res.close();
                    res = null;
                }

                if(stmt != null){
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hotbook;
    }

}
