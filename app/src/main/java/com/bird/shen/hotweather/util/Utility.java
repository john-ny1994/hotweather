package com.bird.shen.hotweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.bird.shen.hotweather.db.HotWeatherDB;
import com.bird.shen.hotweather.model.City;
import com.bird.shen.hotweather.model.Country;
import com.bird.shen.hotweather.model.Province;

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

}
