package com.CampusLife.Campus_Life15;

import java.util.Comparator;

/**
 * Created by Reginald on 10/30/2015.
 */
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
