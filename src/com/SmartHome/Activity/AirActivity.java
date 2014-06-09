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
 * Created by p on 14-4-30.
 */
public class AirActivity extends Activity {

    PublicState ps = PublicState.getInstance();
    String device_id="";
    String base_url = "http://" +ps.getNetAddress()
            + "/wsnRest/control/";
    TextView setTem = null;
    int current_tem = 23;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        if(intent.hasExtra("device_id"))
            device_id = intent.getStringExtra("device_id");
        else finish();
        setContentView(R.layout.air_layout);

        setTem = (TextView)findViewById(R.id.textView2);
        setTem.setText(String.valueOf(current_tem)+"℃");

    }
    public void onPowerOnClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/1/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");
    }
    public void onPowerOffClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/2/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onTemAddClicked(View v){
        current_tem += 1;
        setTem.setText(String.valueOf(current_tem)+"℃");
        Log.d("Air-control:",base_url+device_id+"/1/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onTemMinusClicked(View v){
        current_tem -= 1;
        setTem.setText(String.valueOf(current_tem)+"℃");
        Log.d("Air-control:",base_url+device_id+"/1/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onHSpeedClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/23/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onMSpeedClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/22/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onSSpeedClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/24/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onLROpenClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/27/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onLRCloseClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/28/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onUDOpenClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/25/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onUDCloseClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/26/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onColdClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/21/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onHotClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/19/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
    public void onWetClicked(View v){
        Log.d("Air-control:",base_url+device_id+"/20/bupt/2434");
        ps.controlRequest(base_url+device_id+"/1/bupt/2434");

    }
}