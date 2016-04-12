package com.bird.shen.hotweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bird.shen.hotweather.service.AutoUpdateService;

/**
 * 创建一个自动更新的广播发射器。
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
