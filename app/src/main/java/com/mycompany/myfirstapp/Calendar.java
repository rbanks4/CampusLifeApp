package com.mycompany.myfirstapp;

import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;


public class Calendar extends Activity {

    /*
    We use Date and DateFormat in order to show the user the current date.
    Cal limit is used to limit list of events we show.
     */
    Date date = new Date();
    DateFormat day = new SimpleDateFormat("EEEE MMMM dd, yyyy  hh:mm:ss a");
    int calLimit = 30;

    com.google.api.services.calendar.Calendar mService;

    GoogleAccountCredential credential;
    private TextView mStatusText;
    private TextView mResultsText;
    private ListView listview;
    private Button button;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    /*
    The CalendarAdapter class is used to hold our custom list of events.
     */

    private CalendarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //this text is shown at the top of the screen and only shows when data is being retrieved
        //from the network.
        mStatusText = new TextView(this);
        mStatusText.setLayoutParams(tlp);
        mStatusText.setTypeface(null, Typeface.BOLD);
        mStatusText.setText("Retrieving data...");
        activityLayout.addView(mStatusText);

        //this button is created at the bottom of the screen
        button = new Button(this);
        button.setLayoutParams(bot);
        button.setText("See More Dates");
        button.setVerticalScrollBarEnabled(true);
        button.setBackgroundColor(0xff0e4c8b);//blue
        button.setTextColor(0xffffffff);//white
        mainLayout.addView(button);

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

        mResultsText = new TextView(this);
        //mResultsText.setLayoutParams(tlp);
        mResultsText.setPadding(16, 16, 16, 16);
        mResultsText.setVerticalScrollBarEnabled(true);
        mResultsText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mResultsText);

        mainLayout.addView(activityLayout);

        setContentView(mainLayout);

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        // Have a button at the bottom of the list so you can see more events
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calLimit += 10;
                refreshResults();
            }
        });

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

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        Calendar.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        /*startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);*/
        credential.setSelectedAccountName("p3anutbj7@gmail.com");
        //in order to stop it from asking for an account, I set the account to my gmail
        //the gmail has a dummy Campus Life Calendar setup on it; the calender is still in creation
        ////calendar due Aug 5th
    }

    public void updateResultsText(final List<String> dataStrings, final ArrayList<CLEvent> mylist) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {
                    mStatusText.setText("Error retrieving data!");
                } else if (dataStrings.size() == 0) {
                    mStatusText.setText("No data found.");
                } else {
                    mStatusText.setText("Data retrieved from" +
                            " Campus Life Calendar:");
                    mResultsText.setText(TextUtils.join("\n\n", dataStrings));
                    mResultsText.setText(" ");
                    /*for (String date : dates){ //Reg: just learned how to do a foreach in java
                        //list.add(new CLEvent(date,title[i],time[i],location[i],sponsor[i], desc[i]));
                        //i++;
                    }*/
                    adapter.setObjects(mylist);
                    listview.setAdapter(adapter);
                    //this is where we will put the view
                }
            }
        });
    }

    public void updateStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(message);
            }
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        refreshResults();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new ApiAsyncTask(this).execute();
            } else {
                mStatusText.setText("No network connection available.");
            }
        }
    }

    public void clearResultsText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText("Retrieving data…");
                mResultsText.setText("");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mStatusText.setText("Google Play Services required: " +
                    "after installing, close and relaunch this app.");
        }
    }

    public void Cal(){
        //Reg: we use Intent to call a new activity
        TextView textViewToChange = (TextView) findViewById(R.id.date);
        textViewToChange.setText(day.format(date));
    }
}
