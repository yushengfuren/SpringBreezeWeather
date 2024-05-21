package com.elysia.springbreezeweather.gson.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecasts {
    @SerializedName("daily")
    public List<Forecast> forecasts;

    public class Forecast {
        @SerializedName("fxDate")
        public String date;

        @SerializedName("tempMax")
        public String tempMax;

        @SerializedName("tempMin")
        public String tempMin;

        @SerializedName("iconDay")
        public String iconDay;

        @SerializedName("textDay")
        public String textDay;
        @SerializedName("iconNight")
        public String iconNight;

        @SerializedName("textNight")
        public String textNight;

    }
}
