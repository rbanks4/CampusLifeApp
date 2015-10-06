package com.CampusLife.Campus_Life15;

/**
 * Created by Reginald on 9/24/2015.
 */


import android.util.Log;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class CalParser {
    Date dtime;
    //DateFormat stime = new SimpleDateFormat(" haa");
    DateFormat stime = new SimpleDateFormat("haa");
    DateFormat stime12 = new SimpleDateFormat ("hhaa");
    DateFormat colon = new SimpleDateFormat ("hh:mmaa");
    DateFormat ltime = new SimpleDateFormat("hh:mm aa");

    //Constructor
    CalParser() {
        //this.mActivity = activity;
    }

    //this method will be used in CLEvent
    public String[] dayCircle(String date){
        //parse it by space
        //an example string would be "Friday Sep 30, 2015"
        //split by space: {"Friday", "Sep", "30,", "2015"}
        //note: fix array [2] via 3rd one without comma
        String[] myDate = date.split(" ");
        myDate[2] = myDate[2].replace(",","");
        //final array: {"Friday", "Sep", "30", "2015"}
        //we won't really use [3] unless we need it
        return myDate;
    }

    //todo clean code to remove redundancy in fixSummary
    public String[] fixSummary(String raw) {
        // List the next 10 events from the primary calendar. -Reg
        String file = raw;
        //List<String> dtd = new ArrayList<String>();
        file.replaceAll("When: ", ""); //now it will read
        //String date[] = file.split(", 2015");
        String data[] = new String[4];
        data[0] = GetDate(file);
        data[1] = GetTime(file);
        data[2] = GetLocation(file);
        data[3] = GetDesc(file);

        for (int i = 0; i<3; i++) {//clean up all the jibberish
            //clean up text
            if (data[i] != null) {
                remove("<br>", data[i]);
                remove("Event Status: confirmed", data[i]);
                remove("EDT", data[i]);
                remove("EST", data[i]);
                remove("&amp;", data[i]);
                remove("&nbsp;", data[i]);
                remove("When: ", data[i]);
                quotes(data[i]);
            }
            else{
                //do nothing
            }
        }


        //String delims = "When: ";
        //String[] tokens = employee.split(delims);
        return data;
    }

    public String GetDate(String sum){
        String date[] = sum.split("When: ");
        String fdate; //our final date
        if (date[1].contains("2015")) {
            String date2[] = date[1].split(", 2015");
            fdate = date2[0] + ", 2015";
            return fdate;
        }
        else if (date[1].contains ("2016")) {
            String date2[] = date[1].split(", 2016");
            fdate = date2[0] + ", 2016";
            return fdate;
        }
        else{
            String date2[] = date[1].split(", 20");
            return date2[0];
        }
    }
    public String GetTime(String sum){
        if (sum.contains("am")||sum.contains("pm")){
            String time[] = sum.split("2015");
            String time2[] = time[1].split("&");
            return fixTime(time2[0]);
        }
        else{
            return null;
        }
    }
    public String GetLocation(String sum){
        if (sum.contains("Where: ")){
            String loc[] = sum.split("Where: ");
            String loc2[] = loc[1].split("<");
            return rmlines(loc2[0]);
        }
        else{
            return null;
        }
    }
    public String GetDesc(String sum){
        if (sum.contains("Event Description: ")){
            String desc[] = sum.split("Event Description: ");
            return desc[1];
        }
        else{
            return null;
        }
    }

    public String fixTime(String time){
        time = time.replaceAll("(to)(.*?)(2015)","to");
        if (time.contains(",")){
            String edit;
            String ntime[] = time.split("to");
            if (ntime[0].contains(":")) { //do the same for span[1]
                ntime[0] = removespace2(ntime[0]);
                try {
                    dtime = (Date) colon.parse(ntime[0]);
                    ntime[0] = ltime.format(dtime);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                ntime[0] = noZero(ntime[0]);
                edit = ntime[0];
            }
            else{
                ntime[0] = removespace2(ntime[0]);
                try {
                    removespace2(ntime[0]);
                    dtime = (Date) stime.parse(ntime[0]);
                    String fix = stime12.format(dtime);
                    dtime = (Date) stime12.parse(fix);
                    ntime[0] = ltime.format(dtime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ntime[0] = noZero(ntime[0]);
                edit = ntime[0];
            }
            return edit;
        }
        else {//it's split into a 2 date format, the best way is to make a start-end
            String span[] = time.split(" to ");
            span[0] = removespace2(span[0]);
            span[1] = removespace2(span[1]);
            if (span[0].contains(":")) { //do the same for span[1]
                try {
                    dtime = (Date) colon.parse(span[0]);
                    span[0] = ltime.format(dtime);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                span[0] = noZero(span[0]);
            }
            else{
                try {
                    if (span[0].length() > 3) {
                        span[0] = removespace3(span[0]);
                        dtime = (Date) stime12.parse(span[0]);
                    }
                    else {
                        dtime = (Date) stime.parse(span[0]);
                        span[0] = ltime.format(dtime);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                span[0] = noZero(span[0]);
            }
            if (span[0].contains(":")) {
                try {
                    dtime = (Date) colon.parse(span[1]);
                    span[1] = ltime.format(dtime);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                span[1] = noZero(span[1]);
            }
            else{
                try {
                    span[1] = removespace2(span[1]);
                    span[1] = removespace3(span[1]);
                    span[1] = noZero(span[1]);
                    /*if (span[1].length() > 3) {
                        span[1] = removespace3(span[1]);
                        dtime = (Date) stime12.parse(span[1]);
                        span[1] = ltime.format(dtime);

                    }
                    else {
                        span[1] = removespace2(span[1]);
                        //dtime = (Date) stime.parse(span[1]);
                        //span[1] = ltime.format(dtime);
                    }*/
                    //dtime = (Date) stime.parse(span[1]);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            time = span[0] + " - " +span[1];
            return time;
        }
    }

    public void remove(String word, String caltest){
        while(caltest.contains(word)){
            caltest = caltest.replace(word, "");
        }
    }
    public void quotes(String caltest){
        while(caltest.contains("&quot;")){
            caltest = caltest.replace("&quot;", "");
        }
    }
    public String rmlines(String caltest){
        while(caltest.contains("(\\r|\\n|)")||caltest.contains("\n")){
            caltest = caltest.replace("(\\r|\\n|)", "");
            caltest = caltest.replaceAll("\n", "");
        }
        return caltest;
    }
    public void removespace(String caltest){
        Log.w("BeforeA", "before "+caltest);
        while(caltest.contains(" ")){
            caltest = caltest.replaceAll(" ", "");
            caltest.replaceAll("\\s+","");
            caltest.replaceAll("\\s{2,}",""); //for html contiguous spaces
            caltest.replaceAll("\\W","");
            caltest = caltest.replaceAll("(\\r|\\n)", "");
            caltest = caltest.replaceAll("\n","");
        }
        try {
            dtime = (Date) stime.parse(caltest);
            caltest = ltime.format(dtime);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.w("BeforeAF", "FAILURErm1: " + caltest);
        }
        Log.w("BeforeA", "after "+caltest);
    }
    public String removespace2(String caltest){
        Log.w("BeforeA", "before "+caltest);
        while(caltest.contains(" ")){
            caltest = caltest.replaceAll(" ", "");
            caltest.replaceAll("\\s+","");
            caltest.replaceAll("\\s{2,}",""); //for html contiguous spaces
            caltest.replaceAll("\\W","");
            caltest = caltest.replaceAll("(\\r|\\n)", "");
            caltest = caltest.replaceAll("\n","");
        }
        try {
            dtime = (Date) stime.parse(caltest);
            caltest = ltime.format(dtime);
            Log.w("BeforeA", "SUCCESS: "+caltest);
        }
        catch(Exception e){
            Log.w("BeforeAF", "FAILURErm2: "+caltest);
            e.printStackTrace();
        }
        return caltest;
    }
    public String removespace3(String caltest){
        Log.w("BeforeA", "before "+caltest);
        while(caltest.contains(" ")){
            caltest = caltest.replaceAll(" ", "");
            caltest.replaceAll("\\s+","");
            caltest.replaceAll("\\s{2,}",""); //for html contiguous spaces
            caltest.replaceAll("\\W","");
            caltest = caltest.replaceAll("(\\r|\\n)", "");
            caltest = caltest.replaceAll("\n","");
        }
        try {
            dtime = (Date) stime12.parse(caltest);
            caltest = ltime.format(dtime);
            Log.w("BeforeA", "SUCCESS: "+caltest);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.w("BeforeAF", "FAILURErm3: " + caltest);
        }
        return caltest;
    }
    public String noZero(String date){
        String d = date;
        if(date.charAt(0)=='0'){
            return d.substring(1);
        }
        return d;
    }
}