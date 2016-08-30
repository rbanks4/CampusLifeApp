package com.CampusLife.Campus_Life15;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
        ItemHolder holder = null;
        final ViewFlipper flipperTemp;
        if(convertView == null) {
            holder = new ItemHolder();
            convertView = inflater.inflate(R.layout.layout_calendar, null);

            holder.setupHolderViews(convertView);

            final ItemHolder finalHolder = holder;
            holder.flippy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.flippy.showNext();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
            holder.flippy.setDisplayedChild(0);
        }
        flipperTemp = (ViewFlipper)convertView.findViewById(R.id.flippy);
        //todo remember to remove flipper when we add our animator
        //holder.linearLayout.setBackgroundColor(getItem(position).getColor());
        holder.setupValuesAndColors(getItem(position));

        holder.flippy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flipperTemp.showNext();
            }
        });

        return convertView;
    }
}