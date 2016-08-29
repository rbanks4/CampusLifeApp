package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;


public class ClCalendar extends Activity {

    /*
    We use Date and DateFormat in order to show the user the current date.
    Cal limit is used to limit list of events we show.
     */
    private Date                            date = new Date();
    private ImageView                       logo;
    //todo add to splashscreen class
    private LinearLayout                    loadingLayout;
    public SplashScreen             splash;

    public String                   resString = " ";
    private String                  caltest;
    //todo remove this once class is moved
    private String                  glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    private XMLParser               cutty = new XMLParser();
    Document                        caldoc;
    public ProgressDialog           progress;
    private ArrayList<CLEvent>      elist = new ArrayList<CLEvent>();
    private CalParser               cparse = new CalParser();
    private CalendarAdapter         adapter;
    //private final int progr[]  = {30, 15, 20, 25, 20};
    private int index;
    private ListView                listview;
    private String                  status;
    private static final String     CALENDAR_URL = "https://calendar.google.com/calendar/ical/i3r1v5ktao9tdr0l2klbb3srr4%40group.calendar.google.com/private-76a07fe1d6c488301fbb0dc680d89e9e/basic.ics";
    private static final String     CALENDAR_FILE_NAME = "m_cal";
    private static final String     CAL_LOG = "Calendar Logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal2);
        //DownloadFromUrl(); //gets our calendar file and saves it

        //todo see if this works --------------------------------------it works!
        //todo remove action bar if we aren't using it
        /*
        We make an action Bar to make sure we can go back to main,
        setup the UI, and initialize default values
         */
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout activityLayout = new LinearLayout(this);
        RelativeLayout mainLayout = new RelativeLayout(this);

        splash = new SplashScreen();
        splash.setContext(this);

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
        activityLayout.setPadding(16, 16, 16, 16);//padding should be equal

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

        //todo find a way to change the color of the circle to indicate months
        /*View circle = (View) findViewById(R.id.circle);
        ShapeDrawable btm = new ShapeDrawable();
        btm = (ShapeDrawable) circle.getBackground();
        //btm.set*/
        //todo give the user the ability to subscribe an event to their calendar
        //this listView layout is a placeholder, data will be put in it from code below
        listview = new ListView(this);
        listview.setLayoutParams(flp);
        listview.setVerticalScrollBarEnabled(true);
        //listview.setPadding(0, 0, 0, 60);

        //this is the loading splash screen
        splash.setImage(R.drawable.campuslife24);
        progress = new ProgressDialog(this);
        progress.setMax(100);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setMessage("Loading...");
        splash.setProgress(progress);

        //loadingLayout.addView(progress);
        mainLayout.addView(splash.getSplash());

        //run our background class
        GetCalendarData xmlThread = new GetCalendarData();
        xmlThread.setActivity(this);
        xmlThread.execute();

        activityLayout.addView(listview);
        mainLayout.addView(activityLayout);

        //todo put the second content view somwhere after the background computation is done
        setContentView(mainLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }
    /*
    this will show as the data for the calendar is loading
    */


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
        overridePendingTransition(R.anim.backacceldecelexit,R.anim.backacceldecel); //Reg: it works! Remember to apply the reverse

        return super.onOptionsItemSelected(item);
    }


    public void build(List<Event> events){

        int i = 0;
        try {
            for (Event e : events) {
                if (e.getSummary() == null)
                    Log.e(CAL_LOG, "file is null at" + i);
                else if (e.getSummary().length() > 1) {
                    CLEvent event = new CLEvent(e.getStartDate(), e.getSummary(), e.getEndDate(), e.getLocation(), e.getDescription());
                    Date currentDate = new Date();

                    if (event.getDate() != null) {
                        if (event.getDate().after(currentDate))
                            elist.add(event);
                    } else {
                        //make note of this
                        Log.i(CAL_LOG, "Hey!!! This event didn't have a date at index: " + i + " whole line: " + event.toString());
                    }
                }
                ++i;
            }
        }
        catch(Exception e) {
            Log.e(CAL_LOG, "Cannot add events to calendar: " + i, e);
        }

        //sort the events in order
        Collections.sort(elist, new DateComparator());
        //delete old events
        elist = removeOldDates(elist);

        //adds event list to adapter
        adapter.setObjects(elist);
        //final StableArrayAdapter sAdapter = new StableArrayAdapter(this,R.layout.layout_calendar,elist);
        //makes a list view out of the adapter
        //todo: make animations for appearance of list view
        listview.setAdapter(adapter);

    }

    public ArrayList<CLEvent> removeOldDates(ArrayList<CLEvent> object){
        ArrayList<CLEvent> myList = new ArrayList<>();
        Iterator<CLEvent> event = object.iterator();
         while(event.hasNext()) {
             CLEvent user = event.next();
            if (user.getDate().before(date)) {//if event is past current date
                event.remove(); //remove the event
            }
             else{
                myList.add(user); //add to ArrayList
            }

        }
        return myList;
    }
}
