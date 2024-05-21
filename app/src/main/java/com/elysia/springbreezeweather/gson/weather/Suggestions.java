package com.elysia.springbreezeweather.gson.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Suggestions {
    @SerializedName("daily")
    public List<Suggestion> suggestions;  // 为0时是运动，为1时是洗车，为2时是舒适度

    public class Suggestion {
        @SerializedName("name")
        public String name;

        @SerializedName("category")
        public String category;

        @SerializedName("text")
        public String text;
    }
}
