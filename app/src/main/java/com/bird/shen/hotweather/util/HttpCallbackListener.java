package com.bird.shen.hotweather.util;

/**
 * Create a interface .
 */
public interface HttpCallbackListener {

          void  onFinish (String response);

          void  onError ( Exception e );

}
