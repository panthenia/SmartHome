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

/**
 * Created by p on 14-5-7.
 */
public class PlayerActivity extends Activity {

    TextView current_text;
    String device_id="";
    boolean mute_state = false;
    TextView[] power_views = new TextView[2];
    TextView mute_view = null;

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
        ps.activitis.put(getClass().getName(),this);
        init_ui();
    }
    public void init_ui()   {
        power_views[0] = (TextView)findViewById(R.id.textView0);
        power_views[1] = (TextView)findViewById(R.id.textView);
        current_text = (TextView)findViewById(R.id.textView5);
        mute_view = (TextView)findViewById(R.id.textView3);
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
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
                String fpath = intent.getStringExtra("file_path");
                current_text.setText(fname);
                String url = "http://" + ps.getNetAddress()
                        + "/wsnRest/control/"+ps.user_act+"/"+ps.getMd5();
                Log.d("play-file-name=",fpath.trim());
                if(fpath.length() > 0){
                    String data = device_id+"|3"+"|"+fpath;
                    String edata = null;
                    try {
                        edata = ps.securityDemo.getEncodeData(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ps.controlRequest(url,edata,this);
                }
            }
            else current_text.setText("");
        }
        else current_text.setText("");
    }
    public void onMuteClicked(View v){
        if(mute_state){
            mute_view.setTextColor(Color.rgb(0x6b,0xc1,0xf2));
            mute_state = false;
        }else{
            mute_view.setTextColor(Color.rgb(0x48,0x6a,0x00));
            mute_state = true;
        }
    }
    public void onPowerOnClicked(View v){
        power_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
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
        ps.controlRequest(url,this);
        if(tv_url != null){
            ps.controlRequest(tv_url,this);
            ps.controlRequest(tv_hdmi,this);
        }
    }
    public void onPowerOffClicked(View v){
        power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        String url = "http://" +ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/2/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void onPauseClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/5/"+ps.user_act+"/2434";
        Log.d("request-url", url);ps.controlRequest(url,this);
    }
    public void onStartClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/6/"+ps.user_act+"/2434";
        Log.d("request-url",url);ps.controlRequest(url,this);
    }
    public void onStopClicked(View v){
        String url = "http://" + ps.getNetAddress()
                + "/wsnRest/control/" + device_id+"/4/"+ps.user_act+"/2434";
        Log.d("request-url",url);ps.controlRequest(url,this);
    }

}