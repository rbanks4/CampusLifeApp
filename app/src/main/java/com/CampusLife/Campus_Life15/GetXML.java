package com.CampusLife.Campus_Life15;

import android.app.Activity;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Reginald on 10/30/2015.
 */
class GetXML extends AsyncTask<Void, Integer, String> {
    //the link to our XML data
    String glURL = "https://www.google.com/calendar/feeds/dlc7torch%40gmail.com/private-7ee00fe08d8dd0be70fe658c2f363c7d/basic";
    String resString;
    ClCalendar activity;

    public void setActivity(ClCalendar activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        activity.splash.gone();
        activity.build(result);
    }

    @Override
    protected String doInBackground(Void... nope) {//yep...don't pass in anything
        try {
            //show the status that it's loading
            Thread.sleep(4000);

            URL google = new URL(glURL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) google.openConnection();
            publishProgress(10);
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            publishProgress(50);
            while ((line = reader.readLine()) != null) { // Read line by line
                sb.append(line + "\n");
            }
            publishProgress(75);

            resString = sb.toString(); // Result is here

            is.close(); // Close the stream
            urlConnection.disconnect();
            publishProgress(100);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resString;
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
