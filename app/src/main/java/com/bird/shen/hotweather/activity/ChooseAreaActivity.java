package com.bird.shen.hotweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bird.shen.hotweather.R;
import com.bird.shen.hotweather.db.HotWeatherDB;
import com.bird.shen.hotweather.model.City;
import com.bird.shen.hotweather.model.Country;
import com.bird.shen.hotweather.model.Province;
import com.bird.shen.hotweather.util.HttpCallbackListener;
import com.bird.shen.hotweather.util.HttpUtil;
import com.bird.shen.hotweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Create a class to choose the Area.
 */
public class ChooseAreaActivity extends Activity {

          public static final int LEVEL_PROVINCE = 0;
          public static final int LEVEL_CITY = 1;
          public static final int LEVEL_COUNTRY = 2;

          // defined variables

          private ProgressDialog progressDialog;
          private TextView titleText;
          private ListView listView;
          private ArrayAdapter<String> adapter;
          private HotWeatherDB hotWeatherDB;
          private List<String> dataList = new ArrayList<String>();

          // the list of province;

          private List<Province> provinceList;

          // the list of city;

          private List<City> cityList;

          // the list of country

          private List<Country> countryList;

          // selected province

          private Province selectedProvince;

          // selected city

          private City selectedCity;

          // the Level of select

          private int currentLevel;


          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);

                    setContentView(R.layout.choose_area);

                    listView = (ListView) findViewById(R.id.list_view);
                    titleText = (TextView) findViewById(R.id.title_text);

                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
                    listView.setAdapter(adapter);

                    hotWeatherDB = HotWeatherDB.getInstance(this);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        if (currentLevel == LEVEL_PROVINCE) {

                                                  selectedProvince = provinceList.get(position);
                                                  queryCities();


                                        } else if (currentLevel == LEVEL_CITY) {

                                                  selectedCity = cityList.get(position);
                                                  queryCounties();
                                        }
                              }
                    });

                    // load data of procince;

                    queryProvinces();

          }

          // query the provinces from database ,if don`t have,  query on internet.

          private void queryProvinces() {

                    provinceList = hotWeatherDB.loadProvinces();

                    if (provinceList.size() > 0) {
                              dataList.clear();

                              for (Province province : provinceList) {

                                        dataList.add(province.getProvinceName());

                              }

                              adapter.notifyDataSetChanged();
                              listView.setSelection(0);
                              titleText.setText("中国");

                              currentLevel = LEVEL_PROVINCE;

                    } else {
                              // query on internet.

                             queryFromServer(null, "province");

                    }


          }

          // query the city from database ,if don`t have,  query on internet.

          private void queryCities() {

                    cityList = hotWeatherDB.loadcities(selectedProvince.getId());

                    if (cityList.size() > 0) {

                              dataList.clear();

                              for (City city : cityList) {
                                        dataList.add(city.getCityName());

                              }

                              adapter.notifyDataSetChanged();
                              listView.setSelection(0);
                              titleText.setText(selectedProvince.getProvinceName());

                              currentLevel = LEVEL_CITY;
                    } else {

                              // query on internet.

                            queryFromServer(selectedProvince.getProvincecode(),"city");

                              Log.d("ccccc", selectedProvince.getProvincecode());
                    }

          }

          // query the country from database ,if don`t have,  query on internet.

          private void queryCounties() {

                    countryList = hotWeatherDB.loadCountries(selectedCity.getId());

                    if (countryList.size() > 0) {
                              dataList.clear();

                              for (Country country : countryList) {

                                        dataList.add(country.getCountryName());

                              }

                              adapter.notifyDataSetChanged();
                              listView.setSelection(0);
                              titleText.setText(selectedCity.getCityName());
                              currentLevel = LEVEL_COUNTRY;

                    } else {

                              // query on internet.

                           queryFromServer(selectedCity.getCityCode(),"country");

                    }
          }

          //  According to the code and type of the incoming query data from the server

          private void queryFromServer(final String code, final String type) {

                    String address;

                    if (!TextUtils.isEmpty(code)) {

                              address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";

                    } else {

                              address = "http://www.weather.com.cn/data/list3/city.xml";
                    }

                    // show speed of progress

                    showProgressDialog();

                    HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

                              @Override
                              public void onFinish(String response) {

                               boolean result = false;

                                        if ("province".equals(type)){

                                                  result = Utility.handlePrivinceResponse(hotWeatherDB,response);

                                        }else if ("city".equals(type)){

                                                  Log.d("jjjjj",response +"   "+ selectedProvince.getId());

                                                  result = Utility.handleCityResponse(hotWeatherDB,response,selectedProvince.getId());

                                        }else if ("country".equals(type)){

                                                  result = Utility.handleCountryResponse(hotWeatherDB,response,selectedCity.getId());

                                        }

                                        if (result){
                                                  // 通过 runOnUiThread（）方法回到主线程处理逻辑

                                                  runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                      // 关闭进度对话框
                                                                      closeProgressDialog();

                                                                      if ("province".equals(type)){

                                                                            queryProvinces();
                                                                      }else  if ("city".equals(type)){

                                                                                queryCities();
                                                                      }else if ("country".equals(type)){

                                                                                queryCounties();
                                                                      }
                                                            }
                                                  });
                                        }
                              }

                              @Override
                              public void onError(Exception e) {

                                        // 通过 runOnUiThread（）方法回到主线程处理逻辑

                                        runOnUiThread(new Runnable() {
                                                  @Override
                                                  public void run() {

                                                     // 关闭进度对话框

                                                            closeProgressDialog();

                                                            Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                                                  }
                                        });


                              }
                    });

          }

          // 显示进度对话框

          private  void showProgressDialog(){

                    if (progressDialog == null){

                              progressDialog = new ProgressDialog(this);
                              progressDialog.setMessage("正在加载...");
                              progressDialog.setCanceledOnTouchOutside(false); // 设置对话框不可点击取消

                    }

                    progressDialog.show();

          }

          // 关闭进度条

          private  void closeProgressDialog(){

                    if (progressDialog != null){

                              progressDialog.dismiss();
                    }

          }

          // 捕获 back 按键，根据当前的级别来判断，此时应该返回市列表、省列表，还是退出

          @Override
          public void onBackPressed() {

                    if (currentLevel == LEVEL_COUNTRY){

                              queryCities();

                    }else if (currentLevel == LEVEL_CITY){

                              queryProvinces();
                    }else {

                              finish();
                    }
          }
}




























