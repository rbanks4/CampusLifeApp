package com.CampusLife.Campus_Life15;

import android.widget.ViewFlipper;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CLEvent {

    private String m_date;
    private String m_title;
    private String m_time;
    private String m_location;
    //private String prop4;
    private String m_description;
    //private String mdate;
    private ViewFlipper flip;
    public int color;

    DateFormat fday = new SimpleDateFormat("EEE MMM dd, yyyy");//this is how it should come out
    DateFormat yearday = new SimpleDateFormat("yyyyMMdd");
    DateFormat colon = new SimpleDateFormat ("hh:mmaa");
    DateFormat tday = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    Date date, endDate;



    public CLEvent(String sdate, String title, String edate, String location, String description) {
        this.m_date = sdate;//date
        try {
            if (m_date.length() < 9)
                date = yearday.parse(m_date);
            else
                date = dateFormat.parse(sdate);//now we should be able to sort
            this.m_date = fday.format(date);//update with the new date

            //now we set the time
            if(endDate != null) {
                if(edate.length() < 9) {
                    this.m_time = "All Day";
                }
                else {
                    endDate = dateFormat.parse(edate);
                    this.m_time = colon.format(date) + " - " + colon.format(edate);
                }
            }
            else
                this.m_time = colon.format(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.m_title = title;//title

        if (location != null) {
            this.m_location = location;//location
        }
        else{this.m_location = " ";}
        //this.prop4 = prop4;//sponsor
        if(description != null) {
            this.m_description = description;//description
        }
        else{
            this.m_description = "No description available";
        }
        checkColor();
    }


    private void checkColor(){
        if (m_title.contains("No Classes") || m_title.contains("no classes")
                || m_title.contains("No Class") || m_title.contains("no class")){//check the title name for "No Classes
            color = 0xff000000;//black for no classes
        }
        else if ((m_time == "All Day") || (m_location == "All Day")){
            color = 0xfff4821f;//orange for all day
        }
        else{
            color = 0xff0e4c8b;
        }
    }

    public String getDateString() { return m_date; }

    public Date getDate() { return date; }

    public String getTitle() {
        return m_title;
    }

    public String getTime() {
        return m_time;
    }

    public String getLocation() {
        return m_location;
    }

    //public String getProp4() { return prop4; }

    public String getDescription() { return m_description; }

    public ViewFlipper getFlip() { return flip; }

    public void setFlip(ViewFlipper f){this.flip = f; }

}