package com.SmartHome.Util;

import android.util.Log;
import com.SmartHome.DataType.Area;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.RequestInfo;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TimerTask;

public class EnvironmentTask extends TimerTask{
	PublicState cs;
	String[] envi_type={"Temperature","Humidity","Light"};
	public EnvironmentTask(PublicState cs){
		this.cs=cs;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ArrayList<Area> area_list= cs.getRoomList();
		String url_pre="http://"+cs.getNetAddress()+"/wsnRest/envData/";

		for(int i=0;i<area_list.size();++i){
			for(int j=0;j<envi_type.length;++j){
				String url=url_pre+cs.user_act+"/"+cs.getMd5();//+area_list.get(i).id+"/"+envi_type[j]+
                String decode_data = null;
                try {
                    decode_data = cs.securityDemo.getEncodeData(area_list.get(i).id+"|"+envi_type[j]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("envitask-url=", url);
				RequestInfo rf;
				try {
					rf = new RequestInfo(url,decode_data);
					ServiceRequest sr=new ServiceRequest("getenvi",envi_type[j]
							,area_list.get(i).id,area_list.get(i).name);
					sr.execute(rf);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}