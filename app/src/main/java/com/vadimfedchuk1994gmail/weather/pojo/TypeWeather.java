package com.vadimfedchuk1994gmail.weather.pojo;

public class TypeWeather {

    private String description;
    private int resourceIdIcon;

    public TypeWeather(String description, int resourceIdIcon) {
        this.description = description;
        this.resourceIdIcon = resourceIdIcon;
    }

    public String getDescription() {
        return description;
    }

    public int getResourceIdIcon() {
        return resourceIdIcon;
    }
}
