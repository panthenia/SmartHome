package com.SmartHome.Util;

import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import com.SmartHome.DataType.EnviSensor;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.RequestInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by p on 14-4-23.
 *
 * 该类在新线程中启动网络访问操作，继承android提供的AsyncTask类
 * 在UI线程中创建该类的对象，然后调用继承的下来的excute方法来在新的一个线程
 * 中启动一个操作而不再UI线程中操作。
 *
 * 其中一般来说有三个方法需要重写 ：
 * onPreExecute（在启动新线程之前在UI线程中执行的操作，一般用来处理界面，可以不重写）
 * doInBackground（在新线程中执行的操作放在该函数中，一般耗时的操作放在该函数中，必须重写）
 * onPostExecute（在新线程中的操作执行完后，在UI线程中执行的操作）
 *
 *
 *
 */
public class ServiceRequest extends AsyncTask<RequestInfo, Void, String> {

    String result;
    boolean isobservable = false, rst_ok;
    MyObservable myobservable = null;
    String request_mode = null;

    String current_area, area_nm;
    String envi_type;

    public ServiceRequest(String rm) {
        super();
        request_mode = rm;
    }

    public ServiceRequest(String rm, String envi_type, String area_id,
                          String area_nm) {
        request_mode = rm;
        this.envi_type = envi_type;
        current_area = area_id;
        this.area_nm = area_nm;
    }

    public void addObserver(Observer ob) {
        myobservable.addObserver(ob);
    }

    public void setObserverble() {
        isobservable = true;
        myobservable = new MyObservable();
    }

    protected void onPostExecute(String rst) {
        Log.d("request-result:", rst);

    }

    private String usePost(RequestInfo rf) {
        URL url = rf.getUrl();
        rst_ok = true;
        Log.d("post-url=",url.toString());
        HttpURLConnection httpConnection = null;
        String rst = "", tl;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setConnectTimeout(100 * 1000);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestProperty("Content-Type",
                    "text/xml; charset=UTF-8");
            httpConnection.connect();

            DataOutputStream data = new DataOutputStream(
                    httpConnection.getOutputStream());

            data.writeBytes(rf.getXml());
            data.flush();
            data.close();
            Log.d("ServiceRequet-usePost", "POST:" + rf.getXml());

            InputStream rtstrm = httpConnection.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(
                    rtstrm));
            while ((tl = buff.readLine()) != null) {

                rst += tl;
            }
            buff.close();

            if (httpConnection.getResponseCode() == 200) {
                Log.d("ServiceRequet-usePost", "访问成功");
            } else {
                Log.d("ServiceRequet-usePost", "访问失败");
            }
            httpConnection.disconnect();
        } catch (IOException e) {
            rst_ok = false;
            Log.d("doInBackGround-post", "open connection error");
        } finally {
            httpConnection.disconnect();
        }
        return rst;
    }

    private String useGet(RequestInfo rf) {
        Log.d("servicerequest", "into useGet");
        URL url = rf.getUrl();
        rst_ok = true;
        HttpURLConnection httpConnection = null;
        String rst = "", tl;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(100 * 1000);
            httpConnection.setReadTimeout(100 * 1000);

            InputStream rtstrm = httpConnection.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(
                    rtstrm));
            while ((tl = buff.readLine()) != null) {
                rst += tl;
            }
            buff.close();

        } catch (IOException e) {
            rst_ok = false;
            Log.d("doInBackGround-get", "open connection error");
        } finally {
            httpConnection.disconnect();
        }
        return rst;
    }

    @Override
    protected String doInBackground(RequestInfo... info) {
        // TODO Auto-generated method stub
        String rst = "";
        for (int i = 0; i < info.length; ++i) {
            if (info[i].hasXml())
                rst = usePost(info[i]);
            else
                rst = useGet(info[i]);
        }
        Log.d("request_result:",rst);
        if (request_mode.contains("getDevice")) {
            PublicState.getInstance().saveDevieInfo(rst);
            Log.d("getdevice", rst);
        } else if (request_mode.contains("getenvi")) {
            Calendar calendar = Calendar.getInstance();
            String date = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)
                    +"-"+calendar.get(Calendar.DAY_OF_MONTH);
            String time = ""+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
            PublicState ps = PublicState.getInstance();
            ps.saveSensorInfo(//时间格式 2014-6-3 ,18:00
                    new EnviSensor(envi_type,date,time, area_nm, rst));
            if(envi_type.contains("Temperature")){
                for(int i=0;i<ps.room_list.size();++i){
                    if(ps.room_list.get(i).name.contains(area_nm))
                        ps.room_list.get(i).temrature = rst;
                }
                if(ps.selected_room.name.contains(area_nm))
                    ps.selected_room.temrature = rst;
            }
            Log.d("envitask-url=type="+envi_type+"result=",rst);
        } else if(request_mode.contains("control")) {
            Log.d("control:",rst);
        }else if(request_mode.contains("getrule")){
            PublicState.getInstance().saveRuleInfo(rst);
            Log.d("get-rule:",rst);
        }else if(request_mode.contains("getmode")){
            PublicState.getInstance().saveModeInfo(rst);
            Log.d("get-mode:",rst);
        }
        if (isobservable) {
            myobservable.setflag();
            myobservable.notifyObservers();
        }

        return rst;
    }

    class MyObservable extends Observable {
        public void setflag() {
            setChanged();
        }
    }

}