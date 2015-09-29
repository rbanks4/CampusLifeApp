package com.CampusLife.Campus_Life15;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

//Reg: this class is created in order to customize our adapter for data design purposes
public class CalendarAdapter extends BaseAdapter {
//todo find out why more than one flipper ans flipping at a time
    private LayoutInflater inflater;
    private ArrayList<CLEvent> objects;

    private class ViewHolder {
        LinearLayout linearLayout;
        TextView textView0;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        //TextView textView4;
        TextView textView5;
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
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_calendar, null);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.bar);
            holder.textView0 = (TextView) convertView.findViewById(R.id.date);
            holder.textView1 = (TextView) convertView.findViewById(R.id.textview1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.textview2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textview3);
            //holder.textView4 = (TextView) convertView.findViewById(R.id.textview4);//Reg: the holder will let you see what's inside the layout
            holder.textView5 = (TextView) convertView.findViewById(R.id.textview5);
            holder.flippy = (ViewFlipper) convertView.findViewById(R.id.flippy);
            holder.flippy.setId(objects.get(position).getID());//change it to a number so there is no confusion
            final ViewFlipper finalHolder = holder.flippy;
            //todo make an onTouchListener to replace on click in order to solve problem...not finished
            /*holder.flippy.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d("DOWN", "DOWN");
                            break;

                        case MotionEvent.ACTION_MOVE:
                            Log.d("MOVE", "MOVE");
                            break;

                        case MotionEvent.ACTION_UP:
                            Log.d("UP", "UP");
                    }

                    return true;//return true so this action will repeat
                }
            });*/
            holder.flippy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.showNext();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.linearLayout.setBackgroundColor(getItem(position).color);
        holder.textView0.setText(objects.get(position).getProp0());
        holder.textView1.setText(objects.get(position).getProp1());
        holder.textView2.setText(objects.get(position).getProp2());
        holder.textView3.setText(objects.get(position).getProp3());
        //holder.textView4.setText(objects.get(position).getProp4());
        holder.textView5.setText(objects.get(position).getProp5());
        //objects.get(position).setFlip(holder.flippy);
        //holder.flippy.setid(objects.get(position).getFlip());
        //objects.get(position).setFlip(holder.flippy);
        return convertView;
    }
}