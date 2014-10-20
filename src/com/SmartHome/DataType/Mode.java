package com.SmartHome.DataType;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by p on 14-5-22.
 * 场景类
 */
public class Mode {
    public String name,id,descp, status;
    public ArrayList<String> rules = new ArrayList<String>();
    public void printInfo(){
        Log.d("Mode_info:","Mode-name:"+name);
        Log.d("Mode_info:","Mode-id:"+id);
        for (String rule : rules) Log.d("Mode_info:", "mode-ruleid:" + rule);
    }
}
