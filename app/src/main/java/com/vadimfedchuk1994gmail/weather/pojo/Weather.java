package com.vadimfedchuk1994gmail.weather.pojo;

import com.vadimfedchuk1994gmail.weather.network.weather_response.Datum;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Weather {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String date;
    private String description;
    private String icon;
    private double temperature;
    private double max_temp;
    private double min_temp;
    private long pop; // вероятность осадков
    private double pres; // давление
    private long sunrise;
    private long sunset;
    private String wind_dir; // направление ветра
    private double wind_speed;


    public Weather(String name, String date, String description, String icon, double temperature,
                   double max_temp, double min_temp, long pop, double pres, long sunrise,
                   long sunset, String wind_dir, double wind_speed) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.pop = pop;
        this.pres = pres;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.wind_dir = wind_dir;
        this.wind_speed = wind_speed;
    }

    public static List<Weather> cloneList(List<Datum> weatherResponses, String city) {
        List<Weather> list = new ArrayList<>();
        for (Datum data : weatherResponses) {
            list.add(new Weather(
                    city,
                    data.getValidDate(),
                    data.getWeather().getDescription(),
                    data.getWeather().getIcon(),
                    data.getTemp(),
                    data.getMaxTemp(),
                    data.getMinTemp(),
                    data.getPop(),
                    data.getPres(),
                    data.getSunriseTs(),
                    data.getSunsetTs(),
                    data.getWindCdirFull(),
                    data.getWindSpd()));
        }
        return list;
    }

    @Ignore
    public Weather(String date, String description, String icon, double temperature, double max_temp, double min_temp, long pop, double pres, long sunrise, long sunset, String wind_dir, double wind_speed) {
        this.date = date;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.pop = pop;
        this.pres = pres;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.wind_dir = wind_dir;
        this.wind_speed = wind_speed;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        return name.equalsIgnoreCase(((Weather) obj).name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public long getPop() {
        return pop;
    }

    public double getPres() {
        return pres;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public double getWind_speed() {
        return wind_speed;
    }
}
