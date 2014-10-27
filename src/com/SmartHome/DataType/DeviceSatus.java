package com.SmartHome.DataType;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by p on 2014/9/1.
 * 设备状态类
 */
public class DeviceSatus {
    public String device_id;
    Map<String,String> status = null;
    public DeviceSatus(String device_id){
        this.device_id = device_id;
        status = new HashMap<String,String>();
    }
    public void addVar(String vname,String vvar){
        status.put(vname,vvar);
    }
    public String getVar(String vname){

                if(status.get(vname) == null)
                    return "";
                else return status.get(vname);

    }
    public void setVar(String name,String val){
        status.put(name,val);
    }
    public void printInfo(){
        for (Object o : status.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            Object val = entry.getValue();
            Log.d("key is:",key.toString());
            Log.d("value is:",val.toString());
        }
    }
}
