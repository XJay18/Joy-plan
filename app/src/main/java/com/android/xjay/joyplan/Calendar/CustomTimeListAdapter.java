package com.android.xjay.joyplan.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.xjay.joyplan.R;

import java.util.ArrayList;

/*
日程视图左边的时间条
 */
public class CustomTimeListAdapter extends BaseAdapter {
    ArrayList<String> list;

    /*
    构造函数，初始化列表
     */
    public CustomTimeListAdapter() {
        list = new ArrayList<String>();


        for (int i = 0; i <= 23; i++) {

            String s = new Integer(i).toString();
            list.add(s);
        }


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_list_example, parent, false);
            holder.tv_index = convertView.findViewById(R.id.tv_index);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_index.setText(list.get(position));

        return convertView;
    }


    /*
    时间的tv，0-23
     */
    class ViewHolder {
        TextView tv_index;
    }

}
