package com.SmartHome.DataType;

import com.SmartHome.R;
import com.SmartHome.Util.ServiceRequest;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by p on 14-4-26.
 * 该类为设备类型类，记录了设备的基本信息和设备的状态
 * 设备类型有：
 *      lamp   air   light    water   television    curtain   player   camera           sensor
 *     普通灯  空调   调光灯    饮水机      电视         窗帘     播放器   摄像头          传感器
 *
 */
public class Device{

    public String name = "", room = "", type = "", id = "";
    public HashMap<String,String> status;
    public int icon;


    public Device() {

        status = new HashMap<String, String>();
    }

    public Device(String name, String room, String type, String id) {
        status = new HashMap<String, String>();

        this.name = name;
        this.id = id;
        this.type = type;
        this.room = room;
    }

    public int getIcon(){
        if(type.contains("lamp")){
            if(status.containsKey("power")){
                if(Boolean.valueOf(status.get("power")))
                    return R.drawable.btn_cop_lights_on;
                else return R.drawable.btn_cop_lights_off;
            }
            else return R.drawable.btn_cop_lights_off;
        }
        else if(type.contains("air"))
            return R.drawable.aircondition;
        else if(type.contains("light")){
            if(status.containsKey("power")){
                if(Boolean.valueOf(status.get("power")))
                    return R.drawable.btn_cop_lights_on;
                else return R.drawable.btn_cop_lights_off;
            }
            else return R.drawable.btn_cop_lights_off;
        }
        else if(type.contains("camera"))
            return R.drawable.security_camera;
        else if(type.contains("television"))
            return R.drawable.tv;
        else if(type.contains("curtain")){
            if(status.containsKey("power")){
                if(Boolean.valueOf(status.get("power")))
                    return R.drawable.motors_drapes_close;
                else return R.drawable.motors_drapes_open;
            }
            else return R.drawable.motors_drapes_open;
        }
        else if(type.contains("Temperature"))
            return R.drawable.temprature;
        else if(type.contains("Light"))
            return R.drawable.guangzhao;
        else if(type.contains("Humidity"))
            return R.drawable.shidu;
        else if(type.contains("player"))
            return R.drawable.player;
        return R.drawable.user;
    }
}
