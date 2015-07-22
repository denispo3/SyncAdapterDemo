package com.thinkmobiles.demo.syncadapter.api;

import android.graphics.Bitmap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by denis on 22.07.15.
 */
public interface WeatherService {

    @GET("/data/2.5/weather?q=Uzhhorod&units=metric")
    void getForecastModel(Callback<ForecastModel> callback);

    @GET("/img/w/{file}")
    void getIcon(@Path("file") String file, Callback<Response> callback);
}
