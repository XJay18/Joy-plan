package com.android.xjay.joyplan.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.android.xjay.joyplan.R;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {

    private final View.OnLongClickListener longClickListener;
    private final View.OnClickListener clicklistener;
    private int tag;
    List<String> agendaList;
    List<String> couresList;


    public CustomListAdapter(View.OnClickListener clicklistener, View.OnLongClickListener longClickListener, List<String> list, List<String> courseList, int tag) {
        this.clicklistener = clicklistener;
        this.longClickListener = longClickListener;
        this.agendaList = list;
        this.couresList = courseList;
        this.tag = tag;


    }

    @Override
    public int getCount() {
        return agendaList.size();
    }


    public void refreshAgenda() {
        notifyDataSetChanged();
    }

    public void refreshCourse() {

        notifyDataSetChanged();
    }


    @Override
    public Object getItem(int position) {
        return agendaList.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_example, parent, false);

            holder.btn_agenda = convertView.findViewById(R.id.btn_mission);
            holder.btn_course = convertView.findViewById(R.id.btn_course);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btn_agenda.setOnClickListener(clicklistener);
        holder.btn_course.setOnClickListener(clicklistener);
        holder.btn_agenda.setOnLongClickListener(longClickListener);
        holder.btn_agenda.setTag(tag * 100 + position);
        holder.btn_course.setTag(tag * 100 + position);


        if (couresList.get(position) != "") {
            holder.btn_course.setText(couresList.get(position));
            holder.btn_course.setBackgroundResource(R.drawable.btn_shape_agenda_green);
        } else {
            holder.btn_course.setText("");
            holder.btn_course.setBackgroundResource(R.color.transparent);
        }

        String s = agendaList.get(position);
        if (agendaList.get(position) != "") {
            holder.btn_agenda.setText(agendaList.get(position));
            holder.btn_agenda.setBackgroundResource(R.drawable.btn_shape_agenda_blue);
            if (couresList.get(position) != "") {
                ViewGroup.LayoutParams layoutParams = holder.btn_agenda.getLayoutParams();
                ViewGroup.LayoutParams layoutParams1 = holder.btn_course.getLayoutParams();

                layoutParams.height = (int) (0.7 * (layoutParams1.height));
                holder.btn_agenda.setLayoutParams(layoutParams);
            }
        } else {
            holder.btn_agenda.setText("");
            holder.btn_agenda.setBackgroundResource(R.color.transparent);
        }
        return convertView;
    }

    class ViewHolder {
        Button btn_agenda;
        Button btn_course;
    }

}