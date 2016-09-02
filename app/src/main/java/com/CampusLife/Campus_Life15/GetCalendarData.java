package com.CampusLife.Campus_Life15;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Reginald on 10/30/2015.
 */
class GetCalendarData extends AsyncTask<Void, Integer, String> {
    //the link to our XML data
    String glURL = "https://calendar.google.com/calendar/ical/i3r1v5ktao9tdr0l2klbb3srr4%40group.calendar.google.com/public/basic.ics";
    String resString;
    ClCalendar activity;
    int eventNum = 0;
    List<Event> events = new ArrayList<Event>();
    boolean wakeup = false; //tells us weather or not we should be reading an event
    boolean skipping = false;
    boolean m_desc_flag_on = false;
    private static final String DOWNLOAD_LOG = "Downloading";
    String filename = "calendar.ics";
    private static final String CAL_LIST = "CalendarList";
    private int m_currentFileSize = 0;

    public void setActivity(ClCalendar activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        activity.splash.gone();
        activity.build(events);
    }

    @Override
    protected String doInBackground(Void... nope) {//yep...don't pass in anything
        boolean dowloadFailed = false;
        try {
            //publishProgress(5);
            //show the status that it's loading
            Thread.sleep(500);

            URL google = new URL(glURL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) google.openConnection();

            //publishProgress(25);
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            eventNum = 0;

            urlConnection.setRequestMethod("HEAD");
            urlConnection.getInputStream();
            int size = urlConnection.getContentLength();
            //publishProgress(50);
            while ((line = reader.readLine()) != null) { // Read line by line
                sb.append(line + "\n");
                parseCalLine(line);
                m_currentFileSize += line.length();
                publishProgress(m_currentFileSize*100/size);
            }
            Log.i(CAL_LIST, "max: " + size + urlConnection.getHeaderField(1));
            Log.i(CAL_LIST, "current: " + m_currentFileSize);
            //publishProgress(75);

            //TODO this is really important if we are trying to pass the file as a string
            resString = sb.toString(); // Result is here

            is.close(); // Close the stream
            urlConnection.disconnect();
            //publishProgress(100);
            writeToFile(resString);
            return resString;

        } catch (InterruptedException e) {
            //this is assuming the file exist
            Log.e(DOWNLOAD_LOG, "Calendar download failed.", e);
        } catch (IOException e) {
            Log.e(DOWNLOAD_LOG, "I/O exception", e);
        } catch (Exception e) {
            Log.e(DOWNLOAD_LOG, "not able to write file", e);
        }

        if(isFileAvailable(filename)){
            eventNum = 0;
            setupListFromFile(filename);
        }

        return resString;
    }

    private boolean isFileAvailable(String filename){
        try{
            new File(Environment.getExternalStorageDirectory(), filename);
            return true;
        }
        catch(Exception e){
            Log.i(DOWNLOAD_LOG, "Calendar file not found", e);
            return false;
        }
    }

    private void writeToFile(String data) throws Exception {
        //write calendar file
        FileOutputStream outputStream;
        File ocalFile = new File(Environment.getExternalStorageDirectory(), filename);
        //outputStream = openFileOutput(calFile);
        outputStream = new FileOutputStream(ocalFile);
        outputStream.write(data.getBytes());
        outputStream.close();
    }

    private void setupListFromFile(String filename){
        try {
            File icalFile = new File(Environment.getExternalStorageDirectory(), filename);
            FileInputStream fin = new FileInputStream(icalFile);
            readFile(icalFile);
        } catch(IndexOutOfBoundsException e){
            Log.e(DOWNLOAD_LOG, "Index out of bounds while reading file...");
        }catch (Exception e){
            Log.e("ERROR", "could not find calendar file", e);
            e.printStackTrace();
        }
    }

    private String splitLine(String line, boolean second){
        String[] splitter = line.split(":",2);
        if(splitter.length > 1 && second){
            return splitter[1];
        }
        else if (!second)
            return splitter[0];
        else
            return "";
    }

    public void readFile(File file){

        Log.i(DOWNLOAD_LOG,"inside of readFile where filesize is: " + file.getTotalSpace());

        String[] splitter;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String temp_desc = null;
            Log.i(DOWNLOAD_LOG, "made it into the try data");
            while ((line = br.readLine()) != null)
                parseCalLine(line);
        }
        catch(Exception e){
            Log.i("ERROR", "could not find calendar file", e);
        }
    }

    public void parseCalLine(String line){
        {
            Log.i(DOWNLOAD_LOG, "trying to read line:" + line + " where count is:" + eventNum);
            //process the line
            if(line.equals("BEGIN:VEVENT"))
            {
                wakeup = true;
            }
            else if(line.equals("END:VEVENT")) {
                wakeup = false;
                if (!skipping)
                    eventNum++;
                else
                    skipping = false;
            }

            if (wakeup) {
                String tag = splitLine(line,false);
                String data = splitLine(line,true);
                switch (tag) {
                    case ("DTSTART;TZID=America/New_York"):
                        //we don't want to process this...it's too much to carry
                        wakeup = false;
                        skipping = true;
                        break;
                    case ("DTSTART"):
                    case ("DTSTART;VALUE=DATE"):
                        Event a = new Event();
                        a.setStartDate(data);
                        events.add(a);
                        break;
                    case ("DTEND"):
                    case ("DTEND;VALUE=DATE"):
                        events.get(eventNum).setEndDate(data);
                        break;
                    case ("DESCRIPTION"):
                        events.get(eventNum).setDescription(data);
                        m_desc_flag_on = true;
                        break;
                    case ("LOCATION"):
                        events.get(eventNum).setLocation(data);
                        break;
                    case ("SUMMARY"):
                        events.get(eventNum).setSummary(data);
                        break;
                    case ("LAST-MODIFIED"):
                        m_desc_flag_on = false;
                        break;
                    case ("DTSTAMP"):
                    case ("UID"):
                    case ("CREATED"):
                        break;
                    default:
                        if(m_desc_flag_on && events.get(eventNum).getDescription().length() > 0) {
                            String first = events.get(eventNum).getDescription();
                            events.get(eventNum).setDescription(first + tag.substring(1)); //for some reason it adds a space to be beginning
                        }
                        break;
                }
            }

        }
    }

    public static String[] dayCircle(String date){
        /**
         parse it by space
         an example string would be "Friday Sep 30, 2015"
         split by space: {"Friday", "Sep", "30,", "2015"}
         */
        String[] myDate = date.split(" ");
        myDate[2] = myDate[2].replace(",", "");
        /**
         final array: {"Friday", "Sep", "30", "2015"}
         we won't really use [3] unless we need it
         the function will only go through a semester's worth of info
         updates to the code can be made as the semester progresses
         */
        return myDate;
    }

    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }

    protected void setProgressPercent(int value){
        if(value > 10){
            activity.splash.progressMessage("Reading Data...");
        }
        activity.splash.updateProgress(value);
    }
}
