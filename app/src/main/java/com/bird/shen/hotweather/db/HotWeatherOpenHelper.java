package com.bird.shen.hotweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bird.shen.hotweather.model.City;

/**
 *  Create a class extend SQLiteOpenHelper
 */
public  class HotWeatherOpenHelper extends SQLiteOpenHelper{


   //  Create a table to store Province ;

          public static  final  String  CREATE_PROVINCE =  "create table Province ("
                    + "id integer primary key autoincrement, "
                    + "province_name text, "
                    + "province_code text)";


          // Create a table to store City ;

          public static final  String CREATE_CITY = "create table City ("
                    + "id integer primary key autoincrement, "
                    + "city_name text, "
                    + "city_code text, "
                    + "province_id integer)";


          // Create a table to store Country ;

          public  static  final  String  CREATE_COUNTRY = " create table Country ("
                    + "id integer primary key autoincrement, "
                    + "country_name text, "
                    + "country_code text, "
                    + "city_id integer)";

          public HotWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
                    super(context, name, factory, version);
          }

          @Override
          public void onCreate(SQLiteDatabase db) {

                    db.execSQL(CREATE_PROVINCE);   // Create Province table
                    db.execSQL(CREATE_CITY);       // Create City table
                    db.execSQL(CREATE_COUNTRY);    // Create country table
          }

          @Override
          public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

          }
}
