package com.bird.shen.hotweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.bird.shen.hotweather.db.HotWeatherDB;
import com.bird.shen.hotweather.model.City;
import com.bird.shen.hotweather.model.Country;
import com.bird.shen.hotweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create a tool class to  process the data return by network
 */
public class Utility {


          // Analtical network access to the provice of information;

          public synchronized static boolean handlePrivinceResponse(HotWeatherDB hotWeatherDB, String response) {

                    if (!TextUtils.isEmpty(response)) {

                              String[] allProvince = response.split(",");

                              if (allProvince != null && allProvince.length > 0) {

                                        for (String P : allProvince) {

                                                  String[] arrays = P.split("\\|");

                                                  Province province = new Province();

                                                  province.setProvincecode(arrays[0]);
                                                  province.setProvinceName(arrays[1]);

                                                  hotWeatherDB.saveProvince(province);

                                        }

                                        return true;

                              }
                    }

                    return false;

          }


          // Analtical network access to the city of information;

          public static boolean handleCityResponse(HotWeatherDB hotWeatherDB, String response, int provinceId) {

                    if (!TextUtils.isEmpty(response)) {

                              String[] allCities = response.split(",");

                              if (allCities != null && allCities.length > 0) {

                                        for (String c : allCities) {

                                                  String[] arrays = c.split("\\|");

                                                  City city = new City();

                                                  city.setCityCode(arrays[0]);

                                                  Log.d("kkkk", arrays[0]);

                                                  city.setCityName(arrays[1]);

                                                  Log.d("bbbb",arrays[1]);
                                                  city.setProvinceId(provinceId);

                                                  hotWeatherDB.saveCities(city);

                                        }

                                        return true;

                              }
                    }

                    return false;

          }

          // Analtical network access to the country of information;

          public static boolean handleCountryResponse(HotWeatherDB hotWeatherDB, String response, int cityId) {


                    if (!TextUtils.isEmpty(response)) {

                              String[] allCountries = response.split(",");

                              if (allCountries != null && allCountries.length > 0) {

                                        for (String c : allCountries) {

                                                  String[] arrays = c.split("\\|");

                                                  Country country = new Country();

                                                  country.setCountryCode(arrays[0]);
                                                  country.setCountryName(arrays[1]);
                                                  country.setCityId(cityId);

                                                  hotWeatherDB.saveCountry(country);

                                        }

                                        return true;
                              }
                    }

                    return false;
          }

          // 解析服务器返回的JSON数据，并将解析的数据存储到本地

          public static void handleWeatherResponse(Context context,String response){

                    try {
                              JSONObject jsonObject = new JSONObject(response);

                              JSONObject  weatherinfo = jsonObject.getJSONObject("weatherinfo"); // 匹配键为weatherinfo的数据 它的类型为json数据

                              String cityName = weatherinfo.getString("city");

                             Log.d("城市名为：",cityName);

                              String weatherCode = weatherinfo.getString("cityid");

                        Log.d("城市名为：",weatherCode);

                              String temp1 = weatherinfo.getString("temp1");

                        Log.d("城市名为：",temp1);

                              String temp2 = weatherinfo.getString("temp2");

                        Log.d("城市名为：",temp2);

                              String weatherDescripe = weatherinfo.getString("weather");

                        Log.d("城市名为：",weatherDescripe);

                              String publishTime = weatherinfo.getString("ptime");

                        Log.d("城市名为：",publishTime);

                              // 调用saveWeatherInfo方法，将解析出来的数据写入文件

                              saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDescripe,publishTime);


                    } catch (JSONException e) {
                              e.printStackTrace();
                    }
          }

          // 将服务器返回的所有天气信息存储到SharedPreferences文件中

          public static void saveWeatherInfo(Context context,String cityname,String weatherCode,String temp1,String temp2,String weatherDescripe,String publishTime){

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                    editor.putBoolean("city_selected",true);
                    editor.putString("city_name", cityname);
                    editor.putString("weather_code", weatherCode);
                    editor.putString("temp1", temp1);
                    editor.putString("temp2", temp2);
                    editor.putString("weather_descripe", weatherDescripe);
                    editor.putString("publish_time",publishTime);
                    editor.putString("current_date",sdf.format(new Date()));
                    editor.commit();

          }

}
