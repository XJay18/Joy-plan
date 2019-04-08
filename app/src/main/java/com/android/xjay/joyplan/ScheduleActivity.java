package com.android.xjay.joyplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.xjay.joyplan.CustomExpanding.CustomItem;
import com.android.xjay.joyplan.CustomExpanding.ExpandingList;

public class ScheduleActivity extends AppCompatActivity {

    private ExpandingList expandingList;

    //TODO
    int[] IMAGES = {R.drawable.cc, R.drawable.cc};


    String[] TITLES = new String[20];

    String[] INFOS = new String[20];

    private UserDBHelper_schedule mHelper;
    Cursor c;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        //get cursor c to get record in DB
        mHelper = UserDBHelper_schedule.getInstance(this, 1);
        SQLiteDatabase dbRead = mHelper.getReadableDatabase();
        c = dbRead.query("user_info", null, null
                , null, null, null, null);

        //num of record in DB
        int length = c.getCount();

        c.moveToFirst();


        expandingList=findViewById(R.id.schedule_expanding_list);
        int iconRes=R.drawable.cat;
        for (int i = 0; i < length; i++) {
            TITLES[i] = c.getString(1).toString();
            INFOS[i] = c.getString(2).toString();
            String[] s=new String[]{INFOS[i],INFOS[i],INFOS[i],INFOS[i]};
            addItem(TITLES[i],s,R.color.colorWhite,iconRes);
            c.move(1);
        }







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

                   /* showInsertDialog(new ReserveActivity.OnItemCreated() {
                        @Override
                        public void itemCreated(String title) {
                            View newSubItem = item.createSubItem();
                            configureSubItem(item, newSubItem, title);
                        }
                    });*/
                   Context context=getApplicationContext();
                   CharSequence text="添加成功";
                   int duration=Toast.LENGTH_SHORT;
                   Toast toast=Toast.makeText(context,text,duration);
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
        view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.removeSubItem(view);
            }
        });
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

    interface OnItemCreated {
        void itemCreated(String title);
    }

}
