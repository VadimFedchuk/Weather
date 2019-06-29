package com.vadimfedchuk1994gmail.weather.pojo;

public class TypeWeather {

    private String description;
    private int resourceIdIcon;
    private int resourceIdBackground;

    public TypeWeather(String description, int resourceIdIcon, int resourceIdBackground) {
        this.description = description;
        this.resourceIdIcon = resourceIdIcon;
        this.resourceIdBackground = resourceIdBackground;
    }

    public String getDescription() {
        return description;
    }

    public int getResourceIdIcon() {
        return resourceIdIcon;
    }

    public int getResourceIdBackground() {
        return resourceIdBackground;
    }
}
