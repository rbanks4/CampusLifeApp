package com.CampusLife.Campus_Life15;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by rbank on 8/29/2016.
 */
public class ItemHolder {
    LinearLayout linearLayout;
    LinearLayout titlebox;
    TextView textView_week;
    TextView textView_month, textView_day;
    TextView titleView;
    TextView timesView;
    TextView locationView;
    TextView descriptionsView;
    View circle;
    ViewFlipper flippy;

    public void setupHolderViews(View view){
        linearLayout = (LinearLayout) view.findViewById(R.id.bar);
        titlebox = (LinearLayout) view.findViewById(R.id.titlebox);
        textView_week = (TextView) view.findViewById(R.id.week);
        textView_month = (TextView) view.findViewById(R.id.month);
        textView_day = (TextView) view.findViewById(R.id.day);

        titleView = (TextView) view.findViewById(R.id.textview1);
        timesView = (TextView) view.findViewById(R.id.textview2);
        locationView = (TextView) view.findViewById(R.id.textview3);
        descriptionsView = (TextView) view.findViewById(R.id.textview5);

        flippy = (ViewFlipper) view.findViewById(R.id.flippy);
        circle = (View) view.findViewById(R.id.circle);
    }

    public void setupValuesAndColors(CLEvent event){
        int color = event.getColor();

        //first we setup values
        textView_week.setText(event.getPropDateWeek());
        textView_month.setText(event.getPropDateMonth());
        textView_day.setText(event.getPropDateDay());

        titleView.setText(event.getTitle());
        timesView.setText(event.getTime());
        locationView.setText(event.getLocation());
        descriptionsView.setText(event.getDescription());

        titlebox.setBackgroundColor(color);
        timesView.setTextColor(color);
        locationView.setTextColor(color);
        descriptionsView.setTextColor(color);
        textView_week.setTextColor(color);

        Drawable background = circle.getBackground();
        GradientDrawable shapeDrawable = (GradientDrawable)background;
        shapeDrawable.setColor(color);


    }
}
