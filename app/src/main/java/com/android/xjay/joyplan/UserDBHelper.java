package com.android.xjay.joyplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.*;

public class UserDBHelper extends SQLiteOpenHelper {

    public static final String TB_NAME = "user";
    public static final String ID = "id";
    public static final String NAME = "userid";//用户账号
    public static final String UerPwd = "userpwd";//用户密码
    public UserDBHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
        this.getWritableDatabase();
        // TODO Auto-generated constructor stub
    }

    @Override
    //建立表
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL("CREATE TABLE IF NOT EXISTS "
                + TB_NAME + " ("
                + ID + " INTEGER PRIMARY KEY,"
                + NAME + " VARCHAR,"
                + UerPwd + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
    //关闭数据库
    public void close()
    {
        this.getWritableDatabase().close();
    }
    //添加新用户
    public boolean AddUser(String userid,String userpwd)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put(this.NAME, userid);//添加用户名
            cv.put(this.UerPwd,userpwd);//添加密码
            this.getWritableDatabase().insert(this.TB_NAME,null,cv);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

}
