package com.bird.shen.hotweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bird.shen.hotweather.R;
import com.bird.shen.hotweather.service.AutoUpdateService;
import com.bird.shen.hotweather.util.HttpCallbackListener;
import com.bird.shen.hotweather.util.HttpUtil;
import com.bird.shen.hotweather.util.Utility;

/**
 *  Create a class to get the info of weather
 */
public class WeatherActivity extends Activity implements View.OnClickListener{

         // 这是布局文件layout/weather_layout.xml 里面的一个子布局
          private LinearLayout weatherInfoLayout;

          // 用于显示城市名
          private TextView cityNameText;

         // 显示发布时间
         private  TextView publishText;

        // 显示天气描述信息
         private TextView weatherDesText;

       // 显示气温1
         private TextView temp1Text;

      // 显示气温2
         private TextView temp2Text;

     //  显示当前日期
         private TextView currentDateText;

     // 定义按钮用来切换城市。
         private Button switchCity;

    // 更新天气按钮
         private  Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        // 初始化控件
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);

        cityNameText = (TextView) findViewById(R.id.city_text);
        publishText = (TextView) findViewById(R.id.publish_time_text);
        weatherDesText = (TextView) findViewById(R.id.weather_describe);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);

        String countryCode = getIntent().getStringExtra("country_code");

        if (!TextUtils.isEmpty(countryCode)){

            // 有县级代码情况下就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);

            // 查询天气码
            queryWeatherCode(countryCode);

            Log.d("countycode", countryCode);

        }else {

            // 没有县级代号就显示本地天气
             showWeather();

        }

        switchCity = (Button) findViewById(R.id.switch_city);
        refreshWeather = (Button) findViewById(R.id.refresh_weather);

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);

    }

    // 点击事件逻辑部分

     @Override
     public void onClick(View v) {

        switch (v.getId()){

            case R.id.switch_city:

                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                Log.d("from_weather_activity", " " + intent.getBooleanExtra("from_weather_activity", true));
                startActivity(intent);
                finish();
                break;

            case R.id.refresh_weather:

                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                if (!TextUtils.isEmpty(weatherCode)){

                    queryWeatherInfo(weatherCode);
                }
                break;

            default:

                break;
        }

    }

    // 查询县级代号所对应的天气代号

    private void queryWeatherCode(String countryCode){

        String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";

        // 从服务器中获取信息
      queryFromServer(address,"countryCode");

    }

    // 查询天气代号所对应的天气

    private void queryWeatherInfo(String weatherCode){

        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";

        Log.d("address1",address);

        // 从服务器中获取信息。
        queryFromServer(address, "weatherCode");
    }

    // 从传入的地址和类型向服务器查询天气代号或者天气信息。

    private void queryFromServer(final String address,final String type){

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {

                if ("countryCode".equals(type)){

                    if (!TextUtils.isEmpty(response)) {

                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");

                        if (array != null && array.length == 2) {  // 不太理解这边为什么要做这个判断?

                            String weatherCode = array[1];

                            Log.d("weathercode",weatherCode);

                            // 利用获得的天气代号，调用查看天气信息。

                            queryWeatherInfo(weatherCode);

                        }
                    }
            }else  if ("weatherCode".equals(type)){

                // 处理服务器返回来的天气信息

                    Utility.handleWeatherResponse(WeatherActivity.this,response);

                   // 调用 runUiThread 方法 ，切回住线程，进行UI操作

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // 显示天气信息
                             showWeather();
                        }
                    });
                }
            }

            // 如果有异常的情况下， 切回主线程，返回显示“同步失败”
            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        publishText.setText("同步失败");

                    }
                });
            }
        });
    }

    // 从SharedPreferences文件中读取存储的天气信息，并且显示在界面上。

    private void showWeather(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        cityNameText.setText(prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp2", ""));
        temp2Text.setText(prefs.getString("temp1", ""));
        weatherDesText.setText(prefs.getString("weather_descripe", ""));
        publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));

        weatherInfoLayout.setVisibility(View.VISIBLE); // 将显示天气信息的这一块布局显示可见
        cityNameText.setVisibility(View.VISIBLE);

        // 在天气刷新的位置，启动服务，然后后台自动更新。

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

    }
}
