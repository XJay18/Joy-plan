package com.android.xjay.joyplan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends AppCompatActivity {

    //TODO
    int[] IMAGES={R.drawable.cc,R.drawable.cc};



    String[] TITLES=new String[20];

    String[] INFOS=new String[20];

    private UserDBHelper mHelper;
    Cursor c;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //get cursor c to get record in DB
        mHelper=UserDBHelper.getInstance(this,1);
        SQLiteDatabase dbRead=mHelper.getReadableDatabase();
        c=dbRead.query("user_info",null,null
                ,null,null,null,null);

        //num of record in DB
        int length=c.getCount();

        c.moveToFirst();
        for(int i=0;i<length;i++){
            TITLES[i]=c.getString(1).toString();
            INFOS[i]=c.getString(2).toString();
            c.move(1);
        }

        //listView
        final ListView listView=(ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=listView.getItemAtPosition(position)+"";
                Toast toast=Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        //listView's adapter
        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);

        //btn to MainActivity
        Button changeButton2=findViewById(R.id.changeButton2);
        changeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ScheduleActivity.this,AddActivity.class);
                startActivity(intent);
                ScheduleActivity.this.finish();

            }
        });

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return c.getCount();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewGroup nullParent=null;
            if(view==null)
                view=getLayoutInflater().inflate(R.layout.list_example,nullParent);

            ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
            TextView textView_name=(TextView)view.findViewById(R.id.textView_name);
            TextView textView_description=(TextView)view.findViewById(R.id.textView_description);
            imageView.setImageResource(IMAGES[1]);
            textView_name.setText(TITLES[i]);
            textView_description.setText(INFOS[i]);

            return  view;
        }
    }
}
