package com.bird.shen.hotweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Create a class to get some information from sevice
 */
public class HttpUtil {

          public static void sendHttpRequest(final String adress, final HttpCallbackListener listener) {

                    new Thread(new Runnable() {
                              @Override
                              public void run() {

                                        HttpURLConnection connection = null;

                                        try {

                                                  URL url = new URL(adress);

                                                  connection = (HttpURLConnection) url.openConnection();
                                                  connection.setRequestMethod("GET");
                                                  connection.setConnectTimeout(8000);
                                                  connection.setReadTimeout(8000);

                                                  InputStream in = connection.getInputStream();
                                                  BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                                                  StringBuilder response = new StringBuilder();
                                                  String line;

                                                  while ((line = reader.readLine()) != null) {

                                                            response.append(line);
                                                  }

                                                  if (connection != null) {

                                                            listener.onFinish(response.toString());

                                                  }

                                        } catch (Exception e) {

                                                  listener.onError(e);

                                        } finally {

                                                  if (connection != null) {

                                                            connection.disconnect();
                                                  }

                                        }

                              }
                    }).start();

          }

}
