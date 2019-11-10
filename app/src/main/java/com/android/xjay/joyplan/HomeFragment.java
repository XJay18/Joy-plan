package com.android.xjay.joyplan;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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

import com.android.xjay.calendarview.Calendar;
import com.android.xjay.calendarview.CalendarLayout;
import com.android.xjay.calendarview.CalendarUtil;
import com.android.xjay.calendarview.CalendarView;
import com.android.xjay.calendarview.YearViewPager;
import com.android.xjay.joyplan.Calendar.CustomTimeListAdapter;
import com.android.xjay.joyplan.Calendar.ScrollDisabledListView;
import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;
import com.android.xjay.joyplan.Utils.ScreenSizeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener, CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, View.OnLongClickListener {

    UserDBHelper mHelper;

    ArrayList<ArrayList<String>> AgendaTitleArrayList;
    ArrayList<ArrayList<String>> CourseArrayList;
    protected Context mContext;
    private ExpandingList expandingList;
    DynamicReceiverAddActivity dynamicReceiver;
    String[] TITLES;
    String[] INFOS;
    String[] STARTTIMES;
    //String[] ENDTIMES;
    String[] ADDRESSES;


    //used by fragment_agenda
    TextView mTextMonthDay;

    View mScheduleView;

    View imgAddAgenda;

    YearViewPager mYearViewPager;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;

    LinearLayout mRecyclerView;

    ArrayList<RelativeLayout> courseContainers;


    ScrollView scrollView;

    ScrollDisabledListView timeListView;
    LinearLayout linearLayout;


    int mYear;
    CalendarLayout mCalendarLayout;

    public static HomeFragment newInstance(String info) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        // get the name of this fragment (Agenda or Activity or Discovery or Setup)
        String info = getArguments().getString("info");
        switch (info) {
            case "日程": {

                mScheduleView = inflater.inflate(R.layout.fragment_agenda, null);

                findAllScheduleViews();

                //on receive add course table event
                DynamicReceiverAddCourseTable dynamicReceiverAddCourseTable;
                IntentFilter intentFilterAddCourse = new IntentFilter();
                intentFilterAddCourse.addAction("ADD COURSE TABLE");
                dynamicReceiverAddCourseTable = new DynamicReceiverAddCourseTable();
                mContext.registerReceiver(
                        dynamicReceiverAddCourseTable, intentFilterAddCourse);
                //on add goal event
                DynamicReceiverAddGoal dynamicReceiverAddGoal;
                IntentFilter intentFilterAddGoal = new IntentFilter();
                intentFilterAddGoal.addAction("ADD GOAL");
                dynamicReceiverAddGoal = new DynamicReceiverAddGoal();
                mContext.registerReceiver(dynamicReceiverAddGoal, intentFilterAddGoal);

                DynamicReceiverAddAgenda dynamicReceiverAddAgenda;
                IntentFilter intentFilterAddAgenda=new IntentFilter();
                intentFilterAddAgenda.addAction("ADD AGENDA");
                dynamicReceiverAddAgenda=new DynamicReceiverAddAgenda();
                mContext.registerReceiver(dynamicReceiverAddAgenda,intentFilterAddAgenda);


                //((FragmentActivity)mContext).findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                //initView()

                /*listView1 = mScheduleView.findViewById(R.id.listView1);
                listView2 = mScheduleView.findViewById(R.id.listView2);
                listView3 = mScheduleView.findViewById(R.id.listView3);
                listView4 = mScheduleView.findViewById(R.id.listView4);
                listView5 = mScheduleView.findViewById(R.id.listView5);
                listView6 = mScheduleView.findViewById(R.id.listView6);
                listView7 = mScheduleView.findViewById(R.id.listView7);*/


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
                return mScheduleView;
            }
            // deal with the fragment_discovery
            case "发现": {
                View view = inflater.inflate(R.layout.fragment_discovery, null);
//                ((FragmentActivity)mContext).findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.ll_fqz).setOnClickListener(this);
                view.findViewById(R.id.ll_sjtb).setOnClickListener(this);
                view.findViewById(R.id.ll_sxj).setOnClickListener(this);
                view.findViewById(R.id.ll_fbhd).setOnClickListener(this);
                view.findViewById(R.id.ll_tjkcb).setOnClickListener(this);
                return view;

            }
            case "活动": {
                UserDBHelper mHelper = UserDBHelper.getInstance(mContext, 1);
                //mHelper.reset();
                View view = inflater.inflate(R.layout.fragment_reserve, null);
//                ((FragmentActivity)mContext).findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("ADD ACTIVITY");
                dynamicReceiver = new DynamicReceiverAddActivity();
                mContext.registerReceiver(dynamicReceiver, intentFilter);
                expandingList = view.findViewById(R.id.reserve_expanding_list);
                RedrawExpandingList();

                Button btn_changeTo_addActivity = view.findViewById(R.id.changeButton_reserve);
                btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                return view;
            }
            case "设置": {
                View view = inflater.inflate(R.layout.fragment_setup, null);
//                ((FragmentActivity)mContext).findViewById(R.id.activity_main_toolbar).setVisibility(View.GONE);
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
        imgAddAgenda=mScheduleView.findViewById(R.id.img_add_agenda);

        mRecyclerView = mScheduleView.findViewById(R.id.recyclerView);
        courseContainers = new ArrayList<>();
        RelativeLayout container = mScheduleView.findViewById(R.id.course_container0);
        courseContainers.add(container);

        container=mScheduleView.findViewById(R.id.course_container1);
        container.setOnLongClickListener(this);


        courseContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container2);
        courseContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container3);
        courseContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container4);
        courseContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container5);
        courseContainers.add(container);
        container = mScheduleView.findViewById(R.id.course_container6);
        courseContainers.add(container);
    }

     private void initScrollDisabledListView() {
        AgendaTitleArrayList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayList<String> courseList = new ArrayList<String>();
            for (int j = 0; j < 24; j++) {
                arrayList.add("");
                courseList.add("");
            }
            AgendaTitleArrayList.add(arrayList);



        }

        Calendar calendar = mCalendarView.getSelectedCalendar();
        Calendar weekStartCalendar = CalendarUtil.getStartInWeek(calendar, 1);
    }




    public void RedrawExpandingList() {
        //TODO
        expandingList.Clear_mContainer();
        TITLES = new String[100];

        INFOS = new String[100];

        STARTTIMES = new String[100];

        //ENDTIMES=new String[100];

        ADDRESSES = new String[100];
        Cursor c;
        // mHelper.reset();
        mHelper = UserDBHelper.getInstance(getContext(), 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        int length = c.getCount();
        c.moveToFirst();
        int iconRes = R.drawable.duck;
        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1);
            INFOS[i] = c.getString(2);
            STARTTIMES[i] = c.getString(3);
//            ENDTIMES[i]=c.getString(4).toString();
            ADDRESSES[i] = c.getString(5);
            String[] s = new String[]{INFOS[i]};
            addItem(TITLES[i], INFOS[i], STARTTIMES[i], ADDRESSES[i], R.color.transparent, iconRes);
            c.move(1);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_fqz) {
//            Toast.makeText(mContext,"你点击了番茄钟",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(mContext, FqzActivity.class);
            startActivity(intent);
            Log.v("**", "right");
        } else if (view.getId() == R.id.ll_sjtb) {
//            Toast.makeText(mContext,"你点击了数据图表",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this.getContext(), StatisticsActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_sxj) {
//            Toast.makeText(mContext,"你点击了随心记",Toast.LENGTH_SHORT).show();
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
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_notenfeedback) {
            Intent intent = new Intent(mContext, HelpnfeedbackSetupActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_about) {
            Intent intent = new Intent(mContext, AboutSetupActivity.class);
            startActivity(intent);
        } else if(view.getId()==R.id.btn_course){
            if(view.getTag() instanceof Course) {

                customDialog(view.getTag());
            }
        }else if(view.getId()==R.id.btn_agenda){

            if(view.getTag() instanceof Agenda){
                customDialog(view.getTag());
            }
        }else if(view.getId()==R.id.img_add_agenda){
            Intent intent=new Intent(mContext, AddAgendaActivity.class);
            startActivity(intent);
        }

    }

    private void addItem(String title, String info, String starttime,
                         String address, int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);
        String Date = starttime.substring(0, 10);
        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            item.createSubItems(1);
            final View view = item.getSubItemView(0);
            //Let's set some values in
            configureSubItem(item, view, info);
            //It is possible to get any view inside the inflated layout. Let's set the text in the custom_item
            ((TextView) item.findViewById(R.id.title)).setText(title);
            ((TextView) item.findViewById(R.id.address)).setText(address);
            ((TextView) item.findViewById(R.id.starttime)).setText(Date);
            //We can create items in batch.


            /*item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandingList.removeItem(item);
                }
            });*/
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.btn_agenda:{
                customChooseDialog(v.getTag());
            }
            case R.id.btn_course:{
                Intent intent=new Intent(mContext, AddAgendaActivity.class);
                startActivity(intent);
                return true;
            }

        }
        return false;
    }

    private void customChooseDialog(Object content) {
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_choose, null);
        Button buttonAgenda = view.findViewById(R.id.btn_choose_agenda);
        Button buttonGoal = view.findViewById(R.id.btn_choose_goal);
        buttonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content instanceof Agenda){

                    //get day of week by starttime
                    Agenda agenda=(Agenda)content;
                    String starttime=agenda.start_time;
                    String date=starttime.substring(0,10);
                    Calendar calendar=CalendarUtil.getCalendarByDateString(date);
                    int dayOfWeek=CalendarUtil.getWeekFormCalendar(calendar);

                    //get time in a day by starttime
                    String str_hour=starttime.substring(11,13);
                    String str_mintue=starttime.substring(14,16);
                    int hour=Integer.parseInt(str_hour);
                    int mintue=Integer.parseInt(str_mintue);
                    int time=hour+(mintue/60);

                    //get String without year MMDD
                    String dateWithoutYear=calendar.toStringWithoutYear();
                    dateWithoutYear=dateWithoutYear+str_hour+str_mintue;

                    String nextDateWithoutYear=CalendarUtil.getNextCalendar(calendar).toStringWithoutYear();
                    nextDateWithoutYear=nextDateWithoutYear+str_hour+str_mintue;

                    Intent intent=new Intent();
                    intent.setClass(mContext, AddAgendaActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("date", dateWithoutYear);
                    bundle.putString("nextDate",nextDateWithoutYear);
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

    private void customDialog(Object content) {
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_normal, null);


        TextView tv_title = view.findViewById(R.id.tv_agenda_dialog_title);
        TextView tv_time = view.findViewById(R.id.tv_agenda_dialog_start_time);
        TextView tv_address=view.findViewById(R.id.tv_agenda_dialog_address);
        EditText editText_notation = view.findViewById(R.id.editText_agenda_dialog_notation);


        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(mContext).getScreenHeight() * 0.4f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(mContext).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);


        if(content instanceof Course){
            Course course=(Course)content;

            if(course!=null){
                tv_title.setText("课程："+course.courseName);
                String startIndex=String.valueOf(course.startIndex);
                int k=course.startIndex+course.numOfCourse-1;
                String endIndex=String.valueOf(k);
                String index=startIndex+"-"+endIndex;
                String dayOfWeek="";
                switch (course.dayofweek){
                    case 1:{
                        dayOfWeek="星期一";
                        break;
                    }
                    case 2: {
                        dayOfWeek="星期二";
                        break;
                    }
                    case 3:{
                        dayOfWeek="星期三";
                        break;
                    }
                    case 4:{
                        dayOfWeek="星期四";
                        break;
                    }
                    case 5:{
                        dayOfWeek="星期五";
                        break;
                    }
                    case 6:{
                        dayOfWeek="星期六";
                        break;
                    }
                    case 7:{
                        dayOfWeek="星期日";
                        break;
                    }
                }
                tv_time.setText(dayOfWeek+" "+index+"节");
                tv_address.setText(course.address);
                editText_notation.setText(course.notation);

            }
            else{
                tv_title.setText("无");
                tv_time.setText("00-00-00");
                tv_address.setText("无");
            }
            dialog.show();

            view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Course course=(Course)content;
                    if(course!=null){
                        mHelper.deleteCourse(course);
                        updateCourse();
                    }

                    dialog.dismiss();

                }
            });
            view.findViewById(R.id.btn_confirm_notation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Course course=(Course)content;
                    if(course!=null){
                        String notation=editText_notation.getText().toString();
                        mHelper.updateCourseNotation(course,notation);
                        editText_notation.setText(notation);
                        updateCourse();
                    }

                    dialog.dismiss();

                }
            });
        }
        else if(content instanceof Agenda){
            Agenda agenda=(Agenda)content;
            if (agenda != null) {
                tv_title.setText(agenda.title);
                tv_time.setText(agenda.start_time);
                tv_address.setText(agenda.address);
                editText_notation.setText(agenda.notation);
            } else {
                tv_title.setText("无");
                tv_time.setText("00-00-00");
                tv_address.setText("无");
            }
            view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Agenda agenda=(Agenda)content;
                    if(agenda!=null){
                        mHelper.deleteAgendaWithTitleAndStarttime(agenda.title,agenda.start_time);
                        updateAgenda();
                    }



                    //Calendar calendar=mCalendarView.getSelectedCalendar();
                    //Calendar weekStartCalenddar=CalendarUtil.getStartInWeek(calendar,1);

                    dialog.dismiss();

                }
            });
            view.findViewById(R.id.btn_confirm_notation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Agenda agenda=(Agenda)content;
                    String notation=editText_notation.getText().toString();
                    mHelper.updateAgendaNotation(agenda,notation);
                    editText_notation.setText(notation);
                    updateAgenda();
                    dialog.dismiss();;

                }
            });

            dialog.show();
        }





    }


    //返回当前选择日期所在周的第一天
    public Calendar getListCilckedCalendar(int index) {
        Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
        Calendar weekStartCalendar = CalendarUtil.getStartInWeek(selectedCalendar, 1);
        for (int i = 0; i < index; i++) {
            weekStartCalendar = CalendarUtil.getNextCalendar(weekStartCalendar);
        }
        return weekStartCalendar;
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


    public void updateAgenda(){
        for(int i=0;i<7;i++){
            RelativeLayout container=courseContainers.get(i);
            int count=container.getChildCount();
            for(int j=0;j<count;j++){
                View view=container.getChildAt(j);
                if(view!=null){
                    if(view.getId()==R.id.btn_agenda){
                        container.removeViewAt(j);
                        j--;
                    }
                }

            }

        }
        Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
        Calendar firstDay=CalendarUtil.getStartInWeek(selectedCalendar,1);
        for(int i=0;i<7;i++) {

            String dateString = firstDay.toString();
            firstDay=CalendarUtil.getNextCalendar(firstDay);
            mHelper=UserDBHelper.getInstance(getContext(),1);
            ArrayList<Agenda> agendaArrayList=mHelper.getAgendaListWithDate(dateString);


            if(agendaArrayList!=null){
                int length=agendaArrayList.size();
                for(int j=0;j<length;j++){
                    Agenda agenda=agendaArrayList.get(j);

                    String str_start_time=agenda.start_time;
                    String str_end_time=agenda.end_time;
                    SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date start_time=new Date();
                    Date end_time=new Date();
                    try {
                        start_time=simFormat.parse(str_start_time);
                        end_time=simFormat.parse(str_end_time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int hoursBetween = com.android.xjay.joyplan.Utils.CalendarUtil.hoursBetween(start_time, end_time);
                    int l=hoursBetween*100;
                    int hours=start_time.getHours();
                    float minutes=start_time.getMinutes();
                    float time=hours+minutes/60;
                    float bias=time*100;
                    drawAgenda(courseContainers.get(i),l,bias,agenda);


                    //int l=numOfCourse*200;
                    //drawAgenda(courseContainers.get(i),);



                }

            }
        }







    }
    public void updateCourse(){
        for(int i=0;i<7;i++){
            RelativeLayout container=courseContainers.get(i);
            int count=container.getChildCount();
            for(int j=0;j<count;j++){
                View view=container.getChildAt(j);
                if(view!=null){
                    if(view.getId()==R.id.btn_course){
                        container.removeViewAt(j);
                        j--;
                    }
                }

            }

        }


        //find the day selected
        Calendar selectedCalendar=mCalendarView.getSelectedCalendar();


        //see which semester the day selected is


        int year=selectedCalendar.getYear();
        int month=selectedCalendar.getMonth();
        int day=selectedCalendar.getDay();
        String str_year=new Integer(year).toString();

        int indexOfSemester= 1;
        if((month>=8&&month<=12)||(month>=1&&month<=2)){
            indexOfSemester=1;
        }
        else indexOfSemester=2;

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

            for(int i=0;i<7;i++){
                //get the course of the week
                ArrayList<Course> courseArrayList = mHelper.getCourseWithDayOfWeek(year, indexOfSemester, week, i);
                int length = courseArrayList.size();
                for (int j = 0; j < length; j++) {
                    Course course=courseArrayList.get(j);
                    //the index of the class in a day
                    int index = course.startIndex;

                    String courseName=course.courseName;
                    int numOfCourse=course.numOfCourse;
                    int l=numOfCourse*83;
                    int bias=0;
                    if(index<=4){
                        bias=(index-1)*83+883;
                    }
                    else if(index>4&&index<=8){
                        bias=(index-5)*83+1400;
                    }
                    else if(index>8&&index<=12){
                        bias=(index-9)*83+1900;
                    }
                    drawCourse(courseContainers.get(i),l,bias,course);

                }
            }

        }

    }



    public void drawCourse(RelativeLayout relativeLayout, int length, int bias, Course course){

        Button button=new Button(mContext);


            button.setBackgroundResource(R.drawable.btn_shape_agenda_blue);


        button.setTextColor(Color.WHITE);
        button.setId(R.id.btn_course);
        button.setText(course.courseName);
        button.setTextSize(18);
        button.setAllCaps(false);
        button.setTag(course);
        button.setOnLongClickListener(this);
        button.setOnClickListener(this);
        int l=ScreenSizeUtils.dip2px(mContext,length);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,l);
        int bia=ScreenSizeUtils.dip2px(mContext,bias);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.setMargins(0, bia, 0, 0);
        button.setLayoutParams(rlp);
        relativeLayout.addView(button);
        int a=relativeLayout.getChildCount();
    }



    public void drawAgenda(RelativeLayout relativeLayout, int length, float bias, Agenda agenda){

        Button button=new Button(mContext);

        button.setBackgroundResource(R.drawable.btn_shape_agenda_green);

        button.setTextColor(Color.WHITE);
        button.setId(R.id.btn_agenda);
        button.setText(agenda.title);
        button.setAllCaps(false);
        button.setTextSize(18);
        String startTime=agenda.start_time;

        button.setTag(agenda);
        button.setOnLongClickListener(this);
        button.setOnClickListener(this);
        int l=ScreenSizeUtils.dip2px(mContext,length);
        int bia=ScreenSizeUtils.dip2px(mContext,bias);
        RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,l);

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rlp.setMargins(0, bia, 0, 0);
        button.setLayoutParams(rlp);
        relativeLayout.addView(button);

    }




            /**
             * method to accept Broadcast and refresh the ExpandingList
             */
            class DynamicReceiverAddActivity extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                    RedrawExpandingList();
                }
            }

            class DynamicReceiverAddCourseTable extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                    addCourseTable();
                }
            }

            class DynamicReceiverAddGoal extends BroadcastReceiver {
                public void onReceive(Context context, Intent intent) {
                    addGoal();
                }
            }

            class DynamicReceiverAddAgenda extends BroadcastReceiver{
                public void onReceive(Context context, Intent intent){updateAgenda();}
            }

            private void addGoal () {

                String title = "Running";
                String address = "田径场";
                String content = "";
                String start_time = "2019-05-22 20:00";
                String end_time = "2019-05-22 20:00";
                Agenda agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-23 20:00";
                end_time = "2019-05-23 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-24 20:00";
                end_time = "2019-05-24 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-25 20:00";
                end_time = "2019-05-25 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-26 20:00";
                end_time = "2019-05-26 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-27 20:00";
                end_time = "2019-05-27 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);

                title = "Running";
                address = "田径场";
                content = "";
                start_time = "2019-05-28 20:00";
                end_time = "2019-05-28 20:00";
                agenda = new Agenda(title, start_time, end_time, content, address);
                mHelper.insert_agenda(agenda);
            }

            private void addCourseTable () {


                mHelper = UserDBHelper.getInstance(getContext(), 1);
                mHelper.resetCourseTable();

                mHelper.insert_course(new Course(2019, 1, "定向越野", 1, 1, 15, 5, 2, "田径场", "老师"));
                mHelper.insert_course(new Course(2019, 1, "毛概", 1, 7, 15, 1, 2, "田径场", "老师"));
                mHelper.insert_course(new Course(2019, 1, "毛概阿肯德基咖喱块附近的光华路科技刻录机ad", 2, 7, 15, 9, 3, "田径场", "老师"));
                mHelper.insert_course(new Course(2019, 1, "毛概", 2, 7, 15, 2, 2, "田径场", "老师"));
                mHelper.insert_course(new Course(2019, 1, "毛概", 3, 7, 15, 2, 2, "田径场", "老师"));
                /*mHelper.insert_course(new Course("UML",2,3,2));
                mHelper.insert_course(new Course("UML",2,3,2));
                mHelper.insert_course(new Course("编译技术",2,5,2));
                mHelper.insert_course(new Course("大学美育",2,9,2));
                mHelper.insert_course(new Course("计网",3,1,2));
                mHelper.insert_course(new Course("数据库",3,5,2));
                mHelper.insert_course(new Course("毛概",3,7,2));
                mHelper.insert_course(new Course("大学语文",3,9,2));
                mHelper.insert_course(new Course("UML",4,3,2));
                mHelper.insert_course(new Course("计网",5,1,2));
                mHelper.insert_course(new Course("数据库",5,5,2));*/

            }

            private void configureSubItem ( final CustomItem item, final View view, String info){
                ((TextView) view.findViewById(R.id.sub_title)).setText(info);

            }


            private void showInsertDialog ( final ReserveActivity.OnItemCreated positive){
                final EditText text = new EditText(mContext);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(text);
                builder.setTitle("enter_title");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        positive.itemCreated(text.getText().toString());
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.show();
            }

            interface OnItemCreated {
                void itemCreated(String title);
            }
        }


