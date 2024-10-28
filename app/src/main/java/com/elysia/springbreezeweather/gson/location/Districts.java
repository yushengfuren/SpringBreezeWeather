package com.elysia.springbreezeweather.gson.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Districts {

    @SerializedName("adcode")
    public String adcode;

    @SerializedName("name")
    public String name;

    @SerializedName("districts")
    public List<District> subDistricts;
}
