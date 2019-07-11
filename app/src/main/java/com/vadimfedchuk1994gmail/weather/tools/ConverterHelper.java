package com.vadimfedchuk1994gmail.weather.tools;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConverterHelper {

    public static String convertTimeStampToDate(long timestamp) {
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
        return jdf.format(date);
    }

    public static int convertMbToMm(double mb) {
        return (int) (mb / 1.333);
    }

    public static String convertCelsiusToFahrenheit(double temperature) {
        return (((int) temperature * 9 / 5) + 32) + "\u00B0F";
    }

    public static String convertToDate(String date) {
        String dayOfWeek = new SimpleDateFormat("EEEE, dd M", Locale.ENGLISH).format(date);
        Log.i(Const.LOG, dayOfWeek);
        return dayOfWeek;
    }
}
