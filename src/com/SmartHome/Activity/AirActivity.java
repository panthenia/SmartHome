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
 * Created by p on 14-4-30.
 */
public class AirActivity extends Activity {

    PublicState ps = PublicState.getInstance();
    String device_id="";
    String base_url = "http://" +ps.getNetAddress()
            + "/wsnRest/control/";
    TextView setTem = null;
    TextView crtem = null;
    TextView[] power_views = new TextView[2];
    TextView[] mode_views = new TextView[3];
    TextView[] speed_views = new TextView[3];
    TextView[] updown_views = new TextView[2];
    TextView[] leftright_views = new TextView[2];

    int current_tem = 16;
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
        TextView textView = (TextView)findViewById(R.id.current_user);
        textView.setText("当前用户："+ps.user_act);
        ps.current_ui_content = this;
        ps.activitis.put(getClass().getName(),this);
        setTem = (TextView)findViewById(R.id.textView2);

        power_views[0] = (TextView)findViewById(R.id.t1);//kai
        power_views[1] = (TextView)findViewById(R.id.t2);//guan

        mode_views[0] = (TextView)findViewById(R.id.t21);
        mode_views[1] = (TextView)findViewById(R.id.t19);
        mode_views[2] = (TextView)findViewById(R.id.t20);

        speed_views[0] = (TextView)findViewById(R.id.t23);
        speed_views[1] = (TextView)findViewById(R.id.t22);
        speed_views[2] = (TextView)findViewById(R.id.t24);

        updown_views[0] = (TextView)findViewById(R.id.t25);
        updown_views[1] = (TextView)findViewById(R.id.t26);

        leftright_views[0] = (TextView)findViewById(R.id.t27);
        leftright_views[1] = (TextView)findViewById(R.id.t28);
        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                current_tem = Integer.parseInt(ps.status_list.get(i).getVar("AC_TEMPERATURE"));
                setTem.setText(String.valueOf(current_tem)+"℃");
                if(ps.status_list.get(i).getVar("AC_STATUS").contains("1")){
                    power_views[0].setTextColor(Color.rgb(0x6b, 0xc1, 0xf2));
                    power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
                }else{
                    power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
                }
                if(ps.status_list.get(i).getVar("AC_SPEED").contains("1")){
                    speed_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else if(ps.status_list.get(i).getVar("AC_SPEED").contains("2")){
                    speed_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else {
                    speed_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }

                //2:cold 3:hot 4:wet
                if(ps.status_list.get(i).getVar("AC_MODE").contains("2")){
                    mode_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else if(ps.status_list.get(i).getVar("AC_MODE").contains("3")){
                    mode_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else if(ps.status_list.get(i).getVar("AC_MODE").contains("4")){
                    mode_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }else{
                    mode_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                }

                //0：关 1：开

                if(ps.status_list.get(i).getVar("AC_ORIENT_LR").contains("0")){
                    leftright_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    leftright_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));

                }else if(ps.status_list.get(i).getVar("AC_ORIENT_LR").contains("1")){
                    leftright_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    leftright_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));

                }
                if(ps.status_list.get(i).getVar("AC_ORIENT_UD").contains("0")){
                    updown_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    updown_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));

                }else if(ps.status_list.get(i).getVar("AC_ORIENT_UD").contains("1")){
                    updown_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
                    updown_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));

                }

            }
        }
        crtem = (TextView)findViewById(R.id.textView5);
        crtem.setText(ps.selected_room.temrature+"℃");

    }
    public void setStatus(String name,String val){
        for(int i=0;i<ps.status_list.size();++i){
            if(ps.status_list.get(i).device_id.contains(device_id)){
                ps.status_list.get(i).setVar(name,val);
            }
        }
    }
    public void onPowerOnClicked(View v){
        power_views[0].setTextColor(Color.rgb(0x6b, 0xc1, 0xf2));
        power_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_STATUS","1");
        Log.d("Air-control:",base_url+device_id+"/1/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/1/"+ps.user_act+"/2434",this);
    }
    public void onPowerOffClicked(View v){
        power_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        power_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_STATUS","0");
        Log.d("Air-control:",base_url+device_id+"/2/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/2/"+ps.user_act+"/2434",this);

    }
    public void onTemAddClicked(View v){
        if(current_tem < 30)
            current_tem += 1;
        setTem.setText(String.valueOf(current_tem)+"℃");
        Log.d("Air-control:",base_url+device_id+"/1/"+ps.user_act+"/2434");
        setStatus("AC_TEMPERATURE",String.valueOf(current_tem));
        ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);

    }
    public void onTemMinusClicked(View v){
        if(current_tem > 16)
            current_tem -= 1;
        setTem.setText(String.valueOf(current_tem)+"℃");
        setStatus("AC_TEMPERATURE",String.valueOf(current_tem));
        Log.d("Air-control:",base_url+device_id+"/1/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);

    }
    public void onHSpeedClicked(View v){
        for(int i=0;i<3;++i)
            speed_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        speed_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_SPEED","3");
        Log.d("Air-control:",base_url+device_id+"/23/"+ps.user_act+"/2434");
        //ps.controlRequest(base_url+device_id+"/23/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);
    }
    public void onMSpeedClicked(View v){
        for(int i=0;i<3;++i)
            speed_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        speed_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_SPEED","2");
        Log.d("Air-control:",base_url+device_id+"/22/"+ps.user_act+"/2434");
        //ps.controlRequest(base_url+device_id+"/22/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);
    }
    public void onSSpeedClicked(View v){
        for(int i=0;i<3;++i)
            speed_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        speed_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_SPEED","1");
        Log.d("Air-control:",base_url+device_id+"/24/"+ps.user_act+"/2434");
        //ps.controlRequest(base_url+device_id+"/24/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);
    }
    public void onLROpenClicked(View v){
        leftright_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        leftright_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_ORIENT_LR","1");
        Log.d("Air-control:",base_url+device_id+"/27/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/27/"+ps.user_act+"/2434",this);

    }
    public void onLRCloseClicked(View v){
        leftright_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        leftright_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_ORIENT_LR","0");
        Log.d("Air-control:",base_url+device_id+"/28/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/28/"+ps.user_act+"/2434",this);

    }
    public void onUDOpenClicked(View v){
        updown_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        updown_views[1].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_ORIENT_UD","1");
        Log.d("Air-control:",base_url+device_id+"/25/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/25/"+ps.user_act+"/2434",this);

    }
    public void onUDCloseClicked(View v){
        updown_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        updown_views[0].setTextColor(Color.rgb(0x48,0x6a,0x00));
        setStatus("AC_ORIENT_UD","0");
        Log.d("Air-control:",base_url+device_id+"/26/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/26/"+ps.user_act+"/2434",this);

    }
    public void onColdClicked(View v){
        for(int i=0;i<3;++i)
            mode_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        mode_views[0].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_MODE","2");
        Log.d("Air-control:",base_url+device_id+"/21/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/21/"+ps.user_act+"/2434",this);
        //ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);
    }
    public void onHotClicked(View v){
        for(int i=0;i<3;++i)
            mode_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        mode_views[1].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_MODE","3");
        Log.d("Air-control:",base_url+device_id+"/19/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/19/"+ps.user_act+"/2434",this);
        //ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434",this);
    }
    public void onWetClicked(View v){
        for(int i=0;i<3;++i)
            mode_views[i].setTextColor(Color.rgb(0x48,0x6a,0x00));
        mode_views[2].setTextColor(Color.rgb(0x6b,0xc1,0xf2));
        setStatus("AC_MODE","4");
        Log.d("Air-control:",base_url+device_id+"/20/"+ps.user_act+"/2434");
        ps.controlRequest(base_url+device_id+"/20/"+ps.user_act+"/2434",this);
        //ps.controlRequest(base_url+device_id+"/"+(current_tem-13)+"/"+ps.user_act+"/2434");
    }
}