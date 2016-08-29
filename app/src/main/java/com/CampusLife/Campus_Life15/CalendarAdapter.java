package com.CampusLife.Campus_Life15;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

//Reg: this class is created in order to customize our adapter for data design purposes
public class CalendarAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CLEvent> objects;
    private String status;
    public TextView tview;

    private class ViewHolder {
        LinearLayout linearLayout;
        LinearLayout titlebox;
        TextView textVieww;
        TextView textViewm, textViewd;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        //TextView textView4;
        TextView textView5;
        View circle;
        ViewFlipper flippy;
    }

    public CalendarAdapter(Context context, ArrayList<CLEvent> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public CalendarAdapter() {
        inflater = null;
        this.objects = null;
    }

    public void setObjects(ArrayList<CLEvent> objects) {

        this.objects = objects;
    }

    public void setContext(Context context) {

        inflater = LayoutInflater.from(context);
        //tview = (TextView)
    }

    public void Loading(){
        //todo add animation if this works
        //this doesn't work in doInBackground b/c while inside, it can't post
        status = "Loading...";
        tview.setText(status);
    }
    public void Done(){
        //todo if loading works, this will work
        tview.setVisibility(View.GONE);
    }

    public int getCount() {
        return objects.size();
    }

    public CLEvent getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ViewFlipper flipperTemp;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_calendar, null);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.bar);
            holder.titlebox = (LinearLayout) convertView.findViewById(R.id.titlebox);
            holder.textVieww = (TextView) convertView.findViewById(R.id.week);
            holder.textViewm = (TextView) convertView.findViewById(R.id.month);
            holder.textViewd = (TextView) convertView.findViewById(R.id.day);
            holder.textView1 = (TextView) convertView.findViewById(R.id.textview1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textview2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textview3);
            //holder.textView4 = (TextView) convertView.findViewById(R.id.textview4);//Reg: the holder will let you see what's inside the layout
            holder.textView5 = (TextView) convertView.findViewById(R.id.textview5);
            holder.flippy = (ViewFlipper) convertView.findViewById(R.id.flippy);
            //todo remove at some point
            /*holder.circle = (View) convertView.findViewById(R.id.circle);
            //setting width equal to height to make a perfect circle
            int height = holder.circle.getLayoutParams().width;
            ViewGroup.LayoutParams cirlay = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    height);
            holder.circle.setLayoutParams(cirlay);*/
            final ViewHolder finalHolder = holder;
            holder.flippy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.flippy.showNext();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.flippy.setDisplayedChild(0);
        }
        flipperTemp = (ViewFlipper)convertView.findViewById(R.id.flippy);
        //todo remember to remove flipper when we add our animator
        //holder.linearLayout.setBackgroundColor(getItem(position).color);
        holder.textVieww.setText(objects.get(position).getPropDateWeek());
        holder.textViewm.setText(objects.get(position).getPropDateMonth());
        holder.textViewd.setText(objects.get(position).getPropDateDay());

        holder.textView1.setText(objects.get(position).getTitle());
        holder.textView2.setText(objects.get(position).getTime());
        holder.textView3.setText(objects.get(position).getLocation());
        //holder.textView4.setText(objects.get(position).getProp4());
        holder.textView5.setText(objects.get(position).getDescription());

        holder.titlebox.setBackgroundColor(getItem(position).color);
        holder.textView2.setTextColor(getItem(position).color);
        holder.textView3.setTextColor(getItem(position).color);
        holder.textView5.setTextColor(getItem(position).color);
        //objects.get(position).setFlip(holder.flippy);

        holder.flippy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flipperTemp.showNext();
            }
        });

        return convertView;
    }
}