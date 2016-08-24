package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
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
    //int calLimit = 30;
    public String resString = " ";
    String caltest;
    private WebView mWebView;
    //String glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    String glURL = "https://calendar.google.com/calendar/ical/p3anutbj7%40gmail.com/public/basic.ics";
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
    private net.fortuna.ical4j.model.Calendar m_calendar;
    private static final String CAL_LOG = "Calendar";

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

            //setContentView(R.layout.activity_calendar);
            //tview.setText(result);
            //progress.setVisibility(View.GONE);
            build(result);
        }

        @Override
        protected String doInBackground(String... url) {
            String filename = "calendar.ics";
            FileOutputStream outputStream;
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

            } catch (InterruptedException e) {

                e.printStackTrace();


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                File ocalFile = new File(Environment.getExternalStorageDirectory(), filename);
                //outputStream = openFileOutput(calFile);
                outputStream = new FileOutputStream(ocalFile);
                outputStream.write(resString.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                File icalFile = new File(Environment.getExternalStorageDirectory(), filename);
                FileInputStream fin = new FileInputStream(icalFile);
                readFile(icalFile);
                events.get(0);
//                CalendarBuilder builder = new CalendarBuilder();
//                m_calendar = builder.build(fin);
            } catch (Exception e){
                Log.e("ERROR", "could not find calendar file", e);
                e.printStackTrace();
            }

            return resString;
        }



    }
    private String splitLine(String line){
        String[] splitter = line.split(":");
        if(splitter.length > 1){
            return splitter[1];
        }
        else
            return "";
    }

    List<Event> events = new ArrayList<Event>();
    public void readFile(File file){

        Log.i(CAL_LOG,"inside of readFile");
        int count = 0;
        int eventNum = 0;
        String[] splitter;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            Log.i(CAL_LOG, "made it into the try data");
            while ((line = br.readLine()) != null) {
                Log.i(CAL_LOG, "trying to read line:" + line + " where count is:" + count);
                switch(count){
                    case(1):
                        Event a = new Event();
                        a.setStartDate(splitLine(line));
                        events.add(a);
                        count++;
                        break;
                    case(2):
                        events.get(eventNum).setEndDate(splitLine(line));
                        count++;
                        break;
                    case(6):
                        events.get(eventNum).setDescription(splitLine(line));
                        count++;
                        break;
                    case(8):
                        events.get(eventNum).setLocation(splitLine(line));
                        count++;
                        break;
                    case(11):
                        events.get(eventNum).setSummary(splitLine(line));
                        count++;
                        break;
                    case(3):
                    case(4):
                    case(5):
                    case(7):
                    case(9):
                    case(10):
                        count++;
                        break;
                    case(12):
                        count = 0;
                        eventNum++;
                        break;
                    default:
                        //process the line
                        if(line.contains("BEGIN:VEVENT"))
                        {
                            count++;
                        }
                        continue;
                }

            }
        }
        catch(Exception e){
            Log.i("ERROR", "could not find calendar file", new Exception());
        }
    }

    public void build(String res){
        //create a document to find all of the elements
//        caldoc = cutty.getDomElement(res);//makes the document
//        caldoc.getDocumentElement().normalize();
//        NodeList nList = caldoc.getElementsByTagName("entry");//makes a list of nodes
//        caltest = " ";//now that we have our doc, we can clear it

//        for (int temp = 0; temp < nList.getLength(); temp++) {//todo split data into bite sized events
//            Node nNode = nList.item(temp);
//            caltest.concat("\nCurrent Element :"
//                    + nNode.getNodeName());
//            if (nNode.getNodeType() == Node.ELEMENT_NODE) {//logic returning somthing else
//                Element eElement = (Element) nNode;
//                    /*resString.concat("Title : "
//                            + eElement.getAttribute("rollno"));*/
//                String Title = (eElement.getElementsByTagName("title").item(0).getTextContent()
//                        +"\n");
//
//                //Log.w("myApp", caltest);
//                String Summary = (eElement.getElementsByTagName("summary").item(0).getTextContent()
//                        +"\n");
//                //List<String> desc = new ArrayList<String>(cparse.fixSummary(Summary));
//                String[] changes = cparse.fixSummary(Summary);
//                //desc.toArray(changes);
//
//                CLEvent event = new CLEvent(changes[0], Title, changes[1], changes[2], changes[3]);
//                //Log.w("myApp", "event with 5 elements: " + changes[0] + Title + changes[1] + changes[2] + changes[3]);
//                elist.add(event);
//            }
//
//        }
        for (Event e : events){
            if (e.getSummary().length() > 1) {
                CLEvent event = new CLEvent(e.getStartDate(), e.getSummary(), e.getEndDate(), e.getLocation(), e.getDescription());
                elist.add(event);
            }
        }



        //resString=cutty.getValue(caldoc.getElement,"title");
        // TODO Auto-generated method stub
        //onPostExecute(resString);
        //progress.setVisibility(View.GONE);
        //Collections.sort(elist);
        //TODO fix this in due time
//        Collections.sort(elist, new DateComparator());
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




}
