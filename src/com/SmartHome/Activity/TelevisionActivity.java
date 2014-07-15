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

        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        power_views[0] = (TextView)findViewById(R.id.textView0);//kai
        power_views[1] = (TextView)findViewById(R.id.textView);//guan

        input_views[0] = (TextView)findViewById(R.id.textView6);
        input_views[1] = (TextView)findViewById(R.id.textView7);
        input_views[2] = (TextView)findViewById(R.id.textView12);
        input_views[3] = (TextView)findViewById(R.id.textView11);
        textView.setText("      电视控制");
    }
    public void changeTextColor(int id){
        TextView tv = null;
        if(text_color_state.containsKey(id)){//如果对应的textview已经有了该id的key，说明已经点击过
            if(text_color_state.get(id)){
                tv = (TextView)findViewById(id);
                tv.setTextColor(Color.rgb(0x48,0x6a,0x00));
                text_color_state.put(id,false);
            }else{
                tv = (TextView)findViewById(id);
                tv.setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                text_color_state.put(id,true);
            }
        }else{
            tv = (TextView)findViewById(id);
            tv.setTextColor(Color.rgb(0x6b,0xc1,0xf2));
            text_color_state.put(id,true);
        }

    }
    public void onMuteClicked(View v){
        changeTextColor(v.getId());
    }
    public void onRoundSoundClicked(View v){
        changeTextColor(v.getId());
    }
    public void onThreeDimensionClicked(View v){
        changeTextColor(v.getId());
    }
    public void onPowerOnClicked(View v){
        power_views[0].setTextColor(Color.rgb(0x6b, 0xc1, 0xf2));
        power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/1/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onPowerOffClicked(View v){
        power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/2/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onDigitalClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onAnalogClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        //String url = "http://" +ps.getNetAddress()
               // + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
        //Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onHdmiClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/4/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onPcClicked(View v){
        //changeTextColor(v.getId());
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[3].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        for(int i=0;i<4;++i)
            input_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        input_views[3].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/5/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
}