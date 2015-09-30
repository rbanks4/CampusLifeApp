package com.mycompany.myfirstapp;

import android.widget.ViewFlipper;

public class CLEvent {

    private String prop0;
    private String prop1;
    private String prop2;
    private String prop3;
    //private String prop4;
    private String prop5;
    private ViewFlipper flip;
    public int color;

    public CLEvent(String prop0, String prop1, String prop2, String prop3, String prop5) {
        this.prop0 = prop0;//date
        this.prop1 = prop1;//title
        this.prop2 = prop2;//time
        this.prop3 = prop3;//location
        //this.prop4 = prop4;//sponsor
        this.prop5 = prop5;//description
        color = 0xff0e4c8b;//Reg: Blue for regular events
    }
    public CLEvent(String prop0, String prop1){//reserved for all day events
        this.prop0 = prop0;//date
        this.prop1 = prop5 = prop1;//title
        prop2 = "All Day";
        prop3 = " ";
        checkColor();
    }
    public CLEvent(String prop0, String prop1, String prop5){//reserved for all day events
        this.prop0 = prop0;//date
        this.prop1 = prop1;//title
        prop2 = "All Day";
        prop3 = " ";
        if (prop5 == null){
            this.prop5 = "No description available.";
        }
        else {
            this.prop5 = prop5;//desc
        }
        checkColor();
    }
    public CLEvent(String prop0, String prop1, String prop2, String prop5){//all day events with location
        this.prop0 = prop0;//date
        this.prop1 = prop1;//title
        this.prop2 = prop2;//location
        prop3 = "All Day";
        if (prop5 == null){
            this.prop5 = "No description available.";
        }
        else {
            this.prop5 = prop5;//desc
        }
        checkColor();
    }

    private void checkColor(){
        if (prop1.contains("No Classes") || prop1.contains("no classes")
                || prop1.contains("No Class") || prop1.contains("no class")){//check the title name for "No Classes
            color = 0xff000000;//black for no classes
        }
        else{
            color = 0xfff4821f;//orange for all day
        }
    }

    public String getProp0() { return prop0; }

    public String getProp1() {
        return prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public String getProp3() {
        return prop3;
    }

    //public String getProp4() { return prop4; }

    public String getProp5() { return prop5; }

    public ViewFlipper getFlip() { return flip; }

    public void setFlip(ViewFlipper f){this.flip = f; }

}