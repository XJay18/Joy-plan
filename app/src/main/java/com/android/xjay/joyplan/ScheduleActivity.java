package com.android.xjay.joyplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandingList expandingList;

    //TODO
    int[] IMAGES = {R.drawable.cc, R.drawable.cc};

    String[] TITLES = new String[20];

    String[] INFOS = new String[20];

    String[] STARTTIMES = new String[20];

    String[] ADDRESSES = new String[20];

    private UserDBHelper mHelper;
    Cursor c;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        findViewById(R.id.btn_schedule_back).setOnClickListener(this);
        findViewById(R.id.ll_schedule_help).setOnClickListener(this);

        //get cursor c to get record in DB
        mHelper = UserDBHelper.getInstance(this, 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        //num of record in DB
        int length = c.getCount();

        c.moveToFirst();

        expandingList = findViewById(R.id.schedule_expanding_list);
        RedrawExpandingList();

        //btn to MainActivity
        Button btn_changeTo_addActivity = findViewById(R.id.changeButton_schedule);
        btn_changeTo_addActivity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ScheduleActivity.this, AddActivity.class);
                startActivity(intent);
                ScheduleActivity.this.finish();

            }
        });


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

    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an custom_item with R.layout.expanding_layout
        final CustomItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If custom_item creation is successful, let's configure it
        if (item != null) {
            //item color
            item.setIndicatorColorRes(colorRes);

            //item picture resource
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

                   /* showInsertDialog(new ReserveActivity.OnItemCreated() {
                        @Override
                        public void itemCreated(String title) {
                            View newSubItem = item.createSubItem();
                            configureSubItem(item, newSubItem, title);
                        }
                    });*/
                    Context context = getApplicationContext();
                    CharSequence text = "添加成功";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

           /* item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandingList.removeItem(item);
                }
            });*/
        }
    }

    private void configureSubItem(final CustomItem item, final View view, String subTitle) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);

    }


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
        mHelper = UserDBHelper.getInstance(this, 1);
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

    private void showInsertDialog(final ReserveActivity.OnItemCreated positive) {
        final EditText text = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_schedule_back) {
            Intent intent = new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ll_schedule_help) {
            android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
            mBuilder.setTitle("发布活动");
            mBuilder.setMessage(R.string.info_fbhd);
            android.app.AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        }
    }

    interface OnItemCreated {
        void itemCreated(String title);
    }

}
