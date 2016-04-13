package com.bird.shen.hotweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bird.shen.hotweather.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 制作一个欢迎界面
 */
public class WeatherWelcomeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(WeatherWelcomeActivity.this,ChooseAreaActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }

}
