package com.SmartHome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.HashMap;

/**
 * Created by p on 14-5-6.
 */
public class TelevisionActivity extends Activity {


    String device_id="";
    TextView[] power_views = new TextView[2];
    TextView[] input_views = new TextView[4];
    PublicState ps = PublicState.getInstance();
    HashMap<Integer,Boolean> text_color_state = new HashMap<Integer, Boolean>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tv_layout);
        Intent intent = getIntent();
        if(intent.hasExtra("device_id"))
             device_id = intent.getStringExtra("device_id");
        else finish();
        ps.current_ui_content = this;
        ps.activitis.put(getClass().getName(),this);
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        power_views[0] = (TextView)findViewById(R.id.textView0);//kai
        power_views[1] = (TextView)findViewById(R.id.textView);//guan

        input_views[0] = (TextView)findViewById(R.id.textView6);
        input_views[1] = (TextView)findViewById(R.id.textView7);
        input_views[2] = (TextView)findViewById(R.id.textView12);
        input_views[3] = (TextView)findViewById(R.id.textView11);

        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                if(ps.status_list.get(i).getVar("TV_STATUS").contains("1")){
                    power_views[0].setTextColor(Color.rgb(0x6b, 0xc1, 0xf2));
                    power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
                }else{
                    power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
                }
                //1:dgital 2:hdmi 3:pc
                if(ps.status_list.get(i).getVar("TV_MODE").contains("1")){
                    input_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else if(ps.status_list.get(i).getVar("TV_MODE").contains("2")){
                    input_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else if(ps.status_list.get(i).getVar("TV_MODE").contains("3")){
                    input_views[3].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else{
                    input_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }
            }
        }


        textView.setText("      电视控制");
    }
    public void changeTextColor(int id,String type){
        TextView tv = null;
        if(text_color_state.containsKey(id)){//如果对应的textview已经有了该id的key，说明已经点击过
            if(text_color_state.get(id)){
                tv = (TextView)findViewById(id);
                tv.setTextColor(Color.rgb(0x48,0x6a,0x00));
                text_color_state.put(id,false);
                setStatus(type,"open");
            }else{
                tv = (TextView)findViewById(id);
                tv.setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                text_color_state.put(id,true);
                setStatus(type,"close");
            }
        }else{
            tv = (TextView)findViewById(id);
            tv.setTextColor(Color.rgb(0x6b,0xc1,0xf2));
            text_color_state.put(id,true);
            setStatus(type,"open");
        }

    }
    public void onMuteClicked(View v){
        changeTextColor(v.getId(),"mute");

    }
    public void onRoundSoundClicked(View v){
        changeTextColor(v.getId(),"rsound");
    }
    public void onThreeDimensionClicked(View v){
        changeTextColor(v.getId(),"3d");
    }
    public void onPowerOnClicked(View v){
        power_views[0].setTextColor(Color.rgb(0x6b, 0xc1, 0xf2));
        power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/1/"+ps.user_act+"/2434";
        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                ps.status_list.get(i).setVar("TV_STATUS","1");
            }
        }
        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void onPowerOffClicked(View v){
        power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/2/"+ps.user_act+"/2434";
        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                ps.status_list.get(i).setVar("TV_STATUS","0");
            }
        }
        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void setStatus(String name,String val){
        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                ps.status_list.get(i).setVar(name,val);
            }
        }
    }
    public void onDigitalClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i){
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));

        }
        setStatus("TV_MODE","1");
        input_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";

        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void onAnalogClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i) {
            input_views[i].setTextColor(Color.rgb(0x48, 0x6a, 0x00));

        }
        setStatus("TV_MODE","4");
        input_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        //String url = "http://" +ps.getNetAddress()
               // + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
        //Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onHdmiClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i){
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));

        }
        input_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("TV_MODE","2");
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/4/"+ps.user_act+"/2434";

        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void onPcClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i){
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        }
        setStatus("TV_MODE","3");
        input_views[3].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[3].setTextColor(Color.rgb(0x6b,0xc1,0xf2));

        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/5/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url,this);
    }
}