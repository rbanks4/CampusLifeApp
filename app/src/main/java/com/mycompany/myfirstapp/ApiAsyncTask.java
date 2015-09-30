package com.mycompany.myfirstapp;

import android.os.AsyncTask;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;


import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private Calendar mActivity;
    public int limit;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(Calendar activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearResultsText();
            mActivity.updateResultsText(getDataFromApi(),getListViewFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    Register.REQUEST_AUTHORIZATION);

        } catch (IOException e) {
            mActivity.updateStatus("The following error occurred: " +
                    e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Reg: this function will parse our dateTime from the calendar correctly into any format
    public static java.util.Date parseRFC3339Date(String datestring) throws java.text.ParseException, IndexOutOfBoundsException{
        Date d = new Date();

        //Reg: if there is no time zone, we don't need to do any special parsing.
        if(datestring.endsWith("Z")){
            try{
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//spec for RFC3339
                d = s.parse(datestring);
            }
            catch(java.text.ParseException pe){//try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");//spec for RFC3339 (with fractional seconds)
                s.setLenient(true);
                d = s.parse(datestring);
            }
            return d;
        }

        //step one, split off the timezone. -Reg
        String firstpart = datestring.substring(0,datestring.lastIndexOf('-'));
        String secondpart = datestring.substring(datestring.lastIndexOf('-'));

        //step two, remove the colon from the timezone offset -Reg
        secondpart = secondpart.substring(0,secondpart.indexOf(':')) + secondpart.substring(secondpart.indexOf(':')+1);
        datestring  = firstpart + secondpart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339
        try{
            d = s.parse(datestring);
        }
        catch(java.text.ParseException pe){//try again with optional decimals
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(datestring);
        }
        return d;
    }
    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<String> getDataFromApi() throws Exception {
        // List the next 10 events from the primary calendar. -Reg
        DateTime now = new DateTime(System.currentTimeMillis());
        DateFormat day = new SimpleDateFormat("EEEE MMMM dd, yyyy"),
                time = new SimpleDateFormat("hh:mm a");
        ArrayList<CLEvent> list = new ArrayList<CLEvent>();

        //String date = new String();
        Date Date = new Date(),
            EndDate = new Date();
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();

            //date = day.format(String.format("%s",event.getStart().getDateTime()));

            if ((start == null)&&(event.getLocation()==null)) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
                //list.add(new CLEvent(day.format(Date),event.getSummary()));
                //this way, we can convert what we have into a format that I like
                //date = day.format(String.format("%s", event.getStart().getDate()));
            }
            else if ((start == null)) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
                //list.add(new CLEvent(day.format(Date),event.getSummary(),event.getLocation()));
                //this way, we can convert what we have into a format that I like
                //date = day.format(String.format("%s", event.getStart().getDate()));
            }
            else {
                Date = parseRFC3339Date(start.toStringRfc3339());
                EndDate = parseRFC3339Date(start.toStringRfc3339());
                String times = String.format("%s - %s", time.format(Date), time.format(EndDate));
                list.add(new CLEvent(day.format(Date), event.getSummary(), times, event.getLocation(), event.getDescription()));
                eventStrings.add(
                        String.format("%s \n%s \n%s - %s \n%s \n%s", day.format(Date), event.getSummary(),
                                time.format(Date), time.format(EndDate),
                                event.getLocation(), event.getDescription()));
            }
            /*this should read, for example:
            July 15th
            Summer Fling
            6:00 pm - 10:00 pm
            Pool House
            (Description)
            */

        }
        return eventStrings;
    }

    private ArrayList<CLEvent> getListViewFromApi() throws Exception {
        limit = mActivity.calLimit;
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        DateFormat day = new SimpleDateFormat("EEEE MMMM dd, yyyy"),
                time = new SimpleDateFormat("hh:mm a"),
                oneday = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<CLEvent> list = new ArrayList<CLEvent>();

        //String date = new String();
        Date Date = new Date(),
                EndDate = new Date();
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(limit)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            //date = day.format(String.format("%s",event.getStart().getDateTime()));


            if ((start == null)&&(event.getLocation()==null)) {
                // All-day events don't have start times, so just use
                // the start date.
                Date = oneday.parse(event.getStart().getDate().toStringRfc3339());

                list.add(new CLEvent(day.format(Date),event.getSummary(), event.getDescription()));

            }
            else if ((start == null)) {//date,title,location,desc
                // All-day events don't have start times, so just use
                // the start date.
                Date = oneday.parse(event.getStart().getDate().toStringRfc3339());
                list.add(new CLEvent(day.format(Date), event.getSummary(),event.getLocation(),
                            event.getDescription()));
            }
            else {
                Date = parseRFC3339Date(event.getStart().getDateTime().toStringRfc3339());
                EndDate = parseRFC3339Date(event.getEnd().getDateTime().toStringRfc3339());
                String times = String.format("%s - %s", time.format(Date), time.format(EndDate));
                list.add(new CLEvent(day.format(Date), event.getSummary(), times, event.getLocation(), event.getDescription()));
                eventStrings.add(
                        String.format("%s \n%s \n%s - %s \n%s \n%s", day.format(Date), event.getSummary(),
                                time.format(Date), time.format(EndDate),
                                event.getLocation(), event.getDescription()));
            }
            /*this should read, for example:
            July 15th
            Summer Fling
            6:00 pm - 10:00 pm
            Pool House
            (Description)
            */

        }
        return list;
    }

}