package com.android.xjay.joyplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    protected Context mContext;
    private ExpandingList expandingList;
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
                //TODO
                int[] IMAGES = {R.drawable.cc, R.drawable.cc};

                String[] TITLES = new String[20];

                String[] INFOS = new String[20];
                UserDBHelper_schedule mHelper;

                Cursor c;

                mHelper = UserDBHelper_schedule.getInstance(getContext(), 1);
                SQLiteDatabase dbRead = mHelper.getReadableDatabase();
                c = dbRead.query("user_info", null, null
                        , null, null, null, null);

                int length = c.getCount();
                c.moveToFirst();

                expandingList = view.findViewById(R.id.reserve_expanding_list);
                expandingList.setItemPadding(0);
                int iconRes=R.drawable.duck;
                for (int i = 0; i < length; i++) {
                    TITLES[i] = c.getString(1).toString();
                    INFOS[i] = c.getString(2).toString();
                    String[] s=new String[]{INFOS[i],INFOS[i],INFOS[i],INFOS[i]};
                    addItem(TITLES[i],s,R.color.colorWhite,iconRes);
                    c.move(1);
                }

                Button btn_changeTo_addActivity = view.findViewById(R.id.changeButton_reserve);
                btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, AddActivity.class);
                        startActivity(intent);
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
    }

    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If custom_item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the custom_item
            ((TextView) item.findViewById(R.id.title)).setText(title);

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


