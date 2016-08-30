package com.CampusLife.Campus_Life15;

import android.util.Log;
import android.widget.ViewFlipper;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CLEvent {

    private String m_date;
    private String m_title;
    private String m_time;
    private String m_location;
    private String m_description;
    private String[] propdate;
    private ViewFlipper flip;
    private int color;
    boolean allday, allnight = false;

    private DateFormat fday = new SimpleDateFormat("EEE MMM dd, yyyy");//this is how it should come out
    private DateFormat yearday = new SimpleDateFormat("yyyyMMdd");
    private DateFormat colon = new SimpleDateFormat ("h:mm aa");
    private DateFormat tday = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    private SimpleDateFormat dateFormatNoZ = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
    private Date date;
    private Date endDate;

    public static final int BLACK = 0xff000000;
    public static final int ORANGE = 0xfff4821f;
    public static final int BLUE = 0xff0e4c8b;

    public static final long HOUR = 3600*1000;

    public CLEvent(String sdate, String title, String edate, String location, String description) {
        this.m_date = sdate;//date
        try {
            if (m_date.length() < 9) {
                // if it's just the year and day
                date = yearday.parse(m_date);
                date = new Date(date.getTime() - HOUR * 4);
                allday = true;
                if (edate.length() < 9) {
                    m_time = "All Day";
                    allnight = true;
                }
            }
            else {
                date = dateFormatter(m_date);
                date = new Date(date.getTime() - HOUR * 4);
            }
            this.m_date = fday.format(date);//update with the new date

            //now we set the time
            if(!allday && !allnight && (edate != null)) {
                endDate = dateFormatter(edate);
                endDate = new Date(endDate.getTime() - HOUR * 4);
                this.m_time = colon.format(date) + " - " + colon.format(endDate);
            }
            else if (allday && !allnight)
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
        //final array is something like: {"Friday", "Sep", "30", "2015"}
        this.propdate = GetCalendarData.dayCircle(this.m_date);
        checkColor();
    }

    private Date dateFormatter(String dateString) {
        try {
            //if the string contains 'Z', we format it normally
            if (dateString.indexOf('Z') >= 0)
                return dateFormat.parse(dateString);//now we should be able to sort
            else
                return dateFormatNoZ.parse(dateString);
        }
        catch (Exception e){
            Log.i("Parsing issue", "cannot parse date properly", e);
            return null;
        }
    }

    private void checkColor(){
        if (m_title.contains("No Classes") || m_title.contains("no classes")
                || m_title.contains("No Class") || m_title.contains("no class")){//check the title name for "No Classes
            color = BLACK;//black for no classes
        }
        else if ((m_time == "All Day") || (m_location == "All Day")){
            color = ORANGE;//orange for all day
        }
        else{
            color = BLUE;
        }
    }

    public int getColor(){
        return color;
    }

    public String getPropDateWeek() {return propdate[0];};
    public String getPropDateMonth() {return propdate[1];};
    public String getPropDateDay() {return propdate[2];};

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