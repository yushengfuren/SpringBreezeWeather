package com.elysia.springbreezeweather.service;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elysia.springbreezeweather.R;
import com.elysia.springbreezeweather.gson.weather.Forecasts;

import java.util.List;

public class ForecastAdapter extends ArrayAdapter<Forecasts.Forecast> {
    private int resourceId;

    public ForecastAdapter(Context context, int textViewResourceId, List<Forecasts.Forecast> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Forecasts.Forecast forecast = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        TextView dateText = view.findViewById(R.id.date_text);
        ImageView dayWeatherIcon = view.findViewById(R.id.day_weather_icon);
        ImageView nightWeatherIcon = view.findViewById(R.id.night_weather_icon);
        TextView infoText = view.findViewById(R.id.info_text);
        TextView minText = view.findViewById(R.id.min_text);
        TextView maxText = view.findViewById(R.id.max_text);

        dateText.setText(forecast.date);

        // Use getContext() to access resources
        int resDayId = getContext().getResources().getIdentifier("w" + forecast.iconDay, "drawable", getContext().getPackageName());
        dayWeatherIcon.setImageResource(resDayId);

        int resNightId = getContext().getResources().getIdentifier("w" + forecast.iconNight, "drawable", getContext().getPackageName());
        nightWeatherIcon.setImageResource(resNightId);

        infoText.setText(forecast.textDay);
        minText.setText(forecast.tempMin + "℃");
        maxText.setText(forecast.tempMax + "℃");

        return view;
    }
}

