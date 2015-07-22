package com.thinkmobiles.demo.syncadapter.api;

import retrofit.RestAdapter;

/**
 * Created by denis on 22.07.15.
 */
public class WeatherClient {

    private static WeatherService weatherService;

    public static WeatherService getWeatherService() {
        if (weatherService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.openweathermap.org")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            weatherService = restAdapter.create(WeatherService.class);
        }
        return weatherService;
    }
}
