package com.bird.shen.hotweather.model;

/**
 * POJO class : the entity class about Province
 */
public class Province {

          private  int  id ;

          private  String  provinceName ;

          private  String  provincecode ;

          public int getId() {
                    return id;
          }

          public void setId(int id) {
                    this.id = id;
          }

          public String getProvinceName() {
                    return provinceName;
          }

          public void setProvinceName(String provinceName) {
                    this.provinceName = provinceName;
          }

          public String getProvincecode() {
                    return provincecode;
          }

          public void setProvincecode(String provincecode) {
                    this.provincecode = provincecode;
          }
}
