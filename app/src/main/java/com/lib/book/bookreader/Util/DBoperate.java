package com.lib.book.bookreader.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBoperate {
    private static final String driver_url = "jdbc:mysql://localhost:3306/libraryDB?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false";
    private static final String database_user = "user_lib";
    private static final String database_password = "lib_user";
    private static Connection connection;

    /**
     * 获得数据库连接
     * */
    public static void getConn() {
        connection = DBManager.openConnection(driver_url, database_user, database_password);
    }

    /**
     * 用户注册
     * */
    public static void onInsertUser(String username,String passwd) {
        String sql = "insert into usermanager(username,password) values('"+ username +"','" +passwd+ "')";
        DBManager.execSQL(connection, sql);
    }

    /**
     * 添加到我的书架
     * */
    public static void onInsertUserbook(String username,String bookname){
        String sql = "insert into userlibrary(username,book_name) values('"+ username +"','"+ bookname +"')";
        DBManager.execSQL(connection,sql);
    }

    /**
     * 更新图书火热指数
     * */
    public static boolean onupdatehot(int hot_book,String book_name){
        String sql = "update onlinelibrary set book_hot = "+ hot_book +" where book_name = '"+ book_name +"'";
        return DBManager.execSQL(connection,sql);
    }

    /**
     * 从我的书架移除
     * */
    public static void ondeleteuserbook(String username,String bookname){
        String sql = "delete from userlibrary where username='"+ username +"' and book_name='"+ bookname +"'";
        DBManager.execSQL(connection,sql);
    }

    /**
     * 获得在线书库图书
     * */
    public static ArrayList<HashMap<String,Object>> onQuery(String columnname,String tablename) {
        return DBManager.query(connection, "select "+ columnname +" from "+ tablename,columnname,"name");
    }

    /**
     * 获得我的书架的图书
     * */
    public static ArrayList<HashMap<String,Object>> onQueryUserbook(String columnname,String tablename,String user) {
        return DBManager.query(connection, "select "+ columnname +" from "+ tablename +" where username='"+ user +"'",columnname,"name");
    }

    /**
     * 获得排名前二图书火热指数
     * */
    public static ArrayList<HashMap<String,Object>> onQueryhotnum(){
        return DBManager.query(connection,"SELECT DISTINCT book_hot FROM onlinelibrary ORDER BY book_hot DESC limit 3","book_hot","num");
    }

    /**
     * 获得火热图书名称
     * */
    public static ArrayList<HashMap<String,Object>> onQueryhotbook(int hotnum){
        return DBManager.query(connection,"select book_name from onlinelibrary where book_hot = "+ hotnum,"book_name","bookname");
    }

    /**
     * 搜索获取图书名称
     * */
    public static String onQuerybook(String bookname) {
        return DBManager.querybookinfo(connection, "select book_name from onlinelibrary where book_name ='"+ bookname +"'","book_name");
    }

    /**
     * 获取图书在线链接
     * */
    public static String onQuerybooklink(String bookname) {
        return DBManager.querybookinfo(connection, "select book_link from onlinelibrary where book_name ='"+ bookname +"'","book_link");
    }

    /**
     * 查询图书是否已存在书架
     * */
    public static String onQueryInsertUserbook(String bookname,String user) {
        return DBManager.querybookinfo(connection, "select book_name from userlibrary where username='"+ user +"' and book_name='"+ bookname +"'","book_name");
    }

    /**
     * 获取用户名和密码
     * */
    public static HashMap<String,Object> onQueryUser(String data,String tablename) {
        return DBManager.queryuser(connection, "select username,password from "+ tablename +" where username='"+ data +"'");
    }

    /**
     * 获取图书当前火热指数
     * */
    public static int onQueryhot(String bookname){
        return DBManager.queryhot(connection,"select book_hot from onlinelibrary where book_name='"+ bookname +"'");
    }

    /**
     * 关闭数据库
     * */
    public static void onClose() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                connection = null;
            } finally {
                connection = null;
            }
        }
    }
}
