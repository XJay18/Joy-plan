package com.android.xjay.joyplan.Utils;

import android.content.Context;
import android.util.Log;

import com.android.xjay.joyplan.Course;
import com.android.xjay.joyplan.UserDBHelper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * help to read excel's courses and add to schedule
 */
public class POIExcelProcesser {
    /** the name of courses */
    private static List<String> course_names = new ArrayList<>();
    /** the day of courses */
    private static List<Integer> course_weekdays = new ArrayList<>();
    /** the begin time of courses */
    private static List<Integer> course_begin_times = new ArrayList<>();
    /** the end time of courses */
    private static List<Integer> course_end_times = new ArrayList<>();
    /** the begin week of courses */
    private static List<Integer> course_begin_weeks = new ArrayList<>();
    /** the end week of courses */
    private static List<Integer> course_end_weeks = new ArrayList<>();
    /** the classroom of courses */
    private static List<String> classrooms = new ArrayList<>();
    /** the teacher of courses */
    private static List<String> teachers = new ArrayList<>();

    /** add the courses from excel to database */
    public static void setExceltoSchedule(String filepath, Context mContext) {
        Workbook workbook = null;
        //假定xslx文件在/mnt/sdcard/download目录下
        String[] fileNameFragments = filepath.split(File.separator);
        filepath = File.separator + "mnt" + File.separator + "sdcard";
        for (int i = 4; i < fileNameFragments.length; i++) {
            filepath += File.separator + fileNameFragments[i];
        }

        try {
            workbook = getWorkbook(filepath);
            exec(workbook);

            UserDBHelper userDBHelper = UserDBHelper.getInstance(mContext, 1);
            Course course = null;
            for (int i = 0; i < course_names.size(); i++) {
                course = new Course(2019, 1, course_names.get(i), course_weekdays.get(i), course_begin_weeks.get(i), course_end_weeks.get(i), course_begin_times.get(i),
                        course_end_times.get(i) - course_begin_times.get(i), classrooms.get(i), teachers.get(i));
                userDBHelper.insert_course(course);
            }

//            course=new Course("编译原理",3,4,15,9,3,"a","changdalao ");
//            userDBHelper.insert_course(course);
//            course=new Course("编译原理2",5,4,15,9,3,"a","changdalao ");
//            userDBHelper.insert_course(course);
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("error:", "workbook created failed");
        }
    }

    /** get the workbook of excel */
    private static Workbook getWorkbook(String filepath) throws IOException {
        Workbook workbook = null;
        if (!filepath.endsWith(".xlsx")) {
            return null;
        }
        Log.e("excel path", filepath);
        InputStream InputStream = new FileInputStream(filepath);

        workbook = new XSSFWorkbook(InputStream);
        return workbook;
    }

    /** extract all courses */
    private static void exec(Workbook workbook) {
        XSSFSheet xssfSheet = (XSSFSheet) workbook.getSheetAt(0);
        List<CellRangeAddress> list_cellrangeaddress = xssfSheet.getMergedRegions();
        List<String> list_cellstring = new ArrayList<String>();
        for (CellRangeAddress ca : list_cellrangeaddress) {
            if (ca.getFirstColumn() <= 9 && ca.getFirstColumn() >= 3) {
                Row row = xssfSheet.getRow(ca.getFirstRow());
                Cell cell = row.getCell(ca.getFirstColumn());
                getAllInfo(cell.toString(), ca.getFirstColumn() - 1);
                list_cellstring.add(cell.toString());
            }
        }
    }

    /** extract one course's information */
    private static void getAllInfo(String course_info, int weekday) {
        if (course_info != null) {
            String course_name = null;
            Integer begin_time = null;
            Integer end_time = null;
            Integer begin_week = null;
            Integer end_week = null;
            String classroom = null;
            String teacher = null;
            String temp = null;

            int index = course_info.indexOf("(");
            if (index < 0 || index == course_info.length() - 1) {
                return;
            }
            course_name = course_info.substring(0, index);
            course_name = course_name.trim();
            course_info = course_info.substring(index + 1);

            index = course_info.indexOf(")");
            if (index < 0 || index == course_info.length() - 1) {
                return;
            }
            temp = course_info.substring(0, index);
            begin_time = Integer.parseInt(temp.substring(0, temp.indexOf('-')));
            end_time = Integer.parseInt(temp.substring(temp.indexOf('-') + 1, temp.indexOf('节')));
            course_info = course_info.substring(index + 1);

            index = course_info.indexOf('/');
            if (index < 0 || index == course_info.length() - 1) {
                return;
            }
            temp = course_info.substring(0, index);
            begin_week = Integer.parseInt(temp.substring(0, temp.indexOf('-')));
            end_week = Integer.parseInt(temp.substring(temp.indexOf('-') + 1, temp.indexOf('周')));
            course_info = course_info.substring(index + 1);

            index = course_info.indexOf('/');
            if (index < 0 || index == course_info.length() - 1) {
                return;
            }
            classroom = course_info.substring(0, index);
            course_info = course_info.substring(index + 1);

            index = course_info.indexOf('/');
            if (index < 0 || index == course_info.length() - 1) {
                return;
            }
            teacher = course_info.substring(0, index);

            course_names.add(course_name);
            course_begin_times.add(begin_time);
            course_end_times.add(end_time);
            course_begin_weeks.add(begin_week);
            course_end_weeks.add(end_week);
            classrooms.add(classroom);
            teachers.add(teacher);
            course_weekdays.add(weekday);
        }
        return;
    }
}
