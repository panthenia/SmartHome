package com.SmartHome.DataType;

import android.util.Log;

/**
 * Created by p on 14-4-23.
 */

public class EnviSensor {
    public String envi_type,envi_time,area_name,envi_value;

    public EnviSensor(String tp,String tm,String area,String val){
        envi_type=tp;
        envi_time=tm;
        area_name=area;
        envi_value=val;
    }
    public void print(){
        Log.d("sensor", "area=" + area_name + "type=" + envi_type + "val=" + envi_value);
    }
}