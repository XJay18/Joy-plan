package com.android.xjay.joyplan;

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
import android.support.v7.app.AlertDialog;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.Calendar.Calendar;
import com.android.xjay.joyplan.Calendar.CalendarLayout;
import com.android.xjay.joyplan.Calendar.CalendarUtil;
import com.android.xjay.joyplan.Calendar.CalendarView;
import com.android.xjay.joyplan.Calendar.CustomListAdapter;
import com.android.xjay.joyplan.Calendar.ScrollDisabledListView;
import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener,CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,View.OnLongClickListener {

    protected Context mContext;
    private ExpandingList expandingList;
    DynamicReceiver dynamicReceiver;
    String[] TITLES;
    String[] INFOS;
    String[] TIMES;

    //used by fragment_agenda
    TextView mTextMonthDay;
    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;

    LinearLayout mRecyclerView;

    ScrollDisabledListView listView1;
    ScrollDisabledListView listView2;
    ScrollDisabledListView listView3;
    ScrollDisabledListView listView4;
    ScrollDisabledListView listView5;
    ScrollDisabledListView listView6;
    ScrollDisabledListView listView7;

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
        mContext=getActivity();
        // get the name of this fragment (Agenda or Planning or Discovery or Setup)
        String info=getArguments().getString("info");
        switch (info){
            case "日程": {
               View view = inflater.inflate(R.layout.fragment_agenda, null);

                //initView()
                listView1= view.findViewById(R.id.listView1);
                listView2=  view.findViewById(R.id.listView2);
                listView3=  view.findViewById(R.id.listView3);
                listView4=  view.findViewById(R.id.listView4);
                listView5=  view.findViewById(R.id.listView5);
                listView6=  view.findViewById(R.id.listView6);
                listView7=  view.findViewById(R.id.listView7);

                mTextMonthDay = view.findViewById(R.id.tv_month_day);
                mTextYear =  view.findViewById(R.id.tv_year);
                mTextLunar =  view.findViewById(R.id.tv_lunar);
                mRelativeTool =  view.findViewById(R.id.rl_tool);
                mCalendarView =  view.findViewById(R.id.calendarView);
                mTextCurrentDay =  view.findViewById(R.id.tv_current_day);
                mTextMonthDay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                mCalendarLayout =  view.findViewById(R.id.calendarLayout);
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
            case "发现":
            {

                View view = inflater.inflate(R.layout.fragment_discovery, null);





                view.findViewById(R.id.ll_fqz).setOnClickListener(this);
                view.findViewById(R.id.ll_ydhd).setOnClickListener(this);
                view.findViewById(R.id.ll_rcq).setOnClickListener(this);
                view.findViewById(R.id.ll_sxj).setOnClickListener(this);
                return view;

            }
            case "活动":
            {
                View view=inflater.inflate(R.layout.fragment_reserve,null);
                IntentFilter intentFilter=new IntentFilter();
                intentFilter.addAction("ADD ACTIVITY");
                dynamicReceiver=new DynamicReceiver();
                mContext.registerReceiver(dynamicReceiver,intentFilter);
                expandingList=view.findViewById(R.id.reserve_expanding_list);
                RedrawExpandingList();

                Button btn_changeTo_addActivity = view.findViewById(R.id.changeButton_reserve);
                btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                return view;
            }
            case"设置":
            {
                View view=inflater.inflate(R.layout.fragment_setup,null);
                return view;
            }
            default:{
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


    private void initScrollDisabledListView(){
        Map<String,List<String>> map=CustomerAdapter_getDataMap();
        ArrayList<CustomListAdapter> adapterArrayList=new ArrayList<>();

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int day=mCalendarView.getCurDay();
        Calendar calendar=new Calendar();
        String key;

        for(int i=0;i<7;i++){
            if(i==0){
                calendar.setYear(year);
                calendar.setMonth(month);
                calendar.setDay(day);
                key=calendar.toString();
            }
            else{

                calendar= CalendarUtil.getNextCalendar(calendar);
                key=calendar.toString();
            }
            List<String> list=map.get(key);
            CustomListAdapter customListAdapter=new CustomListAdapter(this,this,list);
            adapterArrayList.add(customListAdapter);

        }


        listView1.setAdapter(adapterArrayList.get(0));

        listView2.setAdapter(adapterArrayList.get(1));

        listView3.setAdapter(adapterArrayList.get(2));

        listView4.setAdapter(adapterArrayList.get(3));

        listView5.setAdapter(adapterArrayList.get(4));

        listView6.setAdapter(adapterArrayList.get(5));

        listView7.setAdapter(adapterArrayList.get(6));
    }

    private Map<String,List<String>> CustomerAdapter_getDataMap(){
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
    }
    public void RedrawExpandingList(){
        //TODO
        expandingList.Clear_mContainer();
        TITLES= new String[20];

        INFOS = new String[20];

        TIMES=new String[20];
        UserDBHelper mHelper;
        Cursor c;

        mHelper = UserDBHelper.getInstance(getContext(), 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        int length = c.getCount();
        c.moveToFirst();
        int iconRes=R.drawable.duck;
        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1).toString();
            INFOS[i] = c.getString(2).toString();
            TIMES[i]=c.getString(3).toString();
            String[] s=new String[]{INFOS[i],INFOS[i],INFOS[i],INFOS[i]};
            addItem(TITLES[i],s,TIMES[i],R.color.transparent,iconRes);
            c.move(1);
        }
    }
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.ll_fqz){
            Toast.makeText(mContext,"你点击了番茄钟",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(mContext,FqzActivity.class);
            startActivity(intent);
        }else if(view.getId()==R.id.ll_ydhd) {
            Toast.makeText(mContext,"你点击了预定活动",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.setClass(this.getContext(),ScheduleActivity.class);
            startActivity(intent);


        }else if(view.getId()==R.id.ll_rcq){
            Toast.makeText(mContext,"你点击了日程圈",Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.ll_sxj){
            Toast.makeText(mContext,"你点击了随心记",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(mContext,SxjActivity.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.btn_mission){
            final int position=(int)view.getTag();
            Toast.makeText(mContext,"点击了button"+position,Toast.LENGTH_SHORT).show();
        }

    }

    private void addItem(String title, String[] subItems,String time,int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);
        String Date=time.substring(0,10);
        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the custom_item
            ((TextView) item.findViewById(R.id.title)).setText(title);
            ((TextView) item.findViewById(R.id.time)).setText(Date);
            //We can create items in batch.
            item.createSubItems(subItems.length);
            for (int i = 0; i < item.getSubItemsCount(); i++) {
                //Let's get the created custom_sub_item custom_item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                configureSubItem(item, view, subItems[i]);
            }
            item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context=mContext;
                    CharSequence text="添加成功";
                    int duration=Toast.LENGTH_SHORT;
                    Toast toast=Toast.makeText(context,text,duration);
                    toast.show();
                }
            });

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
        switch (v.getId()){
            case R.id.btn_mission:
                final int position=(int)v.getTag();
                Toast.makeText(mContext,"长按了button"+ position,Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
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
    }

    /**
     * method to accept Broadcast and refresh the ExpandingList
     */
    class DynamicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RedrawExpandingList();
        }
    }

    private void configureSubItem(final CustomItem item, final View view, String subTitle) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);
        view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.removeSubItem(view);
            }
        });
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


