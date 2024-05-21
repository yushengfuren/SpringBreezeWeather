package com.elysia.springbreezeweather.db;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    private String adcode;
    private String cityName;
    private String provinceAdcode;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceAdcode() {
        return provinceAdcode;
    }

    public void setProvinceAdcode(String provinceAdcode) {
        this.provinceAdcode = provinceAdcode;
    }
}
