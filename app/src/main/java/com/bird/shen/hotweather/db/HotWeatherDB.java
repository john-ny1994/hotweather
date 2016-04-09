package com.bird.shen.hotweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bird.shen.hotweather.model.City;
import com.bird.shen.hotweather.model.Country;
import com.bird.shen.hotweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Create a database ,name is HotWeatherDB
 */
public class HotWeatherDB {

          // define a name of database

          public   static  final  String DB_NAME = "hot_weather";

          // the version of database

          public   static  final  int  VERSION = 1 ;

          private  static  HotWeatherDB  hotWeatherDB ;

          private SQLiteDatabase  db;

          // Privatization construction method

          private HotWeatherDB(Context context) {

                    HotWeatherOpenHelper dbHelper = new HotWeatherOpenHelper(context,DB_NAME,null,VERSION);

                    db = dbHelper.getWritableDatabase();  // Write tables to database.

          }

          // get HotWeatherDB instance

          public synchronized  static  HotWeatherDB  getInstance(Context context){

                    if (hotWeatherDB == null){

                              hotWeatherDB = new HotWeatherDB(context);

                    }

                    return hotWeatherDB;

          }

           //  make the Province instance deposit into HotWeatherDB

          public void saveProvince(Province province){

                    if (province != null){

                              ContentValues  values = new ContentValues();

                              values.put("province_name",province.getProvinceName());
                              values.put("province_code",province.getProvincecode());

                              db.insert("Province",null,values); // the firse parame is table`s name
                    }

          }

     // read the Province information from the database

          public List<Province> loadProvinces(){

                    List<Province> list = new ArrayList<Province>();

                    Cursor cursor = db.query("Province",null,null,null,null,null,null); //the database`s query sentence has 7 parames ,first is table`s name

                    if (cursor.moveToFirst()){

                              do {
                                        Province province = new Province();
                                        province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                        province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                                        province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
                                        list.add(province);

                              }while (cursor.moveToNext());

                    }

                    if (cursor != null){

                              cursor.close();
                    }

                    return list;

          }

          // make the City instance deposit into datebase

          public  void  saveCities(City city){

                    ContentValues values = new ContentValues();

                    values.put("city_name",city.getCityName());
                    values.put("city_code",city.getCityCode());
                    values.put("province_id",city.getProvinceId());

                    db.insert("City",null,values);

          }

          // read the Province information from the database


          public  List<City> loadcities(int provinceId){

                    List<City> list = new ArrayList<City>();

                    Cursor cursor = db.query("City",null,"province_id = ?",new String[]{String.valueOf(provinceId)},null,null,null);

                    // the first is table`s name , the third is lookup culumn , the Fourth is values of culumns

                    if (cursor.moveToFirst()){

                              do {

                                        City city = new City();

                                        city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                        city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                                        city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                                        city.setProvinceId(provinceId);

                                        list.add(city);

                              }while (cursor.moveToNext());

                    }

                    if (cursor != null){

                              cursor.close();
                    }

                    return  list;
          }


          // make the country instance deposit into database

           public  void  saveCountry(Country country){


                     ContentValues values = new ContentValues();

                     values.put("country_name",country.getCountryName());
                     values.put("country_code",country.getCountryCode());
                     values.put("city_id",country.getCityId());

                     db.insert("Country",null,values);
           }

          // read the Country information from the database


          public  List<Country> loadCountries(int cityId){

                    List<Country> list = new ArrayList<Country>();

                    Cursor cursor = db.query("Country",null,"city_id = ?",new String[]{String.valueOf(cityId)},null,null,null);

                    if (cursor.moveToFirst()){

                              do {

                                        Country country = new Country();

                                        country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                        country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                                        country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                                        country.setCityId(cityId);

                                        list.add(country);
                              }while (cursor.moveToNext());

                    }

                    if (cursor != null){

                              cursor.close();
                    }

                    return  list;
          }

}
