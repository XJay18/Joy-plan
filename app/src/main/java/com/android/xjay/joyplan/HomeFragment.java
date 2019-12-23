package com.android.xjay.joyplan;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.calendarview.Calendar;
import com.android.xjay.calendarview.CalendarLayout;
import com.android.xjay.calendarview.CalendarUtil;
import com.android.xjay.calendarview.CalendarView;

import com.android.xjay.joyplan.Calendar.CustomTimeListAdapter;
import com.android.xjay.joyplan.Calendar.ScrollDisabledListView;
import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;
import com.android.xjay.joyplan.Utils.ScreenSizeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.android.xjay.joyplan.Utils.CalendarUtil.hoursBetween;

public class HomeFragment extends Fragment
        implements View.OnClickListener, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnLongClickListener {

    /**
     * 用于数据库操作。
     */
    UserDBHelper mHelper;

    /**
     * 当前context。
     */
    protected Context mContext;

    /**
     * 活动列表
     */
    private ExpandingList expandingList;

    /**
     * 添加活动事件广播接收
     */
    DynamicReceiverAddActivity dynamicReceiver;


    /**
     * 年份文字
     */

    TextView mTextYear;

    /**
     * 月份文字
     */
    TextView mTextMonthDay;

    /**
     * 农历文字
     */
    TextView mTextLunar;

    /**
     * 日期文字
     */
    TextView mTextCurrentDay;

    /**
     * 日历视图
     */
    CalendarView mCalendarView;

    /**
     * 活动页的view
     */
    View mScheduleView;

    /**
     * 添加日程的按钮
     */
    View imgAddAgenda;


    RelativeLayout mRelativeTool;
    LinearLayout mRecyclerView;

    /**
     * 块容器
     */
    ArrayList<RelativeLayout> blockContainers;

    /**
     * 滚动视图
     */
    ScrollView scrollView;

    /**
     * 时间列表
     */
    ScrollDisabledListView timeListView;

    /**
     * 年份
     */
    int mYear;

    /**
     * 用户名
     */
    private static String user_name;

    /**
     * 日历视图。
     */
    CalendarLayout mCalendarLayout;

    public static HomeFragment newInstance(String info) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        user_name = "用户名";
        return fragment;
    }

    public static HomeFragment newInstance(String info, String extraInfo) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        user_name = extraInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        /* 获取当前界面的名称 */
        String info = getArguments().getString("info");
        switch (info) {
            /* 日程页面 */
            case "日程": {
                mScheduleView = inflater.inflate(R.layout.fragment_agenda, null);

                findAllScheduleViews();

                // 接受添加课程表消息
                DynamicReceiverAddCourseTable dynamicReceiverAddCourseTable;
                IntentFilter intentFilterAddCourse = new IntentFilter();
                intentFilterAddCourse.addAction("ADD COURSE TABLE");
                dynamicReceiverAddCourseTable = new DynamicReceiverAddCourseTable();
                mContext.registerReceiver(
                        dynamicReceiverAddCourseTable, intentFilterAddCourse);
                // 接受添加目标消息
                DynamicReceiverAddGoal dynamicReceiverAddGoal;
                IntentFilter intentFilterAddGoal = new IntentFilter();
                intentFilterAddGoal.addAction("ADD GOAL");
                dynamicReceiverAddGoal = new DynamicReceiverAddGoal();
                mContext.registerReceiver(dynamicReceiverAddGoal, intentFilterAddGoal);

                // 接收添加日程消息
                DynamicReceiverAddAgenda dynamicReceiverAddAgenda;
                IntentFilter intentFilterAddAgenda = new IntentFilter();
                intentFilterAddAgenda.addAction("ADD AGENDA");
                dynamicReceiverAddAgenda = new DynamicReceiverAddAgenda();
                mContext.registerReceiver(dynamicReceiverAddAgenda, intentFilterAddAgenda);

                // 接收添加预定活动消息
                DynamicReceiverReserveActivity dynamicReceiverReserveActivity;
                IntentFilter intentFilterReserveActivity = new IntentFilter();
                intentFilterReserveActivity.addAction("RESERVE ACTIVITY");
                dynamicReceiverReserveActivity = new DynamicReceiverReserveActivity();
                mContext.registerReceiver(dynamicReceiverReserveActivity, intentFilterReserveActivity);

                scrollView.setOnLongClickListener(this);
                timeListView.setAdapter(new CustomTimeListAdapter());
                mCalendarView.setOnSelectMonthListener(new CalendarView.onSelectMonthListener() {
                    @Override
                    public void onSelectMonth() {
                        ((FragmentActivity) mContext).findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
                    }
                });
                mTextMonthDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FragmentActivity) mContext).findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
                        if (!mCalendarLayout.isExpand()) {
                            mCalendarLayout.expand();
                            return;
                        }
                        mCalendarView.showYearSelectLayout(mYear);
                        mTextLunar.setVisibility(View.GONE);
                        mTextYear.setVisibility(View.GONE);
                        mTextMonthDay.setText(String.valueOf(mYear));
                    }
                });
                mScheduleView.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCalendarView.scrollToCurrent();
                    }
                });
                imgAddAgenda.setOnClickListener(this);
<<<<<<< HEAD
=======
                imgScreenCapture.setOnClickListener(this);

>>>>>>> 4b9f2fec171bd7cf5ceda6c2150f4a074f86cb0b
                mCalendarView.setOnYearChangeListener(this);
                mCalendarView.setOnCalendarSelectListener(this);
                mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
                mYear = mCalendarView.getCurYear();
                mTextMonthDay.setText(
                        mCalendarView.getCurMonth()
                                + "月"
                                + mCalendarView.getCurDay() + "日");
                mTextLunar.setText("今日");
                mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

                updateAgenda();
                updateCourse();
                updateActivity();

                return mScheduleView;
            }
            /* 发现页面 */
            case "发现": {
                View view = inflater.inflate(R.layout.fragment_discovery, null);
                view.findViewById(R.id.ll_fqz).setOnClickListener(this);
                view.findViewById(R.id.ll_sjtb).setOnClickListener(this);
                view.findViewById(R.id.ll_sxj).setOnClickListener(this);
                view.findViewById(R.id.ll_fbhd).setOnClickListener(this);
                view.findViewById(R.id.ll_tjkcb).setOnClickListener(this);
                return view;
            }
            /* 活动页面 */
            case "活动": {
                UserDBHelper mHelper = UserDBHelper.getInstance(mContext, 1);
                //mHelper.reset();
                View view = inflater.inflate(R.layout.fragment_reserve, null);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("ADD ACTIVITY");
                dynamicReceiver = new DynamicReceiverAddActivity();
                mContext.registerReceiver(dynamicReceiver, intentFilter);
                expandingList = view.findViewById(R.id.reserve_expanding_list);
                RedrawExpandingList();
                return view;
            }
            /* 设置页面 */
            case "设置": {
                View view = inflater.inflate(R.layout.fragment_setup, null);
                ((TextView) view.findViewById(R.id.tv_setup_user_name)).setText(user_name);
                view.findViewById(R.id.ll_setup_accountnsafety).setOnClickListener(this);
                view.findViewById(R.id.ll_setup_notenfeedback).setOnClickListener(this);
                view.findViewById(R.id.ll_setup_about).setOnClickListener(this);
                return view;
            }
            default: {
                View view = inflater.inflate(R.layout.fragment_base, null);
                TextView tvInfo = view.findViewById(R.id.textView);
                tvInfo.setText(getArguments().getString("info"));
                tvInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
                    }
                });
                return view;
            }
        }
    }

    /**
     * 找到所有scheduleView的部件
     */
    private void findAllScheduleViews() {
        timeListView = mScheduleView.findViewById(R.id.time_listview);
        scrollView = mScheduleView.findViewById(R.id.scrollView_agenda);

        mTextMonthDay = mScheduleView.findViewById(R.id.tv_month_day);
        mTextYear = mScheduleView.findViewById(R.id.tv_year);
        mTextLunar = mScheduleView.findViewById(R.id.tv_lunar);
        mRelativeTool = mScheduleView.findViewById(R.id.rl_tool);
        mCalendarView = mScheduleView.findViewById(R.id.calendarView);
        mTextCurrentDay = mScheduleView.findViewById(R.id.tv_current_day);
        mCalendarLayout = mScheduleView.findViewById(R.id.calendarLayout);
        imgAddAgenda = mScheduleView.findViewById(R.id.img_add_agenda);
        imgScreenCapture = mScheduleView.findViewById(R.id.img_screen_capture);
        mRecyclerView = mScheduleView.findViewById(R.id.recyclerView);
        blockContainers = new ArrayList<>();
        RelativeLayout container = mScheduleView.findViewById(R.id.course_container0);
        blockContainers.add(container);

        container = mScheduleView.findViewById(R.id.course_container1);
        container.setOnLongClickListener(this);


        blockContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container2);
        blockContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container3);
        blockContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container4);
        blockContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container5);
        blockContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container6);
        blockContainers.add(container);
    }


    /**
     * 刷新活动列表
     */
    public void RedrawExpandingList() {

        expandingList.Clear_mContainer();
        mHelper = UserDBHelper.getInstance(mContext, 1);
        ArrayList<StudentActivityInfo> studentActivityInfos;
        studentActivityInfos = mHelper.getAllStudentActivityInfo();
        if (studentActivityInfos != null && studentActivityInfos.size() > 0) {
            int length = studentActivityInfos.size();
            for (int i = 0; i < length; i++) {
                addItem(studentActivityInfos.get(i));
            }
        }
    }

    /**
     * bitmap转drawable的函数
     *
     * @param bp 要转换的bitmap
     */
    public Drawable bitmap2Drawable(Bitmap bp) {
        //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
        Bitmap bm = bp;
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        return bd;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_fqz) {
            Intent intent = new Intent();
            intent.setClass(mContext, FqzActivity.class);
            startActivity(intent);
            Log.v("**", "right");
        } else if (view.getId() == R.id.ll_sjtb) {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), StatisticsActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_sxj) {
            Intent intent = new Intent();
            intent.setClass(mContext, SxjActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_fbhd) {
            Intent intent = new Intent();
            intent.setClass(mContext, ScheduleActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_tjkcb) {
            Intent intent = new Intent();
            intent.setClass(mContext, AddCourseTableActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_accountnsafety) {
            Intent intent = new Intent(mContext, AccountnSafetySetupActivity.class);
            intent.putExtra("user_name", user_name);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_notenfeedback) {
            Intent intent = new Intent(mContext, HelpnfeedbackSetupActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_about) {
            Intent intent = new Intent(mContext, AboutSetupActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_course) {
            if (view.getTag() instanceof Course) {
                customDialog(view.getTag());
            }
        } else if (view.getId() == R.id.btn_agenda) {
            if (view.getTag() instanceof Agenda) {
                customDialog(view.getTag());
            }
        } else if (view.getId() == R.id.img_add_agenda) {
            Intent intent = new Intent(mContext, AddAgendaActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_activity) {
            if (view.getTag() instanceof StudentActivityInfo) {
                customDialog(view.getTag());
            }
        } else if (view.getId() == R.id.img_screen_capture) {
//            Toast.makeText(mContext,"img screen",Toast.LENGTH_SHORT).show();

            int h = 0;
            Bitmap bitmap,topBitmap;
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                h += scrollView.getChildAt(i).getHeight();
                scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
            }
            // 创建对应大小的bitmap
            bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                    Bitmap.Config.ARGB_8888);
            topBitmap = Bitmap.createBitmap(mCalendarView.getWidth(),mCalendarView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            final Canvas topCanvas = new Canvas(topBitmap);
            scrollView.draw(canvas);
            mCalendarView.draw(topCanvas);

            int width = Math.max(topBitmap.getWidth(),bitmap.getWidth());
            int height = topBitmap.getHeight() + bitmap.getHeight();

//            Log.e("h",Integer.toString(topBitmap.getHeight()));

            Bitmap totalBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            Canvas totalCanvas = new Canvas(totalBitmap);
            totalCanvas.drawBitmap(topBitmap,0,0,null);
            totalCanvas.drawBitmap(bitmap,0,topBitmap.getHeight(),null);

//            Log.e("wh",Integer.toString(totalBitmap.getWidth())+"  "+Integer.toString(totalBitmap.getHeight()));

            if (totalBitmap != null) {
                try {
                    // 获取路径
                    String doc_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    // 图片文件路径
                    String filePath = doc_path + File.separator + "screenshot.png";
                    File file = new File(filePath);
                    FileOutputStream os = new FileOutputStream(file);
                    totalBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                }
            }
            Toast.makeText(mContext,String.format("照片已保存为screenshot.png"),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 添加活动块
     *
     * @param activityInfo 填充活动块的活动信息
     */
    private void addItem(StudentActivityInfo activityInfo) {
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);
        item.setTag(activityInfo);
        byte[] temp = activityInfo.getImg();
        Drawable drawable;
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            drawable = bitmap2Drawable(bitmap);
        } else {
            int res = R.drawable.cc;
            drawable = this.getResources().getDrawable(res);
        }
        String title = activityInfo.getTitle();
        String address = activityInfo.getAddress();
        String starttime = activityInfo.getStarttime();
        String info = activityInfo.getInfo();
        if (item != null) {
            item.setIndicatorColorRes(R.color.transparent);
            item.setIndicatorIcon(drawable);
            item.createSubItems(1);
            final View view = item.getSubItemView(0);

            configureSubItem(item, view, info);

            ((TextView) item.findViewById(R.id.title)).setText(title);
            ((TextView) item.findViewById(R.id.address)).setText(address);
            ((TextView) item.findViewById(R.id.starttime)).setText(starttime);

        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agenda: {
                customChooseDialog(v.getTag());
                return true;
            }
            case R.id.btn_course: {
                Intent intent = new Intent(mContext, AddAgendaActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    /**
     * 自定义弹出框，长按日程或课程块时弹出，用于选择添加日程还是目标
     *
     * @param content 内容对象，可以是日程或课程，目前只实现了日程
     */
    private void customChooseDialog(Object content) {
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_choose, null);
        Button buttonAgenda = view.findViewById(R.id.btn_choose_agenda);
        Button buttonGoal = view.findViewById(R.id.btn_choose_goal);
        buttonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content instanceof Agenda) {

                    // 得到当天的星期
                    Agenda agenda = (Agenda) content;
                    String starttime = agenda.getStarttime();
                    String date = starttime.substring(0, 10);
                    Calendar calendar = CalendarUtil.getCalendarByDateString(date);
                    int dayOfWeek = CalendarUtil.getWeekFormCalendar(calendar);

                    // 得到开始时间
                    String str_hour = starttime.substring(11, 13);
                    String str_mintue = starttime.substring(14, 16);
                    int hour = Integer.parseInt(str_hour);
                    int mintue = Integer.parseInt(str_mintue);
                    int time = hour + (mintue / 60);

                    // 得到月份日期
                    String dateWithoutYear = calendar.toStringWithoutYear();
                    dateWithoutYear = dateWithoutYear + str_hour + str_mintue;

                    String nextDateWithoutYear = CalendarUtil.getNextCalendar(calendar).toStringWithoutYear();
                    nextDateWithoutYear = nextDateWithoutYear + str_hour + str_mintue;


                    // 将日期信息放入intent中
                    Intent intent = new Intent();
                    intent.setClass(mContext, AddAgendaActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("date", dateWithoutYear);
                    bundle.putString("nextDate", nextDateWithoutYear);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }
        });

        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, AddGoalActivity.class);
                startActivity(intent);
            }
        });
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(mContext).getScreenHeight() * 0.15f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mContext).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 自定义弹出框，点击日程、课程或活动块时弹出
     *
     * @param content Agenda，Course或Activity
     */
    private void customDialog(Object content) {
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_normal, null);

        TextView tv_title = view.findViewById(R.id.tv_agenda_dialog_title);
        TextView tv_time = view.findViewById(R.id.tv_agenda_dialog_start_time);
        TextView tv_address = view.findViewById(R.id.tv_agenda_dialog_address);
        EditText editText_notation = view.findViewById(R.id.editText_agenda_dialog_notation);

        dialog.setContentView(view);
        // 使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        // 设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(mContext).getScreenHeight() * 0.4f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mContext).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        if (content instanceof Course) {
            Course course = (Course) content;

            if (course != null) {
                tv_title.setText("课程：" + course.getCourseName());
                String startIndex = String.valueOf(course.getStartIndex());
                int k = course.getStartIndex() + course.getNumOfCourse() - 1;
                String endIndex = String.valueOf(k);
                String index = startIndex + "-" + endIndex;
                String dayOfWeek = "";
                switch (course.getDayofweek()) {
                    case 1: {
                        dayOfWeek = "星期一";
                        break;
                    }
                    case 2: {
                        dayOfWeek = "星期二";
                        break;
                    }
                    case 3: {
                        dayOfWeek = "星期三";
                        break;
                    }
                    case 4: {
                        dayOfWeek = "星期四";
                        break;
                    }
                    case 5: {
                        dayOfWeek = "星期五";
                        break;
                    }
                    case 6: {
                        dayOfWeek = "星期六";
                        break;
                    }
                    case 7: {
                        dayOfWeek = "星期日";
                        break;
                    }
                }
                tv_time.setText(dayOfWeek + " " + index + "节");
                tv_address.setText(course.getAddress());
                editText_notation.setText(course.getNotation());
            } else {
                tv_title.setText("无");
                tv_time.setText("00-00-00");
                tv_address.setText("无");
            }
            dialog.show();
            view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Course course = (Course) content;
                    if (course != null) {
                        mHelper.deleteCourse(course);
                        updateCourse();
                    }
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_confirm_notation).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Course course = (Course) content;
                            if (course != null) {
                                String notation = editText_notation.getText().toString();
                                mHelper.updateCourseNotation(course, notation);
                                editText_notation.setText(notation);
                                updateCourse();
                            }
                            dialog.dismiss();
                        }
                    });
        } else if (content instanceof Agenda) {
            Agenda agenda = (Agenda) content;
            if (agenda != null) {
                tv_title.setText(agenda.getTitle());
                tv_time.setText(agenda.getStarttime());
                tv_address.setText(agenda.getAddress());
                editText_notation.setText(agenda.getNotation());
            } else {
                tv_title.setText("无");
                tv_time.setText("00-00-00");
                tv_address.setText("无");
            }
            view.findViewById(R.id.btn_delete).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Agenda agenda = (Agenda) content;
                            if (agenda != null) {
                                mHelper.deleteAgendaWithTitleAndStarttime(agenda);
                                updateAgenda();
                            }
                            dialog.dismiss();
                        }
                    });
            view.findViewById(R.id.btn_confirm_notation).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Agenda agenda = (Agenda) content;
                            String notation = editText_notation.getText().toString();
                            mHelper.updateAgendaNotation(agenda, notation);
                            editText_notation.setText(notation);
                            updateAgenda();
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        } else if (content instanceof StudentActivityInfo) {
            StudentActivityInfo studentActivityInfo = (StudentActivityInfo) content;
            if (studentActivityInfo != null) {
                tv_title.setText(studentActivityInfo.getTitle());
                tv_time.setText(studentActivityInfo.getStarttime());
                tv_address.setText(studentActivityInfo.getAddress());
                editText_notation.setText("无");
            } else {
                tv_title.setText("无");
                tv_time.setText("00-00-00");
                tv_address.setText("无");
            }
            view.findViewById(R.id.btn_delete).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StudentActivityInfo studentActivityInfo = (StudentActivityInfo) content;
                            if (studentActivityInfo != null) {
                                mHelper.deleteReserveActivity(studentActivityInfo);
                                updateActivity();
                            }
                            dialog.dismiss();
                        }
                    });
            view.findViewById(R.id.btn_confirm_notation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "无法修改", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        //update the display of courseTable
        updateAgenda();
        updateCourse();
    }

    /**
     * 刷新日程块
     */
    public void updateAgenda() {
        for (int i = 0; i < 7; i++) {
            RelativeLayout container = blockContainers.get(i);
            int count = container.getChildCount();
            for (int j = 0; j < count; j++) {
                View view = container.getChildAt(j);
                if (view != null) {
                    if (view.getId() == R.id.btn_agenda) {
                        container.removeViewAt(j);
                        j--;
                    }
                }
            }
        }
        Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
        Calendar firstDay = CalendarUtil.getStartInWeek(selectedCalendar, 1);
        for (int i = 0; i < 7; i++) {

            String dateString = firstDay.toString();
            firstDay = CalendarUtil.getNextCalendar(firstDay);
            mHelper = UserDBHelper.getInstance(getContext(), 1);
            ArrayList<Agenda> agendaArrayList = mHelper.getAgendaListWithDate(dateString);

            if (agendaArrayList != null) {
                int length = agendaArrayList.size();
                for (int j = 0; j < length; j++) {
                    Agenda agenda = agendaArrayList.get(j);

                    String str_start_time = agenda.getStarttime();
                    String str_end_time = agenda.getEndtime();
                    SimpleDateFormat simFormat =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date start_time = new Date();
                    Date end_time = new Date();
                    try {
                        start_time = simFormat.parse(str_start_time);
                        end_time = simFormat.parse(str_end_time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int hoursBetween = hoursBetween(start_time, end_time);
                    int l = hoursBetween * 100;
                    int hours = start_time.getHours();
                    float minutes = start_time.getMinutes();
                    float time = hours + minutes / 60;
                    float bias = time * 100;
                    drawAgenda(blockContainers.get(i), l, bias, agenda);
                    //int l=numOfCourse*200;
                    //drawAgenda(blockContainers.get(i),);
                }
            }
        }
    }

    /**
     * 刷新活动块
     */
    public void updateActivity() {
        for (int i = 0; i < 7; i++) {
            RelativeLayout container = blockContainers.get(i);
            int count = container.getChildCount();
            for (int j = 0; j < count; j++) {
                View view = container.getChildAt(j);
                if (view != null) {
                    if (view.getId() == R.id.btn_activity) {
                        container.removeViewAt(j);
                        j--;
                    }
                }
            }
        }
        Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
        Calendar firstDay = CalendarUtil.getStartInWeek(selectedCalendar, 1);
        for (int i = 0; i < 7; i++) {

            String dateString = firstDay.toString();
            firstDay = CalendarUtil.getNextCalendar(firstDay);
            mHelper = UserDBHelper.getInstance(getContext(), 1);
            ArrayList<StudentActivityInfo> studentActivityInfos =
                    mHelper.getReserveActivityListWithDate(dateString);

            if (studentActivityInfos != null) {
                int length = studentActivityInfos.size();
                for (int j = 0; j < length; j++) {
                    StudentActivityInfo studentActivityInfo = studentActivityInfos.get(j);

                    String str_start_time = studentActivityInfo.getStarttime();
                    String str_end_time = studentActivityInfo.getEndtime();
                    SimpleDateFormat simFormat =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date start_time = new Date();
                    Date end_time = new Date();
                    try {
                        start_time = simFormat.parse(str_start_time);
                        end_time = simFormat.parse(str_end_time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int hoursBetween = hoursBetween(start_time, end_time);
                    int l = hoursBetween * 100;
                    int hours = start_time.getHours();
                    float minutes = start_time.getMinutes();
                    float time = hours + minutes / 60;
                    float bias = time * 100;
                    drawActivity(blockContainers.get(i), l, bias, studentActivityInfo);


                    //int l=numOfCourse*200;
                    //drawAgenda(blockContainers.get(i),);


                }

            }
        }

    }

    /**
     * 刷新课程块
     */
    public void updateCourse() {
        for (int i = 0; i < 7; i++) {
            RelativeLayout container = blockContainers.get(i);
            int count = container.getChildCount();
            for (int j = 0; j < count; j++) {
                View view = container.getChildAt(j);
                if (view != null) {
                    if (view.getId() == R.id.btn_course) {
                        container.removeViewAt(j);
                        j--;
                    }
                }
            }
        }

        //find the day selected
        Calendar selectedCalendar = mCalendarView.getSelectedCalendar();

        //see which semester the day selected is
        int year = selectedCalendar.getYear();
        int month = selectedCalendar.getMonth();
        int day = selectedCalendar.getDay();
        String str_year = new Integer(year).toString();
        int indexOfSemester = 1;
        if ((month >= 8 && month <= 12) || (month >= 1 && month <= 2)) {
            indexOfSemester = 1;
        } else indexOfSemester = 2;

        String selectedDateString = selectedCalendar.toString();
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        Date selectedDate = new Date();
        Date startWeekDate = new Date();

        try {
            if (indexOfSemester == 1) {
                startWeekDate = simFormat.parse(str_year + "0902" + " " + "000000");
            } else startWeekDate = simFormat.parse(str_year + "0225" + " " + "000000");

            selectedDate = simFormat.parse(selectedDateString + " " + "000000");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //see which week the day selected is
        int days = com.android.xjay.joyplan.Utils.CalendarUtil.daysBetween(startWeekDate, selectedDate);

        //if weeks>0
        if (days > 0) {
            int week = 0;
            week = (days + 1) / 7 + 1;

            //get the database helper
            mHelper = UserDBHelper.getInstance(getContext(), 1);

            for (int i = 0; i < 7; i++) {
                //get the course of the week
                ArrayList<Course> courseArrayList = mHelper.getCourseWithDayOfWeek(year, indexOfSemester, week, i);
                int length = courseArrayList.size();
                for (int j = 0; j < length; j++) {
                    Course course = courseArrayList.get(j);
                    //the index of the class in a day
                    int index = course.getStartIndex();

                    String courseName = course.getCourseName();
                    int numOfCourse = course.getNumOfCourse();
                    int l = numOfCourse * 83;
                    int bias = 0;
                    if (index <= 4) {
                        bias = (index - 1) * 83 + 883;
                    } else if (index > 4 && index <= 8) {
                        bias = (index - 5) * 83 + 1400;
                    } else if (index > 8 && index <= 12) {
                        bias = (index - 9) * 83 + 1900;
                    }
                    drawCourse(blockContainers.get(i), l, bias, course);
                }
            }
        }

    }

    /**
     * 绘制一个课程块
     *
     * @param relativeLayout 绘制的课程块的父组件
     * @param length         课程块的长度
     * @param bias           课程块的垂直偏移量
     * @param course         填充块的课程信息
     */
    public void drawCourse(RelativeLayout relativeLayout, int length, int bias, Course course) {

        Button button = new Button(mContext);

        button.setBackgroundResource(R.drawable.btn_shape_course_blue);


        button.setTextColor(Color.WHITE);
        button.setId(R.id.btn_course);
        button.setText(course.getCourseName());
        button.setTextSize(18);
        button.setAllCaps(false);
        button.setTag(course);
        button.setOnLongClickListener(this);
        button.setOnClickListener(this);
        int l = ScreenSizeUtils.dip2px(mContext, length);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, l);
        int bia = ScreenSizeUtils.dip2px(mContext, bias);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.setMargins(0, bia, 0, 0);
        button.setLayoutParams(rlp);
        relativeLayout.addView(button);
        int a = relativeLayout.getChildCount();
    }

    /**
     * 绘制一个日程块
     *
     * @param relativeLayout 绘制的日程块的父组件
     * @param length         日程块的长度
     * @param bias           日程块的垂直偏移量
     * @param agenda         填充块的日程信息
     */
    public void drawAgenda(RelativeLayout relativeLayout, int length, float bias, Agenda agenda) {

        Button button = new Button(mContext);

        button.setBackgroundResource(R.drawable.btn_shape_agenda_green);

        button.setTextColor(Color.WHITE);
        button.setId(R.id.btn_agenda);
        button.setText(agenda.getTitle());
        button.setAllCaps(false);
        button.setTextSize(18);
        String startTime = agenda.getStarttime();

        button.setTag(agenda);
        button.setOnLongClickListener(this);
        button.setOnClickListener(this);
        int l = ScreenSizeUtils.dip2px(mContext, length);
        int bia = ScreenSizeUtils.dip2px(mContext, bias);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, l);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.setMargins(0, bia, 0, 0);
        button.setLayoutParams(rlp);
        relativeLayout.addView(button);

    }


    /**
     * 绘制一个活动块
     *
     * @param relativeLayout      绘制的活动块的父组件
     * @param length              活动块的长度
     * @param bias                活动块的垂直偏移量
     * @param studentActivityInfo 填充块的活动信息
     */
    public void drawActivity(RelativeLayout relativeLayout, int length, float bias, StudentActivityInfo studentActivityInfo) {

        Button button = new Button(mContext);

        button.setBackgroundResource(R.drawable.btn_shape_activity_orange);

        button.setTextColor(Color.WHITE);
        button.setId(R.id.btn_activity);
        button.setText(studentActivityInfo.getTitle());
        button.setAllCaps(false);
        button.setTextSize(18);
        String startTime = studentActivityInfo.getStarttime();

        button.setTag(studentActivityInfo);
        button.setOnLongClickListener(this);
        button.setOnClickListener(this);
        int l = ScreenSizeUtils.dip2px(mContext, length);
        int bia = ScreenSizeUtils.dip2px(mContext, bias);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, l);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.setMargins(0, bia, 0, 0);
        button.setLayoutParams(rlp);
        relativeLayout.addView(button);

    }


    /**
     * 接收添加活动广播并刷新活动列表
     */
    class DynamicReceiverAddActivity extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RedrawExpandingList();
        }
    }

    /**
     * 接收预定活动广播并刷新活动块
     */
    class DynamicReceiverReserveActivity extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            updateActivity();
        }
    }

    /**
     * 接收添加课程广播并添加课程表
     */
    class DynamicReceiverAddCourseTable extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            addCourseTable();
        }
    }

    /**
     * 接收添加目标广播并添加目标
     */
    class DynamicReceiverAddGoal extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            addGoal();
        }
    }

    /**
     * 接收添加日程广播并添加日程
     */
    class DynamicReceiverAddAgenda extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            updateAgenda();
        }
    }

    /**
     * 添加目标
     */
    private void addGoal() {
        mHelper=UserDBHelper.getInstance(getContext(),1);
        mHelper.getAllStudentActivityInfo();

    }

    /**
     * 添加课程表
     */
    private void addCourseTable() {
        mHelper = UserDBHelper.getInstance(getContext(), 1);
        mHelper.resetCourseTable();
    }

    /**
     * 注册某个活动块到活动列表中
     * @param item
     * @param view
     * @param info
     */
    private void configureSubItem(final CustomItem item, final View view, String info) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(info);

    }
}
