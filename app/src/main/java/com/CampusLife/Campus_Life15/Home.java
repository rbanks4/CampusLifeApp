package com.CampusLife.Campus_Life15;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class Home extends Activity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";


    //todo fetch and customize resolutions to peak user experience
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.w("BeforeA", "Yo! I Started!!!");

        final Animation animAccelerateDecelerate = AnimationUtils.loadAnimation(this, R.anim.accelerate_decelerate);
        //ImageView btnAccelerateDecelerate = (ImageView)findViewById(R.id.AboutUs);
        final ViewFlipper flippo = (ViewFlipper) findViewById(R.id.flippo);
        final ImageView image = (ImageView)findViewById(R.id.AboutUs);
        final Handler handler = new Handler();
        final ImageView calimage = (ImageView)findViewById(R.id.CalImage);


        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(true);
        //ab.setLogo(R.drawable.campuslife24);
        ab.setDisplayUseLogoEnabled(true);



        //flippo.setFlipInterval(1000);
        flippo.startFlipping();
        //flippo.setAutoStart(true);

        flippo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flippo.showNext();
            }
        });

        image.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                viewContact(arg0);
            }});
        //todo try to make a circular reveal (even though it should only work on lolipop
        /*// get the center for the clipping circle
        int cx = calimage.getWidth() / 2;
        int cy = calimage.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(calimage.getWidth(), calimage.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(calimage, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        calimage.setVisibility(View.VISIBLE);
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                calimage.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_search:
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
        */


    }

    /*so this is for the button we just made...swag
    * the Send button, when clicked, will do this method*/
    public void sendMessage(View view){
        //do something brah!
        //we use Intent to call a new activity
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        overridePendingTransition(R.anim.fadeleft, R.anim.acceldecel);

    }
    public void viewCalendar(View view){
        //do something brah!
        //we use Intent to call a new activity
        Intent intent = new Intent(this, ClCalendar.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        overridePendingTransition(R.anim.acceldecel, R.anim.colorblind);

    }
    public void viewInfo(View view){
        //do something brah!
        //we use Intent to call a new activity
        Intent intent = new Intent(this, Info.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        //enter,exit
        overridePendingTransition(R.anim.acceldecel, R.anim.colorblind);

    }
    public void viewContact(View view){
        Intent intent = new Intent(this, Contact.class);
        startActivity(intent);
        //activity comes in and the old one stays still
        overridePendingTransition(R.anim.acceldecel, R.anim.colorblind);

    }

    public void viewRegister(View view){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.acceldecel, R.anim.acceldecelexit);

    }



}
