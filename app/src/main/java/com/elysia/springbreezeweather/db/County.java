package com.elysia.springbreezeweather.db;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    private String adcode;
    private String countyName;
    private String cityAdcode;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityAdcode() {
        return cityAdcode;
    }

    public void setCityAdcode(String cityAdcode) {
        this.cityAdcode = cityAdcode;
    }
}
