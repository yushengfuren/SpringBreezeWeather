package com.elysia.springbreezeweather.gson.weather;

import com.google.gson.annotations.SerializedName;

public class Aqi {
    @SerializedName("now")
    public Now_Aqi now_aqi;

    public class Now_Aqi {
        @SerializedName("aqi")
        public String aqi;

        @SerializedName("pm2p5")
        public String pm2p5;
    }
}
