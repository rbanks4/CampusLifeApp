package com.CampusLife.Campus_Life15;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Reginald on 10/28/2015.
 */
public class SplashScreen {
    private LinearLayout splashLayout;
    private LinearLayout.LayoutParams sParams;
    private ImageView image;
    private Context context;
    private ProgressDialog progress;

    public SplashScreen(){

    }

    public void setContext(Context context){
        this.context = context;
        splashLayout = new LinearLayout(context);
        setParams();
    }

    private void setParams(){
        sParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        sParams.gravity = Gravity.CENTER_HORIZONTAL;
        sParams.gravity = Gravity.CENTER_VERTICAL;

        //images will stack on top of each other and center horizontally
        splashLayout.setLayoutParams(sParams);
        splashLayout.setOrientation(LinearLayout.VERTICAL);
        splashLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
        splashLayout.setBackgroundColor(0xff0e4c8b); //Clayton Blue
    }

    public void setImage(int resource){
        image = new ImageView(context);
        image.setImageResource(resource);
        splashLayout.addView(image);
    }

    public void setProgress(ProgressDialog progress){
        this.progress = progress;
        progress.show();
    }

    public void updateProgress(int value){
        progress.setProgress(value);
    }

    public void progressMessage(String message){
        progress.setMessage(message);
    }

    public LinearLayout getSplash(){
        return splashLayout;
    }

    public void gone(){
        progress.dismiss();
        image.setVisibility(View.GONE);
        splashLayout.setVisibility(View.GONE);
    }

}
