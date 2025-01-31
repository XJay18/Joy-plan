package com.android.xjay.joyplan.Utils;

import android.content.Context;
import android.util.Log;
import java.util.Calendar;

import com.android.xjay.joyplan.Course;
import com.android.xjay.joyplan.PhoneNumber;
import com.android.xjay.joyplan.UserDBHelper;
import com.android.xjay.joyplan.web.WebServiceGet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 将excel内容保存至日程
 */
public class POIExcelProcesser {
    /**
     * 课程名称
     */
    private static List<String> course_names = new ArrayList<>();
    /**
     * 课程日期
     */
    private static List<Integer> course_weekdays = new ArrayList<>();
    /**
     * 课程的开始时间
     */
    private static List<Integer> course_begin_times = new ArrayList<>();
    /**
     * 课程的结束时间
     */
    private static List<Integer> course_end_times = new ArrayList<>();
    /**
     * 课程开始的周数
     */
    private static List<Integer> course_begin_weeks = new ArrayList<>();
    /**
     * 课程结束的周数
     */
    private static List<Integer> course_end_weeks = new ArrayList<>();
    /**
     * 课程的教室
     */
    private static List<String> classrooms = new ArrayList<>();
    /**
     * 课程的教师
     */
    private static List<String> teachers = new ArrayList<>();

    /**
     * 添加课程到数据库
     */
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
                PhoneNumber PN = PhoneNumber.getInstance();
                String userid = PN.getPhone_number();
                Calendar calendar = Calendar.getInstance();
                WebServiceGet.addCourseGet(userid, Integer.toString(calendar.get(Calendar.YEAR)),"1",course_names.get(i).toString(),course_weekdays.get(i).toString(),
                        course_begin_weeks.get(i).toString(),course_end_weeks.get(i).toString(),course_begin_times.get(i).toString(),
                        Integer.toString(course_end_times.get(i) - course_begin_times.get(i)), classrooms.get(i),teachers.get(i),null);
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

    /**
     * 获取excel的工作本
     */
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

    /**
     * 提取课程
     */
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

    /**
     * 提取一门课程的信息
     */
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

    public static void writeCourseToExcel(String filePath, Context mContext, ArrayList<Course> courseList){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            Workbook workbook = getWorkbook(filePath);
            XSSFSheet xssfSheet = (XSSFSheet) workbook.getSheetAt(0);
            Row row = xssfSheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(0);
            cell = row.createCell(1);
            cell.setCellValue(10086);

            row = xssfSheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(500);
            cell = row.createCell(1);
            cell.setCellValue(200);

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
