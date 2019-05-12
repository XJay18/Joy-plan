package com.android.xjay.joyplan.Calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.xjay.joyplan.R;

import java.util.List;


public class CustomListAdapter extends BaseAdapter {

    private final View.OnLongClickListener longClickListener;
    private final View.OnClickListener clicklistener;
    private int tag;
    List<String> agendaList;
    List<String> couresList;
    List<Integer> heightList;



    public CustomListAdapter(View.OnClickListener clicklistener,View.OnLongClickListener longClickListener,List<String> list,List<String> courseList,int tag){
        this.clicklistener=clicklistener;
        this.longClickListener=longClickListener;
        this.agendaList =list;
        this.couresList=courseList;
        this.tag=tag;

    }
    @Override
    public int getCount() {
        return agendaList.size();
    }

    public void refresh(){
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
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_example,parent,false);

            holder.btn_agenda =(Button)convertView.findViewById(R.id.btn_mission);
            holder.btn_course=(Button) convertView.findViewById(R.id.btn_course);
            holder.textView=(TextView)convertView.findViewById(R.id.tv_mission) ;

            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.btn_agenda.setOnClickListener(clicklistener);
        holder.btn_course.setOnClickListener(clicklistener);
        holder.btn_agenda.setOnLongClickListener(longClickListener);
        holder.btn_agenda.setTag(tag*10+position);
        holder.btn_course.setTag(tag*10+position);
        holder.btn_course.setText(couresList.get(position));
        holder.textView.setText(agendaList.get(position));
        if(agendaList.get(position)==""){
            holder.btn_agenda.setBackgroundResource(R.drawable.btn_shape_agenda_white);
        }
        else{
            holder.btn_agenda.setBackgroundResource(R.drawable.btn_shape_agenda_blue);
        }

        if(couresList.get(position)==""){
            holder.btn_course.setBackgroundResource(R.drawable.btn_shape_agenda_white);
        }
        else{
            holder.btn_course.setBackgroundResource(R.drawable.btn_shape_agenda_green);
        }

        return convertView;
    }
    class ViewHolder{
        Button btn_agenda;
        Button btn_course;
        TextView textView;
    }


}
