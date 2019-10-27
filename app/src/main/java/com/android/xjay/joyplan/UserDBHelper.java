package com.android.xjay.joyplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    public static final String ACTIVITY_TABLE = "user_info";
    public static final String AGENDA_TABLE = "agenda_table";
    public static final String COURSE_TABLE = "course_table";


    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    private UserDBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public static UserDBHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new UserDBHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    public void closeLink() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String drop_sql = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE + ";";
        db.execSQL(drop_sql);

        String create_sql = "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + AGENDA_TABLE + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME NOT NULL," + "content VARCHAR NOT NULL," + "address VARCHAR NOT NULL" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        db.execSQL(create_sql);
    }

    public void reset() {
        openWriteLink();
        String drop_sql = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE + ";";
        mDB.execSQL(drop_sql);
        drop_sql = "DROP TABLE IF EXISTS " + AGENDA_TABLE + ";";
        mDB.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL" + ");";
        mDB.execSQL(create_sql);
        create_sql = "CREATE TABLE IF NOT EXISTS " + AGENDA_TABLE + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME NOT NULL," + "content VARCHAR NOT NULL," + "address VARCHAR NOT NULL" + ");";
        mDB.execSQL(create_sql);
        create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "year INTEGER NOT NULL," + "indexofsemester INTERGER NOT NULL," + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        mDB.execSQL(create_sql);
    }

    public void resetCourseTable() {
        openWriteLink();
        String drop_sql = "DROP TABLE IF EXISTS " + COURSE_TABLE + ";";
        mDB.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "year INTEGER NOT NULL," + "indexofsemester INTERGER NOT NULL," + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        mDB.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int delete(String condition) {
        return mDB.delete(ACTIVITY_TABLE, condition, null);
    }

    public int deleteAgendaWithIndex(int index) {
        String id = new Integer(index).toString();
        return mDB.delete(AGENDA_TABLE, "id=?", new String[]{id});
    }

    public int deleteActivityWithIndex(int index) {
        String id = new Integer(index).toString();
        openWriteLink();
        return mDB.delete(ACTIVITY_TABLE, "id=?", new String[]{id});
    }

    public void clean() {
        clean_help(mDB);
    }

    public void drop() {
        drop_help(mDB);
    }

    private void clean_help(SQLiteDatabase db) {
        db.execSQL("delete from" + " " + ACTIVITY_TABLE);
    }

    private void drop_help(SQLiteDatabase db) {
        db.execSQL("DROP TABLE" + " " + ACTIVITY_TABLE);
    }

    public long insert_studentActivity(StudentActivityInfo info) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put("title", info.title);
        cv.put("info", info.info);
        cv.put("starttime", info.starttime);
        cv.put("endtime", info.endtime);
        cv.put("address", info.address);
        result = mDB.insert(ACTIVITY_TABLE, "", cv);
        return result;
    }

    public long insert_agenda(Agenda agenda) {
        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("title", agenda.title);
        cv.put("starttime", agenda.start_time);
        cv.put("endtime", agenda.end_time);
        cv.put("content", agenda.content);
        cv.put("address", agenda.address);


        result = mDB.insert(AGENDA_TABLE, "", cv);
        return result;

    }

    public long insert_course(Course course) {
        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("year", course.year);
        cv.put("indexofsemester", course.index0fSemester);
        cv.put("coursename", course.courseName);
        cv.put("dayofweek", course.dayofweek);
        cv.put("startweek", course.startWeek);
        cv.put("endweek", course.endWeek);
        cv.put("startindex", course.startIndex);
        cv.put("numofcourse", course.numOfCourse);
        cv.put("address", course.address);
        cv.put("teachername", course.teacherName);

        result = mDB.insert(COURSE_TABLE, "", cv);
        return result;

    }


    /**
     *
     * @param date %m%d
     * @return
     */
    public ArrayList<Agenda> getAgendaListWithDate(String date){


        openReadLink();
        Cursor cursor = null;
        Agenda agenda;
        cursor = mDB.query(AGENDA_TABLE, null, createAgendaSelectActionDate(), new String[]{date}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int length = cursor.getCount();
            ArrayList<Agenda> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                String title = cursor.getString(1);
                String starttime = cursor.getString(2);
                String endtime = cursor.getString(3);
                String content = cursor.getString(4);
                String address = cursor.getString(5);
                agenda = new Agenda(title, starttime, endtime, content, address);
                agenda.index = cursor.getInt(0);
                list.add(agenda);
                cursor.move(1);
            }
            return list;
        } else return new ArrayList<>();
    }

    public ArrayList<Course> getCourseWithDayOfWeek(int year, int indexOfSemester, int week, int dayofweek) {
        openReadLink();
        Cursor cursor = null;
        ArrayList<Course> courseArrayList = new ArrayList<>();


        String str_year = new Integer(year).toString();
        String str_index0fSemester = new Integer(indexOfSemester).toString();
        String str_week = new Integer(week).toString();
        String str_dayofweek = new Integer(dayofweek).toString();


        cursor = mDB.query(COURSE_TABLE, null, createCourseSelectActionDayOfWeek(), new String[]{str_year, str_index0fSemester, str_dayofweek, str_week, str_week}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int length = cursor.getCount();
            for (int i = 0; i < length; i++) {
                String courseName = cursor.getString(2);
                String str_startWeek = cursor.getString(4);
                String str_endWeek = cursor.getString(5);
                String str_startIndex = cursor.getString(6);
                String str_numOfCourse = cursor.getString(7);
                String address = cursor.getString(8);
                String teacherName = cursor.getString(9);

                int startIndex = Integer.parseInt(str_startIndex);
                int numOfCourse = Integer.parseInt(str_numOfCourse);
                int startWeek = Integer.parseInt(str_startWeek);
                int endWeek = Integer.parseInt(str_endWeek);
                Course course = new Course(year, indexOfSemester, courseName, dayofweek, startWeek, endWeek, startIndex, numOfCourse, address, teacherName);
                courseArrayList.add(course);
                cursor.move(1);
            }
        }
        return courseArrayList;
    }

    public Agenda getAgendaWithTime(String date) {
        openReadLink();
        Cursor cursor = null;
        Agenda agenda;
        cursor = mDB.query(AGENDA_TABLE, null, createAgendaSelectActionTime(), new String[]{date}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            String title = cursor.getString(1);
            String starttime = cursor.getString(2);
            String endtime = cursor.getString(3);
            String content = cursor.getString(4);
            String address = cursor.getString(5);
            agenda = new Agenda(title, starttime, endtime, content, address);
            agenda.index = cursor.getInt(0);
            return agenda;
        } else return null;
    }


    public String createAgendaSelectActionDate(){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("starttime");
        stringBuffer.append(")=?");
        return stringBuffer.toString();

    }

    public String createAgendaSelectActionTime() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%m%d%H',");
        stringBuffer.append("starttime");
        stringBuffer.append(")=?");
        return stringBuffer.toString();

    }

    public String createCourseSelectActionDayOfWeek() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("year=? and indexofsemester=? and dayofweek=? and startweek<=? and endweek>=?");
        return stringBuffer.toString();
    }

    public int update(StudentActivityInfo info, String condition) {
        return 0;
    }

}






