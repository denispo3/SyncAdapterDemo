package com.thinkmobiles.demo.syncadapter.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by denis on 22.07.15.
 */
public class ForecastModel {

    @SerializedName("weather")
    private List<Weather> weatherList;
    @SerializedName("main")
    private Main mainInfo;
    @SerializedName("wind")
    private Wind windInfo;

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public Main getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(Main mainInfo) {
        this.mainInfo = mainInfo;
    }

    public Wind getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(Wind windInfo) {
        this.windInfo = windInfo;
    }
}
