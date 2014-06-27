package com.SmartHome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

/**
 * Created by p on 14-5-6.
 */
public class TelevisionActivity extends Activity {


    String device_id="";
    PublicState ps = PublicState.getInstance();

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
        textView.setText("      电视控制");
    }
    public void onPowerOnClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/1/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onPowerOffClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/2/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onDigitalClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onAnalogClicked(View v){
        //String url = "http://" +ps.getNetAddress()
               // + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
        //Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onHdmiClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/4/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onPcClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/5/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
}