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
 * Created by p on 14-5-7.
 */
public class PlayerActivity extends Activity {

    TextView current_text;
    String device_id="";
    PublicState ps = PublicState.getInstance();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.player_layout);
        Intent intent = getIntent();
        if(intent.hasExtra("device_id"))
            device_id = intent.getStringExtra("device_id");
        else finish();
        current_text = (TextView)findViewById(R.id.textView5);
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        textView.setText("播放器控制");
    }
    public void onPlayListClicked(View v){
        Intent intent = new Intent(this,PlayListActivity.class);
        this.startActivityForResult(intent,0);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(requestCode == 0 && resultCode == 0){
            if(intent.hasExtra("file_name")){
                String fname = intent.getStringExtra("file_name");
                current_text.setText(fname);
                String url = "http://" + ps.getNetAddress()
                        + "/wsnRest/control/" + device_id+"/3/"+ps.user_act+"/2434";
                Log.d("play-file-name=",fname.trim());
                ps.controlRequest(url,fname);
            }
            else current_text.setText("");
        }
        else current_text.setText("");
    }
    public void onPowerOnClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/1/"+ps.user_act+"/2434";
        String tv_id = ps.getIdByType("television");
        String tv_url = null;
        String tv_hdmi = null;
        if(tv_id != null) {
            tv_url = "http://" + ps.getNetAddress()
                    + "/wsnRest/control/" + tv_id + "/1/" + ps.user_act + "/2434";
            tv_hdmi = "http://" + ps.getNetAddress()
                    + "/wsnRest/control/" + tv_id + "/4/" + ps.user_act + "/2434";
        }
        Log.d("request-url", url);
        ps.controlRequest(url);
        if(tv_url != null){
            ps.controlRequest(tv_url);
            ps.controlRequest(tv_hdmi);
        }
    }
    public void onPowerOffClicked(View v){
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/2/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onPauseClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/5/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url);
    }
    public void onStartClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/6/"+ps.user_act+"/2434";
        Log.d("request-url",url);ps.controlRequest(url);
    }
    public void onStopClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/4/"+ps.user_act+"/2434";
        Log.d("request-url",url);ps.controlRequest(url);
    }

}