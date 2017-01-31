package com.CampusLife.Campus_Life15;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ScrollView;


public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_info);
        // Set the layout for fragment_layout.xml
        //fragment_layout.xml goes to the fragmentTitle class...see that for layout info
        setContentView(R.layout.info_webview);
        // Create a ScrollView to put your hero data in
        //ScrollView scroller = new ScrollView(this);

        //we will load a webpage based off of the information
        WebView webview = (WebView) findViewById(R.id.webinfo);
        webview.getSettings().setUserAgentString("Android");
        webview.loadUrl("http://clayton.edu/campus-life");
        /*webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int m_progress) {
                // Activities and WebViews measure m_progress with different scales.
                // The m_progress meter will automatically disappear when we reach 100%
                //if (this != null) {//fixes null pointer issue
                    this.setProgress(m_progress * 1000);
                //}
            }
        });*/
        //scroller.addView(webview);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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
        overridePendingTransition(R.anim.backacceldecelexit,R.anim.backacceldecel);
        return super.onOptionsItemSelected(item);
    }
}
