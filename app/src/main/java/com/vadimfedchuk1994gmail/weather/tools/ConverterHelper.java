package com.vadimfedchuk1994gmail.weather.tools;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.os.ConfigurationCompat;

public class ConverterHelper {


    public static String convertTimeStampToDate(long timestamp, Context mContext) {
        Locale current = ConfigurationCompat.getLocales(mContext.getResources().getConfiguration()).get(0);
        Date date = new Date(timestamp * 1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("HH:mm", current);
        return jdf.format(date);
    }

    public static double convertMbToMm(double mb) {
        return (mb / 1.333);
    }

    public static double convertCelsiusToFahrenheit(double temperature) {
        return (temperature * 9 / 5) + 32;
    }

    public static String convertToDate(String date, Context mContext) {
        Locale current = ConfigurationCompat.getLocales(mContext.getResources().getConfiguration()).get(0);
        Locale loc = new Locale(WeatherPreferences.getStoredLanguage(mContext));
        SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd", current);
        SimpleDateFormat toFormat = new SimpleDateFormat("EEEE, dd MMMM", loc);
        String reformattedDate;
        try {
            reformattedDate = toFormat.format(fromFormat.parse(date));
        } catch (ParseException e) {
            return date;
        }
        return reformattedDate;
    }
}
