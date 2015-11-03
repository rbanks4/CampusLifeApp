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
 * An asynchronous task that handles the Google Calendar XML exports or links.
 * Uses XML in place of Google Calendar API in order to make calendar public.
 */
public class CalParser {
    Date dtime;
    DateFormat stime = new SimpleDateFormat("haa");
    DateFormat stime12 = new SimpleDateFormat ("hhaa");
    DateFormat colon = new SimpleDateFormat ("hh:mmaa");
    DateFormat ltime = new SimpleDateFormat("hh:mm aa");

    //Constructor
    CalParser() {
    }

    //this method will be used in CLEvent
    public String[] dayCircle(String date){
        /*
        parse it by space
        an example string would be "Friday Sep 30, 2015"
        split by space: {"Friday", "Sep", "30,", "2015"}
        */
        String[] myDate = date.split(" ");
        myDate[2] = myDate[2].replace(",","");
        /*
        final array: {"Friday", "Sep", "30", "2015"}
        we won't really use [3] unless we need it
        the CalParser will only go through a semester's worth of info
        updates to the code can be made as the semester progresses
        */
        return myDate;
    }

    public String[] fixSummary(String raw) {
        /*
        List the future events from the primary calendar. -Reg
        read in the raw file, replace words that don't belong (like "When: ")
        parse date, time, location, and description based on key words
        */
        String file = raw;
        file.replaceAll("When: ", "");
        String data[] = new String[4];
        data[0] = GetDate(file);
        data[1] = GetTime(file);
        data[2] = GetLocation(file);
        data[3] = GetDesc(file);

        for (int i = 0; i<3; i++) {
            //if data exist, clean data of XML tags, and edits
            if (data[i] != null) {
                remove(" ", data[i]);
                        remove("Event Status: confirmed", data[i]);
                remove("EDT", data[i]);
                remove("EST", data[i]);
                remove("&", data[i]);
                remove(" ", data[i]);
                remove("When: ", data[i]);
                quotes(data[i]);
            }
            else{
                //do nothing
            }
        }

        return data;
    }

    //todo turn into a switch
    public String GetDate(String sum){
        /*
        our token to find date is "When: "
        so we take the data after it
        */
        String date[] = sum.split("When: ");
        //our final date
        String fdate; 
        /*
        check for the year
        there is some weird xml code after ", 2015"
        we use split to cut out what we need
        oddly enough we add it back to keep format
        todo add 'year' to our CLEvent object
        */
        if (date[1].contains("2015")) {
            String date2[] = date[1].split(", 2015");
            fdate = date2[0] + ", 2015";
            return fdate;
        }
        else if (date[1].contains ("2016")) {
            //we don't need this yet
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
        /*
        checks summary for am or pm token
        this s how we find the time
        time is on the right side of our year
        */
        if (sum.contains("am")||sum.contains("pm")){
            String time[] = sum.split("2015");
            String time2[] = time[1].split("&");
            //fixTime() is super long remember to clean
            return fixTime(time2[0]);
        }
        else{
            return null;
        }
    }
    public String GetLocation(String sum){
        //looks for "Where: ", parses location
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
        //looks for "Event Description: " parses desc
        if (sum.contains("Event Description: ")){
            String desc[] = sum.split("Event Description: ");
            return desc[1];
        }
        else{
            return null;
        }
    }

    //todo clean fixTime()
    /*
    fixtime will give all of our time data a uniform look
    -no leading 0s 
    -if only a start time, just show start time
    -remove redunant xml clutter
    -add ":00" to times like "4 pm"
    */
    public String fixTime(String time){
        //removes all the meat in the string we don't need
        time = time.replaceAll("(to)(.*?)(2015)","to");
        /*
        in the xml file, some times are written as "h pm"
        and others are written as "h:mm pm" we could also
        use 'contains("30")' but to be safe (in case :15 shows)
        we will just look for a colon (:)
        */
        if (time.contains(",")){
            //this top if statement is only for if we have 1 time
            String edit;
            String ntime[] = time.split("to");
            if (ntime[0].contains(":")) {
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
                    //adds the colon
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
            if (span[0].contains(":")) {
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

                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            time = span[0] + " - " +span[1];
            return time;
        }
    }

    /*the code below is used to clean up the unneeded spaces, indents, and new lines*/

    public void remove(String word, String caltest){
        //removes whatever we ask it to remove from a selected string
        while(caltest.contains(word)){
            caltest = caltest.replace(word, "");
        }
    }
    public void quotes(String caltest){
        //removes quotes
        while(caltest.contains("quot")){
                caltest = caltest.replace("quot", "");
    }
}
    public String rmlines(String caltest){
        //removes new lines
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