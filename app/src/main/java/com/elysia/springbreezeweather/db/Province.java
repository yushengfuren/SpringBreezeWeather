package com.elysia.springbreezeweather.db;

import org.litepal.crud.LitePalSupport;

public class Province extends LitePalSupport {
    private String adcode;
    private String provinceName;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
