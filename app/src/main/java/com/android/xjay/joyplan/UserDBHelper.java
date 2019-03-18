package com.android.xjay.joyplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="user.db";
    private static final int DB_VERSION=1;
    private static UserDBHelper mHelper=null;
    private SQLiteDatabase mDB=null;
    public static final String TABLE_NAME="user_info";

    private UserDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }
    private UserDBHelper(Context context,int version){
        super(context,DB_NAME,null,version);
    }
    public static UserDBHelper getInstance(Context context,int version){
        if(version>0&&mHelper==null) {
            mHelper = new UserDBHelper(context, version);
        }
        else if(mHelper==null){
            mHelper=new UserDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink(){
        if(mDB==null||!mDB.isOpen()){
            mDB=mHelper.getReadableDatabase();
        }
        return mDB;
    }

    public SQLiteDatabase openWriteLink(){
        if(mDB==null||!mDB.isOpen()){
            mDB=mHelper.getWritableDatabase();
        }
        return mDB;
    }

    public void closeLink(){
        if(mDB!=null&&mDB.isOpen()){
            mDB.close();
            mDB=null;
        }
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String drop_sql="DROP TABLE IF EXISTS "+TABLE_NAME+";";
        db.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+"id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"info VARCHAR NOT NULL,"+"date VARCHAR NOT NULL,"+"address VARCHAR NOT NULL"+");";
        db.execSQL(create_sql);
    }

    public void reset(){
        String drop_sql="DROP TABLE IF EXISTS "+TABLE_NAME+";";
        mDB.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+"id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"info VARCHAR NOT NULL,"+"date VARCHAR NOT NULL,"+"address VARCHAR NOT NULL"+");";
        mDB.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public int delete(String condition){
        return mDB.delete(TABLE_NAME,condition,null);
    }

    public void clean(){
        clean_help(mDB);
    }

    public void drop(){
        drop_help(mDB);
    }

    private void clean_help(SQLiteDatabase db){
        db.execSQL("delete from"+" "+TABLE_NAME);
    }
    private void drop_help(SQLiteDatabase db) {db.execSQL("DROP TABLE"+" "+TABLE_NAME);}

    public long insert(StudentActivityInfo info){
        long result=-1;
        ContentValues cv=new ContentValues();
        cv.put("title",info.title);
        cv.put("info",info.info);
        cv.put("date",info.date);
        cv.put("address",info.address);
        result=mDB.insert(TABLE_NAME,"",cv);
        return result;
    }

    public int update(StudentActivityInfo info,String condition){
        return  0;
    }

}

