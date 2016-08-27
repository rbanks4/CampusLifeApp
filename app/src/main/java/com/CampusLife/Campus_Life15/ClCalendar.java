package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import javax.net.ssl.HttpsURLConnection;


public class ClCalendar extends Activity {

    /*
    We use Date and DateFormat in order to show the user the current date.
    Cal limit is used to limit list of events we show.
     */
    Date date = new Date();
    DateFormat day = new SimpleDateFormat("EEEE MMMM dd, yyyy  hh:mm:ss a");
    //int calLimit = 30;
    public String resString = " ";
    String caltest;
    private WebView mWebView;
    //String glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    //String glURL = "https://calendar.google.com/calendar/ical/p3anutbj7%40gmail.com/public/basic.ics";
    String glURL = "https://calendar.google.com/calendar/ical/i3r1v5ktao9tdr0l2klbb3srr4%40group.calendar.google.com/public/basic.ics";
    private TextView tview;
    private ProgressBar progress;
    ArrayList<CLEvent> elist = new ArrayList<CLEvent>();
    CalParser cparse = new CalParser();
    CalendarAdapter adapter;
    //private final int progr[]  = {30, 15, 20, 25, 20};
    private int index;
    ListView listview;
    private static final String CAL_LOG = "Calendar";
    String filename = "calendar.ics";
    private static final String CAL_LIST = "CalendarList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //setContentView(R.layout.activity_calendar);
        setContentView(R.layout.activity_cal2);

        //mWebView = (WebView) findViewById(R.id.calxml);
        //converting website into my own src code
        tview = (TextView) findViewById(R.id.src);
        progress = (ProgressBar)findViewById(R.id.progress);

        // Enable Javascript
        //WebSettings webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        //String glURL = "https://www.google.com/calendar/embed?src=dlc7torch%40gmail.com&ctz=America/New_York ";
        //mWebView.loadUrl(glURL);
        new GetXML().execute();
        /*//build();

        *//*
        We make an action Bar to make sure we can go back to main,
        setup the UI, and initialize default values
         *//*
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Create a ScrollView to put your hero data in*/


        //todo see if this works --------------------------------------it works!


        //setContentView(R.layout.activity_calendar);
        /*
        We make an action Bar to make sure we can go back to main,
        setup the UI, and initialize default values
         */
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout activityLayout = new LinearLayout(this);
        RelativeLayout mainLayout = new RelativeLayout(this);

        adapter = new CalendarAdapter();
        adapter.setContext(this);

        RelativeLayout.LayoutParams ml = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        mainLayout.setLayoutParams(ml);
        mainLayout.setVerticalScrollBarEnabled(true);

        lp.weight = 1.0f;
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 80);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams flp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams bot = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        bot.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        //this listView layout is a placeholder, data will be put in it from code below
        listview = new ListView(this);
        listview.setLayoutParams(flp);
        listview.setVerticalScrollBarEnabled(true);
        //listview.setPadding(0, 0, 0, 60);
        LayoutInflater inflater = LayoutInflater.from(this);
        //int bID = getResources().getIdentifier("button1", "id", getPackageName());
        //View ViewButton = inflater.inflate(bID,null);
        //listview.addFooterView(ViewButton);
        activityLayout.addView(listview);

        mainLayout.addView(activityLayout);

        setContentView(mainLayout);

        //note we also want to check if the file already exist
        if(savedInstanceState != null) {
            elist.clear();
            elist = (ArrayList<CLEvent>) savedInstanceState.get(CAL_LIST);
            adapter.setObjects(elist);
            listview.setAdapter(null);
            listview.setAdapter(adapter);
        }

    }
    //we don't use this
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        onBackPressed();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        overridePendingTransition(R.anim.acceldecel, R.anim.acceldecelexit); //Reg: it works! Remember to apply the reverse

        return super.onOptionsItemSelected(item);
    }
// TODO delete Cal() if it has no real use
    public void Cal(){
        //Reg: we use Intent to call a new activity
        TextView textViewToChange = (TextView) findViewById(R.id.date);
        textViewToChange.setText(day.format(date));
    }

    class GetXML extends AsyncTask<String, Void, String> {

        private Exception exception;
        //String resString = "wait on it...";
        //TextView tview;


        public GetXML() {
            resString = "wait on it...";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            build(result);
        }

        @Override
        protected String doInBackground(String... url) {
            boolean dowloadFailed = false;

            try {
                Thread.sleep(4000);

                URL google = new URL(glURL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) google.openConnection();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) // Read line by line
                    sb.append(line + "\n");

                resString = sb.toString(); // Result is here

                is.close(); // Close the stream
                urlConnection.disconnect();

                if(resString.length() > 0)
                    writeToFile(resString);

            } catch (InterruptedException e) {
                //this is assuming the file exist
                Log.e(CAL_LOG, "Calendar download failed.", e);
            } catch (IOException e) {
                Log.e(CAL_LOG, "I/O exception", e);
            } catch (Exception e) {
                Log.e(CAL_LOG, "not able to write file",e);
            }
            if(isFileAvailable(filename)){
                setupListFromFile(filename);
            }

            return resString;
        }
    }
    private boolean isFileAvailable(String filename){
        try{
            new File(Environment.getExternalStorageDirectory(), filename);
            return true;
        }
        catch(Exception e){
            Log.i(CAL_LOG, "Calendar file not found", e);
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
            Log.e(CAL_LOG, "Index out of bounds while reading file...");
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

    List<Event> events = new ArrayList<Event>();
    boolean wakeup = false; //tells us weather or not we should be reading an event
    boolean skipping = false;
    boolean m_desc_flag_on = false;
    public void readFile(File file){

        Log.i(CAL_LOG,"inside of readFile where filesize is: " + file.getTotalSpace());
        int eventNum = 0;
        String[] splitter;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String temp_desc = null;
            Log.i(CAL_LOG, "made it into the try data");
            while ((line = br.readLine()) != null) {
                Log.i(CAL_LOG, "trying to read line:" + line + " where count is:" + eventNum);
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
                                events.get(eventNum).setDescription(first + tag);
                            }
                            break;
                    }
                }

            }
        }
        catch(Exception e){
            Log.i("ERROR", "could not find calendar file", e);
        }
    }

    public void build(String res){
        for (Event e : events){
            if (e.getSummary().length() > 1) {
                CLEvent event = new CLEvent(e.getStartDate(), e.getSummary(), e.getEndDate(), e.getLocation(), e.getDescription());
                Date currentDate = new Date();

                if(event.getDate() != null) {
                    if (event.getDate().after(currentDate))
                        elist.add(event);
                }
                else{
                    //make note of this
                    Log.i(CAL_LOG, "Hey!!! This event didn't have a date!!!: " + event.toString());
                }
            }
        }



        //resString=cutty.getValue(caldoc.getElement,"title");
        // TODO Auto-generated method stub
        //onPostExecute(resString);
        //progress.setVisibility(View.GONE);
        Collections.sort(elist, new DateComparator());
        adapter.setObjects(elist);
        listview.setAdapter(adapter);
        //tview.setText(caltest);
        //return resString;
    }


    //todo sort all of the objects we parsed by date
    public static <CLEvent> void sort(List<CLEvent> list, Comparator<? super CLEvent> c){
        //don't know how to use it
    }

    //todo if this works, delete the other sort function
    public class DateComparator implements Comparator<CLEvent> {
        public int compare(CLEvent o1, CLEvent o2) {
            if (o1.getDate() != null && o2.getDate() != null) {
                if (o1.getDate().before(o2.getDate())) {
                    return -1;
                } else if (o1.getDate().after(o2.getDate())) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CAL_LIST, elist);
    }
}
