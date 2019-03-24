package com.android.xjay.joyplan.DAO;
import android.util.Log;
import java.sql.*;
public class BaseDAO {
    private static String driver="com.mysql.cj.jdbc.Driver";
    private static String url="jdbc:mysql://localhost:3306/joyplan?useSSL=false&serverTimezone=UTC;"+"useUnicode=true&characterEncoding=UTF8";
    private static String user="root";
    private static String pass="root";
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    public BaseDAO(){
        this.conn=getConnection();
    }
    private static Connection getConnection(){

        try {

            Class.forName(driver);
            conn=DriverManager.getConnection(url,user,pass);
            stmt=conn.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    public boolean searchuser(String str1,String str2){
       try {
           String querySql = "select * from user";
           rs = stmt.executeQuery(querySql);
           while (rs.next()) {
               if (rs.getString("number") == str1 && rs.getString("password")==str2)
                   return true;
           }
           return false;
       }
        catch (SQLException e) {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }
       return false;
    }
    public void closeSQL() {
        try {
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
