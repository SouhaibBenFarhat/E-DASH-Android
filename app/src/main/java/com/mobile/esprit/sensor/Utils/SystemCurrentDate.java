package com.mobile.esprit.sensor.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class SystemCurrentDate {

    public static final String DATE_FORMAT_NOW = "dd MMM yyyy HH:mm";

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
