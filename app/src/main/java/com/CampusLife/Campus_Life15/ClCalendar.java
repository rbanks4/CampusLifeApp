package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.net.ssl.HttpsURLConnection;


public class ClCalendar extends Activity {

    /*
    We use Date and DateFormat in order to show the user the current date.
    Cal limit is used to limit list of events we show.
     */
    Date date = new Date();

    DateFormat day = new SimpleDateFormat("EEEE MMMM dd, yyyy  hh:mm:ss a");
    ImageView logo;
    //int calLimit = 30;
    LinearLayout loadingLayout;
    public String resString = " ";
    String caltest;
    private WebView mWebView;
    String glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    private TextView tview;
    XMLParser cutty = new XMLParser();
    Document caldoc;
    private ProgressBar progress;
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

        //tview = (TextView) findViewById(R.id.src);//use this to display our progress

        //new GetXML().execute();

        //todo see if this works --------------------------------------it works!

        /*
        We make an action Bar to make sure we can go back to main,
        setup the UI, and initialize default values
         */
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout activityLayout = new LinearLayout(this);
        RelativeLayout mainLayout = new RelativeLayout(this);
        loadingLayout = new LinearLayout(this);

        adapter = new CalendarAdapter();
        adapter.setContext(this);

        RelativeLayout.LayoutParams ml = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        ll.gravity = Gravity.CENTER_HORIZONTAL;
        ll.gravity = Gravity.CENTER_VERTICAL;

        mainLayout.setLayoutParams(ml);
        mainLayout.setVerticalScrollBarEnabled(true);

        lp.weight = 1.0f;
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);//padding should be equal

        loadingLayout.setLayoutParams(ll);
        loadingLayout.setOrientation(LinearLayout.VERTICAL);
        loadingLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
        loadingLayout.setBackgroundColor(0xff0e4c8b); //Clayton Blue

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
        logo = new ImageView(this);
        logo.setImageResource(R.drawable.campuslife24);
        progress = new ProgressBar(this);
        //tview = new TextView(this);
        //tview.setText("Loading...");
        //tview.setPadding(8, 8, 8, 8);
        //tview.setTextColor(0xffffffff);//basic white
        //Animation loadingmovement = AnimationUtils.loadAnimation(this,R.anim.backacceldecel);
        //tview.setAnimation(loadingmovement);
        //tview.startAnimation(loadingmovement);
        loadingLayout.addView(logo);
        loadingLayout.addView(progress);
        //loadingLayout.addView(tview);
        mainLayout.addView(loadingLayout);
        //setContentView(loadingLayout);//loading screen for now
        new GetXML().execute();

        activityLayout.addView(listview);

        mainLayout.addView(activityLayout);

        //mainLayout.setBackgroundColor(0xFF59B3DB);//that sky blue
        //activityLayout.setBackgroundColor(0xFF59B3DB);//that sky blue
        //todo put the second content view somwhere after the background computation is done
        setContentView(mainLayout);
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
            //tview.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            //setContentView(R.layout.activity_calendar);
            //tview.setText(result);
            //progress.setVisibility(View.GONE);
            build(result);
        }

        @Override
        protected String doInBackground(String... url) {
            try {
                //show the status that it's loading
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

            } catch (InterruptedException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return resString;
        }
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

    public class DateComparator implements Comparator<CLEvent> {
        public int compare(CLEvent o1, CLEvent o2) {
            if (o1.getDate().before(o2.getDate())) {
                return -1;
            } else if (o1.getDate().after(o2.getDate())) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
