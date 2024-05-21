package com.elysia.springbreezeweather.gson.weather;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("adcode")
    public String adcode;

    @SerializedName("name")
    public String name;

    @SerializedName("aqi")
    public Aqi aqi;

    @SerializedName("forecasts")
    public Forecasts forecasts;

    @SerializedName("nowWeather")
    public NowWeather nowWeather;

    @SerializedName("suggestions")
    public Suggestions suggestions;

}
