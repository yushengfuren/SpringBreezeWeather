package com.elysia.springbreezeweather.gson.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Districts {

    public List<District> districts;
    public class District {
        @SerializedName("adcode")
        public String adcode;

        @SerializedName("name")
        public String name;

        @SerializedName("districts")
        public List<District> subDistricts;
    }
}
