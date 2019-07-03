package com.vadimfedchuk1994gmail.weather.pojo;

import com.vadimfedchuk1994gmail.weather.network.weather_response.Datum;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Weather {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String date;
    public String description;
    public String icon;
    public double temperature;
    public double max_temp;
    public double min_temp;
    public long pop; // вероятность осадков
    public double pres; // давление
    public long sunrise;
    public long sunset;
    public String wind_dir; // направление ветра
    public double wind_speed;


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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Weather)) {
            return false;
        }

        return name.equalsIgnoreCase(((Weather) obj).name);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (name != null ? name.hashCode() : 0);
        return hash;
    }
}
