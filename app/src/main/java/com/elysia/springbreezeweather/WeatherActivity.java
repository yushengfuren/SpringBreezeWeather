package com.elysia.springbreezeweather;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.elysia.springbreezeweather.gson.weather.Aqi;
import com.elysia.springbreezeweather.gson.weather.Forecasts;
import com.elysia.springbreezeweather.gson.weather.NowWeather;
import com.elysia.springbreezeweather.gson.weather.Suggestions;
import com.elysia.springbreezeweather.gson.weather.Weather;
import com.elysia.springbreezeweather.service.ForecastAdapter;
import com.elysia.springbreezeweather.util.HttpUtil;
import com.elysia.springbreezeweather.util.StringUtil;
import com.elysia.springbreezeweather.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public Button navButton;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private ImageView nowIcon;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private String countyName;

    private ListView listView;
    private ForecastAdapter adapter;
    private List<Forecasts.Forecast> dataList = new ArrayList<>();


    private Forecasts forecasts;
    private Aqi classAqi;
    private Forecasts classForecasts;
    private NowWeather classNowWeather;
    private Suggestions classSuggestions;
    private Weather classWeather;

    public SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        classWeather = new Weather();
        classWeather.suggestions = new Suggestions();
        classWeather.aqi = new Aqi();
        classWeather.nowWeather = new NowWeather();
        classWeather.forecasts = new Forecasts();

        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        nowIcon = (ImageView) findViewById(R.id.now_icon);

        listView = (ListView) findViewById(R.id.forecast_layout_listview);
        adapter = new ForecastAdapter(WeatherActivity.this, R.layout.forecast_item, dataList);
        listView.setAdapter(adapter);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = pref.getString("weather", null);
        final String weatherId;

        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            countyName = weather.name;
            weatherId = weather.adcode;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            countyName = getIntent().getStringExtra("county_name");
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecasts.Forecast selectedForecast = dataList.get(position);
                showForecastDialog(selectedForecast);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    private void showWeatherInfo(Weather weather) {

        String cityName = weather.name;
        String updateTime = StringUtil.extractTime(weather.nowWeather.now_weather.time);
        String degree = weather.nowWeather.now_weather.temperature + "℃";
        String weatherInfo = weather.nowWeather.now_weather.text;
        String weatherIcon = weather.nowWeather.now_weather.icon;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        int resId = getResources().getIdentifier("w" + weatherIcon, "drawable", getPackageName());
        nowIcon.setImageResource(resId);

        forecasts = weather.forecasts;
        dataList.clear();
        dataList.addAll(forecasts.forecasts);
        adapter.notifyDataSetChanged();
        listView.setSelection(0);

        aqiText.setText(weather.aqi.now_aqi.aqi);
        pm25Text.setText(weather.aqi.now_aqi.pm2p5);

        String comfort = "舒适度：" + weather.suggestions.suggestions.get(2).category + "。\n" + weather.suggestions.suggestions.get(2).text;
        String carWash = "洗车指数：" + weather.suggestions.suggestions.get(1).category + "。\n" + weather.suggestions.suggestions.get(1).text;
        String sport = "运动建议：" + weather.suggestions.suggestions.get(0).category + "。\n" + weather.suggestions.suggestions.get(0).text;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

         weatherLayout.setVisibility(View.VISIBLE);

    }

    public void requestWeather(final String weatherId) {
        CountDownLatch latch = new CountDownLatch(4);
        String nowWeatherUrl = "https://devapi.qweather.com/v7/weather/now?location=" + weatherId + "&key=a500f59504f440e1af60bcdbc1c0a780";
        String forecastUrl = "https://devapi.qweather.com/v7/weather/7d?location=" + weatherId + "&key=a500f59504f440e1af60bcdbc1c0a780";
        String airDegreeUrl = "https://devapi.qweather.com/v7/air/now?location=" + weatherId + "&key=a500f59504f440e1af60bcdbc1c0a780";
        String suggestionUrl = "https://devapi.qweather.com/v7/indices/1d?type=1,2,8&location=" + weatherId + "&key=a500f59504f440e1af60bcdbc1c0a780";

        HttpUtil.sendOkHttpRequest(nowWeatherUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                classNowWeather = Utility.handleNowWeatherResponse(responseText);
                if (classNowWeather == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        HttpUtil.sendOkHttpRequest(forecastUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取预报失败", Toast.LENGTH_SHORT).show();
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                classForecasts = Utility.handleForecastsResponse(responseText);
                if (classForecasts == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取预报失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                latch.countDown();
            }
        });

        HttpUtil.sendOkHttpRequest(airDegreeUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取空气质量失败", Toast.LENGTH_SHORT).show();
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                classAqi = Utility.handleAqiResponse(responseText);
                if (classAqi == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取空气质量失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                latch.countDown();
            }
        });

        HttpUtil.sendOkHttpRequest(suggestionUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取建议失败", Toast.LENGTH_SHORT).show();
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseText = response.body().string();
                classSuggestions = Utility.handleSuggestionsResponse(responseText);
                if (classSuggestions == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "获取建议失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                latch.countDown();;
            }
        });

        try {
            latch.await();
            swipeRefreshLayout.setRefreshing(false);

            classWeather.adcode = weatherId;
            classWeather.suggestions = classSuggestions;
            classWeather.aqi = classAqi;
            classWeather.nowWeather = classNowWeather;
            classWeather.forecasts = classForecasts;
            classWeather.name = countyName;

            String toJson = new Gson().toJson(classWeather);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            editor.putString("weather", toJson);
            editor.apply();
//            editor.clear();
//            editor.apply();

            showWeatherInfo(classWeather);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void showForecastDialog(Forecasts.Forecast forecast) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        builder.setTitle("Forecast Details");

        String message = "日期: " + forecast.date + "\n" +
                "最高温度: " + forecast.tempMax + "\n" +
                "最低温度: " + forecast.tempMin + "\n" +
                "白天天气: " + forecast.textDay + "\n" +
                "夜晚天气: " + forecast.textNight;

        builder.setMessage(message);

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}