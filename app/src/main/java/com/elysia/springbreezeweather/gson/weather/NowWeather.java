package com.elysia.springbreezeweather.gson.weather;

import com.google.gson.annotations.SerializedName;

public class NowWeather {
    @SerializedName("now")
    public Now_Weather now_weather;

    public class Now_Weather {
        @SerializedName("obsTime")
        public String time;

        @SerializedName("temp")
        public String temperature;

        @SerializedName("feelsLike")
        public String feelsLike;

        @SerializedName("icon")
        public String icon;

        @SerializedName("text")
        public String text;
    }

}
