package com.SmartHome.DataType;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.SmartHome.Adaptor.*;
import com.SmartHome.Cription.security.SecurityDemo;
import com.SmartHome.R;
import com.SmartHome.Util.DataUtil;
import com.SmartHome.Util.InfoParser;
import com.SmartHome.Util.ServiceRequest;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by p on 14-4-23.
 */
public class PublicState extends Application {


    public static PublicState publicStateself = null;


    public String net_ip="",net_port="";
    public String user_act = "",user_psw = "";
    public Area selected_room=null;
    public SecurityDemo securityDemo = null;
    public ArrayList<Area> room_list = null;
    public ArrayList<Rule> rule_list = null;
    public ArrayList<Mode> mode_list = null;
    public ArrayList<String> play_list = null;
    public ArrayList<DeviceSatus> status_list = null;
    public ArrayList<Device> device_list = null;
    public boolean login_result = false;

    public String device_info = "";
    public String rule_info = "";
    public String mode_info = "";
    public String status_info = "";
    public String md5 = "";
    public Context current_ui_content = null;

    public MediaAdaptor media_adp = null;
    public LightAdaptor light_adp = null;
    public SecureAdaptor secure_adp = null;
    public EnvironmentAdaptor envi_adp = null ;
    public ModeAdaptor mode_adp = null;
    public int current_adp = -1;
    public DataUtil du;
    public HashMap<String,Context> activitis = null;
    public MessageDigest md5_encriptor = null;
    public void onCreate(){

        publicStateself = this;
        du = new DataUtil(this, this.getString(R.string.database_name), null, 1);
        room_list = new ArrayList<Area>();
        device_list = new ArrayList<Device>();
        rule_list = new ArrayList<Rule>();
        mode_list = new ArrayList<Mode>();
        activitis = new HashMap<String, Context>();
        try {
            md5_encriptor = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            securityDemo = new SecurityDemo();
            securityDemo.getDESKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //此处为测试临时使用代码，room_list以后是通过解析获得
       // Mode mode = new Mode();
        //mode.name = "fuck";
       // mode.id = "123";
       // mode.rules.add("88");
       // mode.rules.add("91");
       // mode_list.add(mode);
        //room_list.add(new Area("902","null"));
        //
    }
    public static PublicState getInstance() {
        return publicStateself;
    }
    public void saveDevieInfo(String df){
        device_info = df;
    }
    public void saveRuleInfo(String df){
        rule_info = df.replace("&gt;",">");
        rule_info = rule_info.replace("&lt;","<");

        Log.d("rule_info:","after_change:"+rule_info);
    }
    public void saveModeInfo(String df){
        mode_info = df;
    }
    public void saveStatusInfo(String info){
        status_info = info;
    }
    public void updateUi(){
        switch (current_adp){
            case 1:
                if(media_adp != null){
                    media_adp.notifyDataSetChanged();
                }
                break;
            case 2:
                if(light_adp != null){
                    light_adp.notifyDataSetChanged();
                }
                break;
            case 3:
                if(secure_adp != null){
                    secure_adp.notifyDataSetChanged();
                }
                break;
            case 4:
                if(envi_adp != null){
                    envi_adp.notifyDataSetChanged();
                }
                break;
            case 5:
                if(mode_adp != null){
                    mode_adp.notifyDataSetChanged();
                }
                break;
            case -1:break;
        }
    }
    public void validMediaAdaptor(){
        current_adp = 1;
    }
    public void validLightAdaptor(){
        current_adp = 2;
    }
    public void validSecureAdaptor(){
        current_adp = 3;
    }
    public void validEnviAdaptor(){
        current_adp = 4;
    }
    public void validModeAdaptor(){
        current_adp = 5;
    }

    public void invalidCurrentAdaptor(){
        current_adp = -1;
    }
    public void handleStatusInfo(String info){
        status_info = info;
        try {
            InfoParser.parseStatusInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    public void handlePlayListInfo(String info){
        try {
            InfoParser.parsePlayListInfo(info);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    public String getNetAddress(){
        return net_ip+":"+net_port;
    }
    public Rule getRuleById(String id){
        for (Rule aRule_list : rule_list)
            if (aRule_list.id.equals(id))
                return aRule_list;
        return null;
    }
    public Device getDeviceById(String id){
        for (Device aDevice_list : device_list)
            if (aDevice_list.id.equals(id))
                return aDevice_list;
        return null;
    }
    public DeviceSatus getDeviceStatusById(String id){

        synchronized (status_list){
            for (DeviceSatus aStatus_list : status_list)
                if (aStatus_list.device_id.equals(id))
                    return aStatus_list;
            return null;
        }
    }
    public ArrayList<Area> getRoomList(){
        return room_list;
    }
    public void saveSensorInfo(EnviSensor ev){
        SQLiteDatabase db = du.getReadableDatabase();
        String sql;
        // area text,type text,time text,val text

        sql = "insert into sensor values('";
        sql += ev.area_name + "','" + ev.envi_type + "','" + ev.envi_time
                    + "','" + ev.envi_value + "','" + ev.envi_date+"')";
        db.execSQL(sql);
        db.close();
    }

    public void printRules(){
        for(int i=0;i<rule_list.size();++i){
            Log.d("rule_info:","print rule-"+i);
            rule_list.get(i).printInfo();
        }
    }
    public String getIdByType(String type){
        for (Device aDevice_list : device_list) {
            if (aDevice_list.type.contains(type))
                return aDevice_list.id;
        }

        return null;
    }
    public void printModes(){
        for (Mode aMode_list : mode_list) aMode_list.printInfo();
    }
    public void printAreas(){
        for (Area aRoom_list : room_list) {
            aRoom_list.printInfo();
        }
    }
    public ArrayList<Device> getDeviceByType(String... types ){//获取设备信息
        ArrayList<Device> dl = new ArrayList<Device>();
        HashSet<String> type_set = new HashSet<String>(Arrays.asList(types));
        Device dv;

        for (Device aDevice_list : device_list) {
            dv = aDevice_list;
            if (type_set.contains(dv.type))
                dl.add(dv);
        }

        return dl;
    }
    public void controlRequest(String url ,Context ctx){

        String[] split_url = url.split("/");
        split_url[split_url.length-1] = getMd5();
        if(split_url.length == 7 ){//不带参数的get-url
            RequestInfo rf= null;
            try {
                rf = new RequestInfo(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ServiceRequest sr=new ServiceRequest("control");
            sr.execute(rf);
        }else if(split_url.length == 9){//两个参数的url
            String new_url = "";
            String data = "";
            for(int i=0;i<5;++i){
                new_url += (split_url[i]+"/");
            }
            new_url += (split_url[7]+"/");
            new_url += split_url[8];

            data += (split_url[5]+"|"+split_url[6]);
            String edata = null;
            try {
                edata = securityDemo.getEncodeData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            controlRequest(new_url,edata,ctx);
        }else if(split_url.length == 10){//三个参数的url
            String new_url = "";
            String data = "";
            for(int i=0;i<5;++i){
                new_url += (split_url[i]+"/");
            }
            new_url += (split_url[8]+"/");
            new_url += split_url[9];

            data += (split_url[5]+"|"+split_url[6]+"|"+split_url[7]);
            String edata = null;
            try {
                edata = securityDemo.getEncodeData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            controlRequest(new_url,edata,ctx);
        }
    }
    public void controlRequest(String url,String data,Context ctx){
        RequestInfo rf= null;
        try {
            rf = new RequestInfo(url,data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr=new ServiceRequest("control",ctx);
        sr.execute(rf);
    }
    public String getMd5(){
        //Log.d("md5:",md5);
        return md5;
    }
    public void makeMd5(){
        String data = user_act+user_psw;
        md5_encriptor.reset();
        byte[] data_byte = null;
        data_byte = data.getBytes();

        byte[] hash_data = md5_encriptor.digest(data_byte);
        StringBuilder md5StrBuff = new StringBuilder();

        for (byte aHash_data : hash_data) {
            if (Integer.toHexString(0xFF & aHash_data).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & aHash_data));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & aHash_data));
        }

        md5 = md5StrBuff.toString();
    }
    public ArrayList<EnviSensor> getSensorInfo(String type,String date){
        SQLiteDatabase db = du.getReadableDatabase();
        ArrayList<EnviSensor> sensor_list = new ArrayList<EnviSensor>();

        Cursor rst = db.rawQuery("select * from sensor "+"where area = '"+selected_room.name+
                "' and type = '"+type+"' and date ='"+date+"'", null);
        int time_dx = rst.getColumnIndex("time");
        int val_dx = rst.getColumnIndex("val");
        int date_dx = rst.getColumnIndex("date");
        for (rst.moveToFirst(); !(rst.isAfterLast()); rst.moveToNext()) {
            // String tp,String tm,String area,String val
            EnviSensor sr = new EnviSensor(type,rst.getString(date_dx),rst.getString(time_dx),
                    selected_room.name,rst.getString(val_dx));
            //sr.print();
            sensor_list.add(sr);
        }
        rst.close();
        db.close();
        return sensor_list;
    }
}
