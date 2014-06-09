package com.SmartHome.DataType;

import android.util.Log;

/**
 * Created by p on 14-5-4.
 */
public class Area {
    public Area(){
        name="";
        id="";
    }
    public Area(String name,String id){
        this.name=name;
        this.id= id;
    }
    public String name,id;
    public void printInfo(){
        Log.d("Area_info:","area-name:"+name+" area-id:"+id);
    }
}
