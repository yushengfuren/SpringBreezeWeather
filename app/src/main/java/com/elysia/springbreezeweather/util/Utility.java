package com.elysia.springbreezeweather.util;

import android.text.TextUtils;

import com.elysia.springbreezeweather.db.City;
import com.elysia.springbreezeweather.db.County;
import com.elysia.springbreezeweather.db.Province;
import com.elysia.springbreezeweather.gson.location.Districts;
import com.elysia.springbreezeweather.gson.weather.Aqi;
import com.elysia.springbreezeweather.gson.weather.Forecasts;
import com.elysia.springbreezeweather.gson.weather.NowWeather;
import com.elysia.springbreezeweather.gson.weather.Suggestions;
import com.elysia.springbreezeweather.gson.weather.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static Districts handleDistrictsResponse(String response) {
        Districts districts = new Gson().fromJson(response,Districts.class);
        return districts;
    }

    public static Aqi handleAqiResponse(String response) {
        Aqi aqi = new Gson().fromJson(response, Aqi.class);
        return aqi;
    }

    public static Forecasts handleForecastsResponse(String response) {
        Forecasts forecasts = new Gson().fromJson(response, Forecasts.class);
        return forecasts;
    }

    public static NowWeather handleNowWeatherResponse(String response) {
        NowWeather nowWeather = new Gson().fromJson(response, NowWeather.class);
        return nowWeather;
    }

    public static Suggestions handleSuggestionsResponse(String response) {
        Suggestions suggestions = new Gson().fromJson(response, Suggestions.class);
        return suggestions;
    }

    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            Districts country = Utility.handleDistrictsResponse(response);
            for (int i = 0; i < country.districts.get(0).subDistricts.size(); i++) {
                Districts.District provinceDist = country.districts.get(0).subDistricts.get(i);
                Province province = new Province();
                province.setProvinceName(provinceDist.name);
                province.setAdcode(provinceDist.adcode);
                province.save();
            }
            return true;
        }
        return false;
    }

    public static boolean handleCityResponse(String response, String provinceAdcode) {
        if (!TextUtils.isEmpty(response)) {
            Districts province = Utility.handleDistrictsResponse(response);
            for (int i =0; i < province.districts.get(0).subDistricts.size(); i++) {
                Districts.District cityDist = province.districts.get(0).subDistricts.get(i);
                City city = new City();
                city.setCityName(cityDist.name);
                city.setAdcode(cityDist.adcode);
                city.setProvinceAdcode(provinceAdcode);
                city.save();
            }
            return true;
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, String cityAdcode) {
        if (!TextUtils.isEmpty(response)) {
            Districts city = Utility.handleDistrictsResponse(response);
            for (int i =0; i < city.districts.get(0).subDistricts.size(); i++) {
                Districts.District countyDist = city.districts.get(0).subDistricts.get(i);
                County county = new County();
                county.setCountyName(countyDist.name);
                county.setAdcode(countyDist.adcode);
                county.setCityAdcode(cityAdcode);
                county.save();
            }
            return true;
        }
        return false;
    }

    public static String handleLocationResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONObject(response).getJSONArray("location");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Weather handleWeatherResponse(String response) {
        Weather weather = new Gson().fromJson(response, Weather.class);
        return weather;
    }
}
