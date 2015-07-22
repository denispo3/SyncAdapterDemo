package com.thinkmobiles.demo.syncadapter.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.thinkmobiles.demo.syncadapter.MainActivity;
import com.thinkmobiles.demo.syncadapter.R;
import com.thinkmobiles.demo.syncadapter.api.WeatherClient;

import java.io.IOException;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by denis on 22.07.15.
 */
public class WeatherAppWidget extends AppWidgetProvider {

    private static final String LOG_TAG = "WeatherAppWidget";

    public static final String EXTRA_TAG_TEMP = "temp";
    public static final String EXTRA_TAG_DESCRIPTION = "description";
    public static final String EXTRA_TAG_WIND = "wind";
    public static final String EXTRA_TAG_IMG = "img";
    public static final String EXTRA_TAG_HUMIDITY = "humidity";
    public static final String EXTRA_TAG_PRESSURE = "pressure";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        Bundle extras = intent.getExtras();

        Log.d(LOG_TAG, "onReceive: " + (extras == null));

        final int widgetsCount = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        if (extras != null) {
            // Create an Intent to launch activity
            Intent startIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            views.setOnClickPendingIntent(R.id.container, pendingIntent);
            views.setTextViewText(R.id.tv_title, "Weather: " + extras.getString(EXTRA_TAG_DESCRIPTION));
            float temp = extras.getFloat(WeatherAppWidget.EXTRA_TAG_TEMP);// - 273.15f;
            views.setTextViewText(R.id.tv_temperature, "Temperature: " + temp + "\u2103");
            views.setTextViewText(R.id.tv_wind, "Wind: " + extras.getFloat(EXTRA_TAG_WIND) + "m/s");
            views.setTextViewText(R.id.tv_pressure, "Pressure: " + extras.getFloat(EXTRA_TAG_PRESSURE) + "hPa");
            views.setTextViewText(R.id.tv_humidity, "Humidity: " + extras.getFloat(EXTRA_TAG_HUMIDITY) + "%");
            views.setTextViewText(R.id.tv_update_time, "Last update: " + new Date());

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetIds, views);

            WeatherClient.getWeatherService().getIcon(extras.getString(EXTRA_TAG_IMG) + ".png", new Callback<Response>() {
                @Override
                public void success(Response bitmap, Response response) {
                    try {
                        views.setImageViewBitmap(R.id.iv_icon, BitmapFactory.decodeStream(bitmap.getBody().in()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, views);
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }
    }
}
