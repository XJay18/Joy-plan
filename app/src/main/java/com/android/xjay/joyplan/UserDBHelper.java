package com.android.xjay.joyplan;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="user.db";
    private static final int DB_VERSION=1;
    private static UserDBHelper mHelper=null;
    private SQLiteDatabase mDB=null;
    public static final String ACTIVITY_TABLE ="user_info";
    public static final String AGENDA_TABLE="agenda_table";

    private UserDBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);

    }
    private UserDBHelper(Context context, int version){
        super(context,DB_NAME,null,version);
    }
    public static UserDBHelper getInstance(Context context, int version){
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
        String drop_sql="DROP TABLE IF EXISTS "+ ACTIVITY_TABLE +";";
        db.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+ ACTIVITY_TABLE +"("+"id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"info VARCHAR NOT NULL,"+"starttime DATETIME NOT NULL,"+"endtime DATETIME not null,"+"address VARCHAR NOT NULL"+");";
        db.execSQL(create_sql);
        create_sql="CREATE TABLE IF NOT EXISTS "+AGENDA_TABLE+"("+"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"starttime DATETIME NOT NULL,"+"endtime DATETIME NOT NULL,"+"content VARCHAR NOT NULL,"+"address VARCHAR NOT NULL"+");";
        db.execSQL(create_sql);
    }

    public void reset(){
        openWriteLink();
        String drop_sql="DROP TABLE IF EXISTS "+ ACTIVITY_TABLE +";";
        mDB.execSQL(drop_sql);
        String create_sql="CREATE TABLE IF NOT EXISTS "+ ACTIVITY_TABLE +"("+"id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"info VARCHAR NOT NULL,"+"starttime DATETIME NOT NULL,"+"endtime DATETIME not null,"+"address VARCHAR NOT NULL"+");";
        mDB.execSQL(create_sql);
        create_sql="CREATE TABLE IF NOT EXISTS "+AGENDA_TABLE+"("+"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+"title VARCHAR NOT NULL,"+"starttime DATETIME NOT NULL,"+"endtime DATETIME NOT NULL,"+"content VARCHAR NOT NULL,"+"address VARCHAR NOT NULL"+");";
        mDB.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public int delete(String condition){
        return mDB.delete(ACTIVITY_TABLE,condition,null);
    }

    public void clean(){
        clean_help(mDB);
    }

    public void drop(){
        drop_help(mDB);
    }

    private void clean_help(SQLiteDatabase db){
        db.execSQL("delete from"+" "+ ACTIVITY_TABLE);
    }
    private void drop_help(SQLiteDatabase db) {db.execSQL("DROP TABLE"+" "+ ACTIVITY_TABLE);}

    public long insert_studentActivity(StudentActivityInfo info){
        long result=-1;
        ContentValues cv=new ContentValues();
        cv.put("title",info.title);
        cv.put("info",info.info);
        cv.put("starttime",info.starttime);
        cv.put("endtime",info.endtime);
        cv.put("address",info.address);
        result=mDB.insert(ACTIVITY_TABLE,"",cv);
        return result;
    }

    public long insert_agenda(Agenda agenda){
        long result=-1;
        openWriteLink();
        ContentValues cv=new ContentValues();
        cv.put("title",agenda.title);
        cv.put("starttime",agenda.start_time);
        cv.put("endtime",agenda.end_time);
        cv.put("content",agenda.content);
        cv.put("address",agenda.address);


        result=mDB.insert(AGENDA_TABLE,"",cv);
        return result;

    }

    public int update(StudentActivityInfo info,String condition){
        return  0;
    }

}






