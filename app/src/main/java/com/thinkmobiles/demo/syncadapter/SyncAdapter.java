package com.thinkmobiles.demo.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.thinkmobiles.demo.syncadapter.api.ForecastModel;
import com.thinkmobiles.demo.syncadapter.api.Weather;
import com.thinkmobiles.demo.syncadapter.api.WeatherClient;
import com.thinkmobiles.demo.syncadapter.appwidget.WeatherAppWidget;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by denis on 18.07.15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = "denis";

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync");
        WeatherClient.getWeatherService().getForecastModel(callback);
    }

    private Callback<ForecastModel> callback = new Callback<ForecastModel>() {
        @Override
        public void success(ForecastModel forecastModel, Response response) {
            Log.d(LOG_TAG, "success callback");

            Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
            intent.setPackage(getContext().getPackageName());
            intent.putExtras(makeBundle(forecastModel));
            getContext().sendBroadcast(intent);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(LOG_TAG, error.getMessage());
        }
    };

    // Put parsed results to Bundle to send via broadcast
    private Bundle makeBundle (ForecastModel forecastModel) {
        Bundle extras = new Bundle();
        extras.putFloat(WeatherAppWidget.EXTRA_TAG_TEMP, forecastModel.getMainInfo().temp);
        extras.putFloat(WeatherAppWidget.EXTRA_TAG_HUMIDITY, forecastModel.getMainInfo().humidity);
        extras.putFloat(WeatherAppWidget.EXTRA_TAG_PRESSURE, forecastModel.getMainInfo().pressure);
        extras.putFloat(WeatherAppWidget.EXTRA_TAG_WIND, forecastModel.getWindInfo().speed);
        if (forecastModel.getWeatherList() != null) {
            List<Weather> weatherList = forecastModel.getWeatherList();
            Weather weather = weatherList.get(0);
            extras.putString(WeatherAppWidget.EXTRA_TAG_DESCRIPTION, weather.description);
            extras.putString(WeatherAppWidget.EXTRA_TAG_IMG, weather.icon);
        }
        return extras;
    }
}
