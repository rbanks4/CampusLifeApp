package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;


public class ClCalendar extends Activity {

    /*
    We use Date and DateFormat in order to show the user the current date.
    Cal limit is used to limit list of events we show.
     */
    Date date = new Date();
    ImageView logo;
    //todo add to splashscreen class
    LinearLayout loadingLayout;
    public SplashScreen splash;

    public String resString = " ";
    String caltest;
    //todo remove this once class is moved
    String glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    XMLParser cutty = new XMLParser();
    Document caldoc;
    public ProgressDialog progress;
    ArrayList<CLEvent> elist = new ArrayList<CLEvent>();
    CalParser cparse = new CalParser();
    CalendarAdapter adapter;
    //private final int progr[]  = {30, 15, 20, 25, 20};
    private int index;
    ListView listview;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal2);

        //todo see if this works --------------------------------------it works!
        //todo remove action bar if we aren't using it
        /*
        We make an action Bar to make sure we can go back to main,
        setup the UI, and initialize default values
         */
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

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
        GetXML xmlThread = new GetXML();
        xmlThread.setActivity(this);
        xmlThread.execute();

        activityLayout.addView(listview);
        mainLayout.addView(activityLayout);

        //todo put the second content view somwhere after the background computation is done
        setContentView(mainLayout);
    }


    private class StableArrayAdapter extends ArrayAdapter<CLEvent> {

        HashMap<CLEvent, Integer> mIdMap = new HashMap<>();
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<CLEvent> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            CLEvent item = getItem(position);
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


    public void build(String res){
        //create a document to find all of the elements
        caldoc = cutty.getDomElement(res);//makes the document
        caldoc.getDocumentElement().normalize();
        NodeList nList = caldoc.getElementsByTagName("entry");//makes a list of nodes
        caltest = " ";//now that we have our doc, we can clear it

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            caltest.concat("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {//logic returning somthing else
                Element eElement = (Element) nNode;
                //gets event title
                String Title = (eElement.getElementsByTagName("title").item(0).getTextContent());
                //fixes quoting issue
                if (Title.contains("&quot;")){Title.replaceAll("&quot;","");}
                //gets event summary
                String Summary = (eElement.getElementsByTagName("summary").item(0).getTextContent());
                //CalParserclass parses the rest of the data in this event neatly
                String[] changes = cparse.fixSummary(Summary);
                //events are spit out here in order (date, title, time, location, desc)
                CLEvent event = new CLEvent(changes[0], Title, changes[1], changes[2], changes[3]);
                //add the event to our event list
                elist.add(event);

            }

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
