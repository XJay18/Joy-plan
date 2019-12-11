package com.android.xjay.joyplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * 数据库操作类
 */
public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final int DB_VERSION = 1;
    private static UserDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    public static final String RESERVE_ACTIVITY_TABLE = "reserve_activity_table";
    public static final String ACTIVITY_TABLE = "user_info";
    public static final String AGENDA_TABLE = "agenda_table";
    public static final String COURSE_TABLE = "course_table";
    public static final String FQZ_STATICTIS = "fqz_statictis";

    private UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    private UserDBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    /**
     * 获取实例
     *
     * @param context
     * @param version
     * @return
     */
    public static UserDBHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new UserDBHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    /**
     * 打开读取连接
     *
     * @return
     */
    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    /**
     * 打开写入连接
     *
     * @return
     */
    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    /**
     * 关闭连接
     */
    public void closeLink() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_sql = "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL," + "img BOLB NOT NULL" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + AGENDA_TABLE + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME NOT NULL," + "notation VARCHAR NOT NULL," + "address VARCHAR NOT NULL" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "notation VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + FQZ_STATICTIS + "(" + "startoftime DATETIME NOT NULL," + "monday INTEGER NOT NULL," + "tuesday INTEGER NOT NULL," + "wednesday INTEGER NOT NULL," + "thursday INTEGER NOT NULL," + "friday INTEGER NOT NULL," + "saturday INTEGER NOT NULL," + "sunday INTEGER NOT NULL," + "PRIMARY KEY(startoftime)" + ");";
        db.execSQL(create_sql);

        create_sql = "CREATE TABLE IF NOT EXISTS " + RESERVE_ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL," + "img BOLB NOT NULL" + ");";
        db.execSQL(create_sql);

    }

    /**
     * 重置数据库
     */
    public void reset() {
        openWriteLink();
        String drop_sql = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE + ";";
        mDB.execSQL(drop_sql);
        drop_sql = "DROP TABLE IF EXISTS " + AGENDA_TABLE + ";";
        mDB.execSQL(drop_sql);
        drop_sql = "DROP TABLE IF EXISTS " + COURSE_TABLE + ";";
        mDB.execSQL(drop_sql);
        drop_sql = "DROP TABLE IF EXISTS " + RESERVE_ACTIVITY_TABLE + ";";
        mDB.execSQL(drop_sql);


        String create_sql = "CREATE TABLE IF NOT EXISTS " + ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL," + "img BOLB NOT NULL" + ");";
        mDB.execSQL(create_sql);
        create_sql = "CREATE TABLE IF NOT EXISTS " + AGENDA_TABLE + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME NOT NULL," + "notation VARCHAR NOT NULL," + "address VARCHAR NOT NULL" + ");";
        mDB.execSQL(create_sql);
        create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "year INTEGER NOT NULL," + "indexofsemester INTERGER NOT NULL," + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "notation VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        mDB.execSQL(create_sql);
        create_sql = "CREATE TABLE IF NOT EXISTS " + RESERVE_ACTIVITY_TABLE + "(" + "id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL," + "title VARCHAR NOT NULL," + "info VARCHAR NOT NULL," + "starttime DATETIME NOT NULL," + "endtime DATETIME not null," + "address VARCHAR NOT NULL," + "img BOLB NOT NULL" + ");";
        mDB.execSQL(create_sql);
    }

    /**
     * 重置课程表
     */
    public void resetCourseTable() {
        openWriteLink();
        String drop_sql = "DROP TABLE IF EXISTS " + COURSE_TABLE + ";";
        mDB.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + "(" + "year INTEGER NOT NULL," + "indexofsemester INTERGER NOT NULL," + "coursename VARCHAR NOT NULL," + "dayofweek INTEGER NOT NULL," + "startweek INTEGER NOT NULL," + "endweek INTEGER NOT NULL," + "startindex INTEGER NOT NULL," + "numofcourse INTEGER NOT NULL," + "address VARCHAR NOT NULL," + "teachername VARCHAR NOT NULL," + "notation VARCHAR NOT NULL," + "PRIMARY KEY(dayofweek,startweek,startindex)" + ");";
        mDB.execSQL(create_sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 根据标题和开始时间删除日程
     *
     * @param agenda
     * @return
     */
    public int deleteAgendaWithTitleAndStarttime(Agenda agenda) {
        String title = agenda.getTitle();
        String starttime = agenda.getStarttime();
        return mDB.delete(AGENDA_TABLE, "title=? and starttime=?", new String[]{title, starttime});
    }

    /**
     * 删除课程
     *
     * @param course
     * @return
     */
    public int deleteCourse(Course course) {
        String str_year = String.valueOf(course.getYear());
        String str_semester = String.valueOf(course.getNumOfCourse());
        String str_dayOfWeek = String.valueOf(course.getDayofweek());
        String str_startweek = String.valueOf(course.getStartWeek());
        String str_startindex = String.valueOf(course.getStartIndex());
        String str_coursename = String.valueOf(course.getCourseName());
        return mDB.delete(COURSE_TABLE, "year=? and indexofsemester=? and dayofweek=? and startweek=? and startindex=? and coursename=?", new String[]{str_year, str_semester, str_dayOfWeek, str_startweek, str_startindex, str_coursename});
    }

    /**
     * 删除预定活动
     *
     * @param studentActivityInfo
     * @return
     */
    public int deleteReserveActivity(StudentActivityInfo studentActivityInfo) {
        String title = studentActivityInfo.getTitle();
        String starttime = studentActivityInfo.getStarttime();
        return mDB.delete(RESERVE_ACTIVITY_TABLE, "title=? and starttime=?", new String[]{title, starttime});
    }


    /**
     * 插入学生活动
     *
     * @param info
     * @return
     */
    public long insert_studentActivity(StudentActivityInfo info) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put("title", info.getTitle());
        cv.put("info", info.getInfo());
        cv.put("starttime", info.getStarttime());
        cv.put("endtime", info.getEndtime());
        cv.put("address", info.getAddress());
        cv.put("img", info.getImg());
        result = mDB.insert(ACTIVITY_TABLE, "", cv);
        return result;
    }

    /**
     * 插入日程
     *
     * @param agenda
     * @return
     */
    public long insert_agenda(Agenda agenda) {

        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("title", agenda.getTitle());
        cv.put("starttime", agenda.getStarttime());
        cv.put("endtime", agenda.getEndtime());
        cv.put("notation", agenda.getNotation());
        cv.put("address", agenda.getAddress());


        result = mDB.insert(AGENDA_TABLE, "", cv);
        return result;

    }

    /**
     * 插入预定活动
     *
     * @param studentActivityInfo
     * @return
     */
    public long insert_reserve_activity(StudentActivityInfo studentActivityInfo) {

        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("title", studentActivityInfo.getTitle());
        cv.put("starttime", studentActivityInfo.getStarttime());
        cv.put("endtime", studentActivityInfo.getEndtime());
        cv.put("info", studentActivityInfo.getInfo());
        cv.put("address", studentActivityInfo.getAddress());
        cv.put("img", studentActivityInfo.getImg());

        result = mDB.insert(RESERVE_ACTIVITY_TABLE, "", cv);
        return result;

    }

    /**
     * 插入课程
     *
     * @param course
     * @return
     */
    public long insert_course(Course course) {
        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("year", course.getYear());
        cv.put("indexofsemester", course.getIndex0fSemester());
        cv.put("coursename", course.getCourseName());
        cv.put("dayofweek", course.getDayofweek());
        cv.put("startweek", course.getStartWeek());
        cv.put("endweek", course.getEndWeek());
        cv.put("startindex", course.getStartIndex());
        cv.put("numofcourse", course.getNumOfCourse());
        cv.put("address", course.getAddress());
        cv.put("teachername", course.getTeacherName());
        cv.put("notation", course.getNotation());
        result = mDB.insert(COURSE_TABLE, "", cv);
        return result;

    }

    /**
     * 插入番茄钟
     */
    public long insert_fqz(Fqz fqz) {
        long result = -1;
        openWriteLink();
        ContentValues cv = new ContentValues();
        cv.put("startoftime", fqz.startoftime);
        cv.put("monday", fqz.monday);
        cv.put("tuesday", fqz.tuesday);
        cv.put("wednesday", fqz.wednesday);
        cv.put("thursday", fqz.thursday);
        cv.put("friday", fqz.friday);
        cv.put("saturday", fqz.saturday);
        cv.put("sunday", fqz.sunday);
        result = mDB.insert(FQZ_STATICTIS, "", cv);
        return result;
    }

    /**
     * @param date %Y%m%d
     * @return
     */
//    public Fqz getFqzStatictisData(String date){
//        openReadLink();
//        Cursor cursor=null;
//        Fqz fqz;
//        cursor=mDB.query(FQZ_STATICTIS,null,);
//    }

    /**
     * 获取所有学生活动
     *
     * @return
     */
    public ArrayList<StudentActivityInfo> getAllStudentActivityInfo() {
        openReadLink();
        Cursor cursor = null;
        StudentActivityInfo studentActivityInfo;
        cursor = mDB.rawQuery("select * from user_info ORDER BY starttime asc", null);
        if (cursor != null && cursor.moveToFirst()) {
            int length = cursor.getCount();
            ArrayList<StudentActivityInfo> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {

                String title = cursor.getString(1);
                String info = cursor.getString(2);
                String starttime = cursor.getString(3);
                String endtime = cursor.getString(4);
                String address = cursor.getString(5);
                byte[] img = cursor.getBlob(cursor.getColumnIndex("img"));
                studentActivityInfo = new StudentActivityInfo(title, info, starttime, endtime, address, img);
                list.add(studentActivityInfo);
                cursor.move(1);
            }
            return list;

        } else return new ArrayList<>();
    }


    /**
     * @param date %Y%m%d
     * @return
     */
    public ArrayList<StudentActivityInfo> getReserveActivityListWithDate(String date) {


        openReadLink();
        Cursor cursor = null;
        StudentActivityInfo studentActivityInfo;
        cursor = mDB.query(RESERVE_ACTIVITY_TABLE, null, createReserveActicitySelectActionDate(), new String[]{date}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int length = cursor.getCount();
            ArrayList<StudentActivityInfo> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {

                String title = cursor.getString(1);
                String info = cursor.getString(2);
                String starttime = cursor.getString(3);
                String endtime = cursor.getString(4);
                String address = cursor.getString(5);
                byte[] img = cursor.getBlob(cursor.getColumnIndex("img"));
                studentActivityInfo = new StudentActivityInfo(title, info, starttime, endtime, address, img);
                list.add(studentActivityInfo);
                cursor.move(1);
            }
            return list;
        } else return new ArrayList<>();
    }


    /**
     * @param date %Y%m%d
     * @return
     */
    public ArrayList<Agenda> getAgendaListWithDate(String date) {


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
                String notation = cursor.getString(4);
                String address = cursor.getString(5);
                agenda = new Agenda(title, starttime, endtime, notation, address);
                list.add(agenda);
                cursor.move(1);
            }
            return list;
        } else return new ArrayList<>();
    }


    /**
     * 更新日程备注
     *
     * @param agenda   要更新的日程
     * @param notation 更新后的备注
     */
    public void updateAgendaNotation(Agenda agenda, String notation) {
        ContentValues cv = new ContentValues();
        cv.put("title", agenda.getTitle());
        cv.put("starttime", agenda.getStarttime());
        cv.put("endtime", agenda.getEndtime());
        cv.put("notation", notation);
        cv.put("address", agenda.getAddress());
        String title = agenda.getTitle();
        String starttime = agenda.getStarttime();
        mDB.update(AGENDA_TABLE, cv, "title=? and starttime=?", new String[]{title, starttime});


    }

    /**
     * 更新课程备注
     *
     * @param course   要更新的课程
     * @param notation 更新后的备注
     */
    public void updateCourseNotation(Course course, String notation) {
        ContentValues cv = new ContentValues();
        cv.put("year", course.getYear());
        cv.put("indexofsemester", course.getIndex0fSemester());
        cv.put("coursename", course.getCourseName());
        cv.put("dayofweek", course.getDayofweek());
        cv.put("startweek", course.getStartWeek());
        cv.put("endweek", course.getEndWeek());
        cv.put("startindex", course.getStartIndex());
        cv.put("numofcourse", course.getNumOfCourse());
        cv.put("address", course.getAddress());
        cv.put("teachername", course.getTeacherName());
        cv.put("notation", notation);
        String str_year = String.valueOf(course.getYear());
        String str_indexofsemester = String.valueOf(course.getIndex0fSemester());
        String str_dayofweek = String.valueOf(course.getDayofweek());
        String str_startweek = String.valueOf(course.getStartWeek());
        String str_startindex = String.valueOf(course.getStartIndex());
        mDB.update(COURSE_TABLE, cv, createCourseSelectionActionWithCourseName(), new String[]{str_year, str_indexofsemester, course.getCourseName(), str_dayofweek, str_startweek, str_startindex});
    }

    public ArrayList<Course> getAllCourses(){
        openReadLink();
        Cursor cursor = null;
        ArrayList<Course> courseList = new ArrayList<>();

        String queryStatement = String.format("select * from %s",COURSE_TABLE);
        cursor = mDB.rawQuery(queryStatement,null);

        if(cursor!=null){
            cursor.moveToFirst();
            while(cursor.moveToNext()){
                Course course = new Course(2019,1,cursor.getString(2),cursor.getInt(3),cursor.getInt(4),
                        cursor.getInt(5),cursor.getInt(6),cursor.getInt(7),cursor.getString(8), cursor.getString(9));
                courseList.add(course);
                Log.e("courseInfo",String.format("0 index is %s,2 index is %s",cursor.getString(0),cursor.getInt(2)));
            }

        }
        return courseList;
    }

    /**
     * 获取某课程
     *
     * @param year            学年
     * @param indexOfSemester 学期
     * @param week            某一周
     * @param dayofweek       星期几
     * @return 课程列表
     */
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
                String noattion = cursor.getString(10);

                int startIndex = Integer.parseInt(str_startIndex);
                int numOfCourse = Integer.parseInt(str_numOfCourse);
                int startWeek = Integer.parseInt(str_startWeek);
                int endWeek = Integer.parseInt(str_endWeek);
                Course course = new Course(year, indexOfSemester, courseName, dayofweek, startWeek, endWeek, startIndex, numOfCourse, address, teacherName, noattion);
                courseArrayList.add(course);
                cursor.move(1);
            }
        }
        return courseArrayList;
    }

    /**
     * 根据开始时间获取日程
     *
     * @param date 开始日期
     * @return
     */
    public Agenda getAgendaWithTime(String date) {
        openReadLink();
        Cursor cursor = null;
        Agenda agenda;
        cursor = mDB.query(AGENDA_TABLE, null, createAgendaSelectActionTime(), new String[]{date}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            String title = cursor.getString(1);
            String starttime = cursor.getString(2);
            String endtime = cursor.getString(3);
            String notation = cursor.getString(4);
            String address = cursor.getString(5);
            agenda = new Agenda(title, starttime, endtime, notation, address);
            return agenda;
        } else return null;
    }

    /**
     * 获取查询活动字符串
     *
     * @return
     */
    public String createActivitySelectActionDate() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("starttime");
        stringBuffer.append(">=?");
        return stringBuffer.toString();
    }

    /**
     * 获取查询日程字符串
     *
     * @return
     */
    public String createAgendaSelectActionDate() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%Y%m%d',");
        stringBuffer.append("starttime");
        stringBuffer.append(")=?");
        return stringBuffer.toString();

    }

    /**
     * 获取查询预订活动字符串
     *
     * @return
     */
    public String createReserveActicitySelectActionDate() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%Y%m%d',");
        stringBuffer.append("starttime");
        stringBuffer.append(")=?");
        return stringBuffer.toString();

    }

    /**
     * 获取查询日程字符串
     *
     * @return
     */
    public String createAgendaSelectActionTime() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%m%d%H',");
        stringBuffer.append("starttime");
        stringBuffer.append(")=?");
        return stringBuffer.toString();

    }

    /**
     * 获取查询课程字符串
     *
     * @return
     */
    public String createCourseSelectActionDayOfWeek() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("year=? and indexofsemester=? and dayofweek=? and startweek<=? and endweek>=?");
        return stringBuffer.toString();
    }

    /**
     * 获取查询课程字符串
     *
     * @return
     */
    public String createCourseSelectionActionWithCourseName() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("year=? and indexofsemester=? and coursename=? and dayofweek=? and startweek=? and startindex=?");
        return stringBuffer.toString();
    }


}
