package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Finals extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finals);

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.finals_list);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        Log.w("test", listAdapter.getData());
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        //listDataHeader.add("Monday - Wednesday - Friday");
        listDataHeader.add("MWF Class");
        listDataHeader.add("M, W, or MW Class");
        listDataHeader.add("TR Class");
        listDataHeader.add("MTWR Class");
        listDataHeader.add("Friday Class");
        listDataHeader.add("Saturday Class");

        // Adding child data
        List<String> MWF = new ArrayList<String>();
        //todo separating data
        MWF.add("MWF 7:00 -7:50 AM,Dec 9 (Fri),8:00 AM - 10:00 AM");
        MWF.add("MWF 8:00 - 8:50 AM,Dec 7 (Wed),8:00 AM - 10:00 AM");
        MWF.add("MWF 9:00 - 9:50 AM,Dec 12 (Mon),8:00 AM - 10:00 AM");
        MWF.add("MWF 10:00 - 10:50 AM,Dec 9 (Fri),10:15 AM - 12:15 PM");
        MWF.add("MWF 11:00 - 11:50 AM,Dec 9 (Fri),12:30 PM - 2:30 PM");

        List<String> MW = new ArrayList<String>();
        MW.add("M or MW 12:45 - 2:00 PM ,Dec 12 (Mon) ,10:15 AM - 12:15 PM");
        MW.add("W 12:45-2:00 PM ,Dec 7 (Wed) ,10:15 AM - 12:15 PM");
        MW.add("M or MW 2:10 - 3:25 PM ,Dec 12 (Mon) ,12:30 PM - 2:30 PM");
        MW.add("W 2:10 - 3:25 PM ,Dec 7 (Wed) ,12:30 PM - 2:30 PM");
        MW.add("M or MW 3:35-4:50 PM ,Dec 12 (Mon) ,2:45 PM - 4:45 PM");
        MW.add("W 3:35 - 4:50 PM ,Dec 7 (Wed) ,2:45 PM - 4:45 PM");
        MW.add("M or MW 5:00 - 6:15 PM ,Dec 12 (Mon) ,5:00 PM - 7:00 PM");
        MW.add("W 5:00 - 6:15 PM ,Dec 7 (Wed) ,5:00 PM - 7:00 PM");
        MW.add("M or MW 6:30 - 7:45 PM ,Dec 12 (Mon) ,7:15 PM - 9:15 PM");
        MW.add("W 6:30 - 7:45 PM ,Dec 7 (Wed) ,7:15 PM - 9:15 PM");
        MW.add("M or MW 8:00- 9:15 PM ,Dec 12 (Mon) ,9:30 PM - 11:30 PM");
        MW.add("W 8:00 - 9:15 PM ,Dec 7 (Wed) ,9:30 PM - 11:30 PM");

        List<String> TR = new ArrayList<String>();
        TR.add("T or TR 8:25 - 9:40 AM ,Dec 6 (Tues) ,8:00 AM - 10:00 AM");
        TR.add("T or TR 9:50 - 11:05 AM ,Dec 8 (Thurs) ,8:00 AM - 10:00 AM");
        TR.add("T or TR 11:15 AM -12: 30 PM ,Dec 6 (Tues) ,10:15 AM - 12:15 PM");
        TR.add("T or TR 12:45 - 2:00 PM ,Dec 8 (Thurs) ,10:15 AM - 12:15 PM");
        TR.add("R 12:45 - 2:00 PM ,Dec 6 (Tues) ,12:30 PM - 2:30 PM");
        TR.add("T or TR 2:10 - 3:25 PM ,Dec 6 (Tues) ,2:45 PM - 4:45 PM");
        TR.add("R 2:10 - 3:25 PM ,Dec 8 (Thurs) ,12:30 PM - 2:30 PM");
        TR.add("T or TR 3:35 - 4:50 PM ,Dec 8 (Thurs) ,2:45 PM - 4:45 PM");
        TR.add("T or TR 5:00 - 6:15 PM ,Dec 6 (Tues) ,5:00 PM - 7:00 PM");
        TR.add("T or TR 6:30 - 7:45 PM ,Dec 8 (Thurs) ,5:00 PM - 7:00 PM");
        TR.add("R 6:30 -7:45 PM ,Dec 8 (Thurs) ,7:15 PM - 9:15 PM");
        TR.add("T or TR 8:00 - 9:15 PM ,Dec 6 (Tues) ,7:15 PM - 9:15 PM");

        List<String> MTWR = new ArrayList<String>();
        MTWR.add("MTWH 8:00 - 8:50 AM ,Dec 7 (Wed) ,8:00 AM - 10:00 AM");
        MTWR.add("MTWH 10:15 - 10:50 AM ,Dec 9 (Fri) ,10:15 AM - 12:15 PM");
        MTWR.add("MTWH 2:10 -3:00 PM ,Dec 12 (Mon) ,12:30 PM - 2:30 PM");


        List<String> Fri = new ArrayList<String>();
        Fri.add("F 12:00 - 2:00 PM ,Dec 9 (Fri) ,2:45 PM - 4:45 PM");
        Fri.add("F 2:15 - 4:45 PM ,Dec 9 (Fri) ,5:00 PM - 7:00 PM");
        Fri.add("F 5:00 - 7:30 PM ,Dec 9 (Fri) ,7:15 PM - 9:15 PM");

        List<String> Sat = new ArrayList<String>();
        Sat.add("7:30 - 10:15 AM S/9:00- 10:15 AM ,Dec 10 (Sat) ,8:00 AM - 10:00 AM");
        Sat.add("10:30 - 1:15 PM S/10:30 - 11:45 AM ,Dec 10 (Sat) ,10:15 AM - 12:15 PM");
        Sat.add("12:00 -1:15 PM ,Dec 10 (Sat) ,12:30 PM - 2:30 PM");
        Sat.add("1:30 - 4:30 PM S ,Dec 10 (Sat) ,2:45 PM - 4:45 PM");

        listDataChild.put(listDataHeader.get(0), MWF); // Header, Child data
        listDataChild.put(listDataHeader.get(1), MW);
        listDataChild.put(listDataHeader.get(2), TR);
        listDataChild.put(listDataHeader.get(3), MTWR);
        listDataChild.put(listDataHeader.get(4), Fri);
        listDataChild.put(listDataHeader.get(5), Sat);

        /*//children can be split here then sent to adapter
        for (String childText : Sat) {
            String children[] = childText.split(",");
            for (String child : children){
                Log.w("children",child);
            }
        }

        for (String days_l : listDataHeader){
            Log.w("Data", days_l);
        }*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
