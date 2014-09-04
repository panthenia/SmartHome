package com.SmartHome.Util;

import android.util.Log;
import com.SmartHome.DataType.Area;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.RequestInfo;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by p on 2014/9/1.
 */
public class FreshStatusTask extends TimerTask {
    PublicState ps;
    public FreshStatusTask(PublicState cs){
        this.ps=cs;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub

        String getstate="http://"+ps.getNetAddress();
        getstate+="/wsnRest/getDeviceVar/"+ps.user_act+"/"+ps.getMd5();
        RequestInfo rf3= null;
        try {
            rf3 = new RequestInfo(getstate);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr3=new ServiceRequest("getstatus");
        sr3.execute(rf3);
    }

}