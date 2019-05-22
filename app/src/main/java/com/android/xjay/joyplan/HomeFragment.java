package com.android.xjay.joyplan;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.android.xjay.calendarview.Calendar;
import com.android.xjay.calendarview.CalendarLayout;
import com.android.xjay.calendarview.CalendarUtil;
import com.android.xjay.calendarview.CalendarView;
import com.android.xjay.calendarview.YearViewPager;
import com.android.xjay.joyplan.Calendar.CustomListAdapter;
import com.android.xjay.joyplan.Calendar.CustomTimeListAdapter;
import com.android.xjay.joyplan.Calendar.ScrollDisabledListView;
import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;
import com.android.xjay.joyplan.Utils.ScreenSizeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener, CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener, View.OnLongClickListener {

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
    ArrayList<CustomListAdapter> adapterArrayList = new ArrayList<>();

    //used by fragment_agenda
    TextView mTextMonthDay;

    YearViewPager mYearViewPager;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;

    LinearLayout mRecyclerView;

    ScrollView scrollView;
    ScrollDisabledListView timeListView;
    ScrollDisabledListView listView1;
    ScrollDisabledListView listView2;
    ScrollDisabledListView listView3;
    ScrollDisabledListView listView4;
    ScrollDisabledListView listView5;
    ScrollDisabledListView listView6;
    ScrollDisabledListView listView7;
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
                DynamicReceiverAddCourseTable dynamicReceiverAddCourseTable;
                IntentFilter intentFilterAddCourse = new IntentFilter();
                intentFilterAddCourse.addAction("ADD COURSE TABLE");
                dynamicReceiverAddCourseTable=new DynamicReceiverAddCourseTable();
                mContext.registerReceiver(dynamicReceiverAddCourseTable, intentFilterAddCourse);

                DynamicReceiverAddGoal dynamicReceiverAddGoal;
                IntentFilter intentFilterAddGoal=new IntentFilter();
                intentFilterAddGoal.addAction("ADD GOAL");
                dynamicReceiverAddGoal=new DynamicReceiverAddGoal();
                mContext.registerReceiver(dynamicReceiverAddGoal,intentFilterAddGoal);

                View view = inflater.inflate(R.layout.fragment_agenda, null);
//                ((FragmentActivity)mContext).findViewById(R.id.activity_main_toolbar).setVisibility(View.VISIBLE);
                //initView()
                timeListView = view.findViewById(R.id.time_listview);
                listView1 = view.findViewById(R.id.listView1);
                listView2 = view.findViewById(R.id.listView2);
                listView3 = view.findViewById(R.id.listView3);
                listView4 = view.findViewById(R.id.listView4);
                listView5 = view.findViewById(R.id.listView5);
                listView6 = view.findViewById(R.id.listView6);
                listView7 = view.findViewById(R.id.listView7);

                scrollView = view.findViewById(R.id.scrollView_agenda);
                scrollView.setOnLongClickListener(this);

                listView1.setTag(1);
                listView2.setTag(2);
                listView3.setTag(3);
                listView4.setTag(4);
                listView5.setTag(5);
                listView6.setTag(6);
                listView7.setTag(7);

                timeListView.setAdapter(new CustomTimeListAdapter());

                mTextMonthDay = view.findViewById(R.id.tv_month_day);
                mTextYear =  view.findViewById(R.id.tv_year);
                mTextLunar =  view.findViewById(R.id.tv_lunar);
                mRelativeTool =  view.findViewById(R.id.rl_tool);
                mCalendarView =  view.findViewById(R.id.calendarView);
                mTextCurrentDay =  view.findViewById(R.id.tv_current_day);
                mCalendarView.setOnSelectMonthListener(new CalendarView.onSelectMonthListener() {
                    @Override
                    public void onSelectMonth() {
                        ((FragmentActivity)mContext).findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
                    }
                });

                mTextMonthDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FragmentActivity)mContext).findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
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
                view.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCalendarView.scrollToCurrent();
                    }
                });
                mCalendarLayout = view.findViewById(R.id.calendarLayout);
                mCalendarView.setOnYearChangeListener(this);
                mCalendarView.setOnCalendarSelectListener(this);
                mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
                mYear = mCalendarView.getCurYear();
                mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
                mTextLunar.setText("今日");
                mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
                mRecyclerView = view.findViewById(R.id.recyclerView);
                initScrollDisabledListView();

                return view;
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
                UserDBHelper mHelper=UserDBHelper.getInstance(mContext,1);
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
                TextView tvInfo = (TextView) view.findViewById(R.id.textView);
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

    private void initScrollDisabledListView() {
        AgendaTitleArrayList = new ArrayList<>();
        CourseArrayList=new ArrayList<>();
        adapterArrayList=new ArrayList<CustomListAdapter>();
        for (int i = 0; i < 7; i++) {
            ArrayList<String> arrayList = new ArrayList<String>();
            ArrayList<String> courseList = new ArrayList<String>();
            for (int j = 0; j < 24; j++) {
                arrayList.add("");
                courseList.add("");
            }
            AgendaTitleArrayList.add(arrayList);
            CourseArrayList.add(courseList);



            CustomListAdapter customListAdapter = new CustomListAdapter(this, this, arrayList, courseList, i);
            adapterArrayList.add(customListAdapter);

        }


        listView1.setAdapter(adapterArrayList.get(0));
        listView1.setTag(0);

        listView2.setAdapter(adapterArrayList.get(1));
        listView2.setTag(1);

        listView3.setAdapter(adapterArrayList.get(2));
        listView3.setTag(2);

        listView4.setAdapter(adapterArrayList.get(3));
        listView4.setTag(3);

        listView5.setAdapter(adapterArrayList.get(4));
        listView5.setTag(4);

        listView6.setAdapter(adapterArrayList.get(5));
        listView6.setTag(5);

        listView7.setAdapter(adapterArrayList.get(6));
        listView7.setTag(6);

        Calendar calendar = mCalendarView.getSelectedCalendar();
        Calendar weekStartCalendar = CalendarUtil.getStartInWeek(calendar, 1);
        updateAgenda(weekStartCalendar);
    }

    /*private Map<String,List<String>> CustomerAdapter_getDataMap(){
        Map<String,List<String>> map=new HashMap<>();
        for(int y=2018;y<2025;y++){
            for(int m=1;m<=12;m++){
                for(int d=1;d<=31;d++) {
                    List<String> list = new ArrayList<String>();

                    String s="哈皮";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    s="制作PPT";
                    list.add(s);

                    //map的键值
                    String key=y + "" + (m < 10 ? "0" + m : m) + "" + (d < 10 ? "0" + d : d);

                    map.put(key,list);
                }
            }
        }
        return map;
    }*/


    public void RedrawExpandingList() {
        //TODO
        expandingList.Clear_mContainer();
        TITLES = new String[100];

        INFOS = new String[100];

        STARTTIMES = new String[100];

//        ENDTIMES=new String[100];

        ADDRESSES = new String[100];
        Cursor c;
//        mHelper.reset();
        mHelper = UserDBHelper.getInstance(getContext(), 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        int length = c.getCount();
        c.moveToFirst();
        int iconRes = R.drawable.duck;
        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1).toString();
            INFOS[i] = c.getString(2).toString();
            STARTTIMES[i] = c.getString(3).toString();
//            ENDTIMES[i]=c.getString(4).toString();
            ADDRESSES[i] = c.getString(5).toString();
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
        } else if (view.getId() == R.id.btn_mission) {
            int tag = (int) view.getTag();
            int listIndex = tag / 100;
            int buttonIndex = tag % 100;
            final int position = (int) view.getTag();
            String s=new Integer(position).toString();
            adapterArrayList.get(2).notifyDataSetChanged();
            Log.v("buttonIndex",s);
            customDialog(listIndex, buttonIndex);
        } else if (view.getId() == R.id.ll_setup_accountnsafety) {
            Toast.makeText(mContext, "你点击了账号与安全", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, AccountnSafetySetupActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_notenfeedback) {
            Toast.makeText(mContext, "你点击了反馈", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, HelpnfeedbackSetupActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ll_setup_about) {
            Toast.makeText(mContext, "你点击了关于", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, AboutSetupActivity.class);
            startActivity(intent);
        }

    }

    private void addItem(String title, String info, String starttime, String address, int colorRes, int iconRes) {
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

            case R.id.btn_mission: {
                int tag=(int)v.getTag();
                customChooseDialog(tag);
                return true;
                /*Intent intent = new Intent();
                intent.setClass(mContext, AddAgendaActivity.class);
                Bundle bundle = new Bundle();


                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Calendar weekStartCalendar = CalendarUtil.getStartInWeek(selectedCalendar, 1);
                Calendar clickedListCalendar = weekStartCalendar;

                int tag = (int) v.getTag();
                int ListIndex = tag / 100;
                int ButtonIndex = tag % 100;
                for (int i = 0; i < ListIndex; i++) {
                    clickedListCalendar = CalendarUtil.getNextCalendar(clickedListCalendar);
                }
                String date = clickedListCalendar.toStringWithoutYear();
                date = date + (ButtonIndex < 10 ? "0" + ButtonIndex : ButtonIndex) + "00";
                bundle.putString("date", date);

                // TODO: NOT NEXT DAY BUT NEXT HOUR
                String nextDate = CalendarUtil.getNextCalendar(clickedListCalendar).toStringWithoutYear();
                nextDate = nextDate + (ButtonIndex < 10 ? "0" + ButtonIndex : ButtonIndex) + "00";
                bundle.putString("nextDate", nextDate);


                intent.putExtras(bundle);
                startActivity(intent);
                return true;*/
            }
        }
        return false;
    }

    private void customChooseDialog(int tag){
        final Dialog dialog=new Dialog(mContext,R.style.NormalDialogStyle);
        View view=View.inflate(mContext,R.layout.dialog_choose,null);
        Button buttonAgenda=view.findViewById(R.id.btn_choose_agenda);
        Button buttonGoal=view.findViewById(R.id.btn_choose_goal);
        buttonAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, AddAgendaActivity.class);
                Bundle bundle = new Bundle();
               /* mHelper=UserDBHelper.getInstance(getContext(),1);
                mHelper.reset();*/

                Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                Calendar weekStartCalendar = CalendarUtil.getStartInWeek(selectedCalendar, 1);
                Calendar clickedListCalendar = weekStartCalendar;


                int ListIndex = tag / 100;
                int ButtonIndex = tag % 100;
                for (int i = 0; i < ListIndex; i++) {
                    clickedListCalendar = CalendarUtil.getNextCalendar(clickedListCalendar);
                }
                String date = clickedListCalendar.toStringWithoutYear();
                date = date + (ButtonIndex < 10 ? "0" + ButtonIndex : ButtonIndex) + "00";
                bundle.putString("date", date);

                // TODO: NOT NEXT DAY BUT NEXT HOUR
                String nextDate = CalendarUtil.getNextCalendar(clickedListCalendar).toStringWithoutYear();
                nextDate = nextDate + (ButtonIndex < 10 ? "0" + ButtonIndex : ButtonIndex) + "00";
                bundle.putString("nextDate", nextDate);


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(mContext,AddGoalActivity.class);
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
    private void customDialog(int listIndex, int buttonIndex) {
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        View view = View.inflate(mContext, R.layout.dialog_normal, null);


        TextView tv_title = view.findViewById(R.id.tv_agenda_dialog_title);
        TextView tv_time = view.findViewById(R.id.tv_agenda_dialog_start_time);
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

        mHelper = UserDBHelper.getInstance(getContext(), 1);
        String time = getListCilckedCalendar(listIndex).toStringWithoutYear();
        time = time + (buttonIndex < 10 ? "0" + buttonIndex : buttonIndex);
        Agenda agenda = mHelper.getAgendaWithTime(time);


        if (agenda != null) {
            tv_title.setText(agenda.title);
            tv_time.setText(agenda.start_time);
            editText_notation.setText(agenda.content);
        } else {
            tv_title.setText("无");
            tv_time.setText("00-00-00");
        }

        view.findViewById(R.id.btn_delete_agenda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agenda.index!=-1)
                {
                    int id=agenda.index;
                    mHelper.deleteAgendaWithIndex(id);

                    Calendar calendar=mCalendarView.getSelectedCalendar();
                    Calendar weekStartCalenddar=CalendarUtil.getStartInWeek(calendar,1);
                    updateAgenda(weekStartCalenddar);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
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
        Calendar weekStartCalendar = CalendarUtil.getStartInWeek(calendar, 1);
        updateAgenda(weekStartCalendar);
        updateCourse();
    }

    public void updateCourse(){

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                CourseArrayList.get(i).set(j, "");
            }
        }
        mHelper = UserDBHelper.getInstance(getContext(), 1);
        int year=mCalendarView.getSelectedCalendar().getYear();
        int month=mCalendarView.getSelectedCalendar().getMonth();
        int day=mCalendarView.getSelectedCalendar().getDay();
        if(year>=2019&&((month>2)||month==2&&day>=25)){
            for(int i=1;i<=7;i++){

                Calendar selectedCalendar=mCalendarView.getSelectedCalendar();
                String selectedDateString=selectedCalendar.toString();
                SimpleDateFormat simFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
                Date selectedDate=new Date();
                Date startWeekDate=new Date();
                try{
                    selectedDate= simFormat.parse(selectedDateString+" "+"000000");
                    startWeekDate=simFormat.parse("20190225 000000");
                }catch (ParseException e){
                    e.printStackTrace();
                }

                int days= com.android.xjay.joyplan.Utils.CalendarUtil.daysBetween(startWeekDate,selectedDate);
                int week=(days+1)/7+1;
                ArrayList<Course> courseArrayList=mHelper.getCourseWithDayOfWeek(week,i);
                int length=courseArrayList.size();
                for(int j=0;j<length;j++){
                    int index=courseArrayList.get(j).startIndex;
                    String courseName=courseArrayList.get(j).courseName;
                    int numOfCourse=courseArrayList.get(j).numOfCourse;
                    if(index<=4){
                        for(int k=0;k<numOfCourse;k++){
                            CourseArrayList.get(i).set(7+index+k,courseName);
                        }

                    }
                    else if(index>4&&index<=8){
                        for(int k=0;k<numOfCourse;k++){
                            CourseArrayList.get(i).set(9+index+k,courseName);
                        }

                    }
                    else if(index>8){
                        for(int k=0;k<numOfCourse;k++){
                            CourseArrayList.get(i).set(10+index+k,courseName);
                        }
                    }
                }
            }
        }

    }
    public void updateAgenda(Calendar calendar) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 24; j++) {
                AgendaTitleArrayList.get(i).set(j, "");
            }
        }
        for (int i = 0; i < 7; i++) {
            adapterArrayList.get(i).notifyDataSetChanged();
            String date = calendar.toStringWithoutYear();
            Log.v("test12", date);
            calendar = CalendarUtil.getNextCalendar(calendar);
            mHelper = UserDBHelper.getInstance(getContext(), 1);
            ArrayList<Agenda> AgendaList = mHelper.getAgendaListWithDate(date);
            int length = AgendaList.size();
            if (length == 0) {
                adapterArrayList.get(i).refreshAgenda();
                continue;
            } else {
                ArrayList<String> titleList = new ArrayList<String>();
                for (int j = 0; j < length; j++) {
                    String title = AgendaList.get(j).title;
                    String startTime = AgendaList.get(j).start_time;

                    int hour = Integer.parseInt(startTime.substring(11, 13));


                    AgendaTitleArrayList.get(i).set(hour, title);
                }

                adapterArrayList.get(i).refreshAgenda();

            }
        }
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

    class DynamicReceiverAddCourseTable extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            addCourseTable();
        }
    }

    class DynamicReceiverAddGoal extends BroadcastReceiver{
        public void onReceive(Context context,Intent intent){
             addGoal();
        }
    }

    private void addGoal(){

        String title = "Running";
        String address = "田径场";
        String content = "";
        String start_time="2019-05-22 20:00";
        String end_time="2019-05-22 20:00";
        Agenda agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-23 20:00";
        end_time="2019-05-23 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-24 20:00";
        end_time="2019-05-24 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-25 20:00";
        end_time="2019-05-25 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-26 20:00";
        end_time="2019-05-26 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-27 20:00";
        end_time="2019-05-27 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);

        title = "Running";
        address = "田径场";
        content = "";
        start_time="2019-05-28 20:00";
        end_time="2019-05-28 20:00";
        agenda = new Agenda(title, start_time, end_time, content, address);
        mHelper.insert_agenda(agenda);
    }
    private void addCourseTable(){

        mHelper.resetCourseTable();
        mHelper=UserDBHelper.getInstance(getContext(), 1);
        mHelper.insert_course(new Course("定向越野",1,1,5,5,2,"田径场","老师"));
       /* mHelper.insert_course(new Course("毛概",1,7,2));
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

        updateCourse();
        for(int i=0;i<7;i++){
            adapterArrayList.get(i).refreshCourse();
        }

    }

    private void configureSubItem(final CustomItem item, final View view, String info) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(info);

    }



    private void showInsertDialog(final ReserveActivity.OnItemCreated positive) {
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


