package com.elysia.springbreezeweather.service;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elysia.springbreezeweather.R;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    ImageView dayWeatherIcon;
    ImageView nightWeatherIcon;
    TextView infoText;
    TextView minText;
    TextView maxText;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        dateText = itemView.findViewById(R.id.date_text);
        dayWeatherIcon = itemView.findViewById(R.id.day_weather_icon);
        nightWeatherIcon = itemView.findViewById(R.id.night_weather_icon);
        infoText = itemView.findViewById(R.id.info_text);
        minText = itemView.findViewById(R.id.min_text);
        maxText = itemView.findViewById(R.id.max_text);
    }
}

