package com.SmartHome.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

/**
 * Created by p on 14-4-4.
 */
public class NetConfigActivity extends Activity {

    EditText ipEdit,portEdit;
    String ip,port;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.net_config_layout);

        PublicState ps = PublicState.getInstance();
        ipEdit = (EditText)findViewById(R.id.net_ip);
        portEdit = (EditText)findViewById(R.id.net_port);
        ps.activitis.put(getClass().getName(),this);
        if(ps.net_ip.length() > 0 && ps.net_port.length() > 0){
            ipEdit.setText(ps.net_ip);
            portEdit.setText(ps.net_port);
        }
    }
    public void onNetConfigCancel(View v){
        finish();
    }
    public void onNetConfigSubmit(View v){

        PublicState ps = (PublicState)getApplication();
        getInputText();
        if(ip.length() == 0 || port.length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),"网关信息有误，请重新填写", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        ps.net_ip = ip;
        ps.net_port = port;
        finish();
    }
    public void getInputText(){//该函数获取当前activity上的EditText控件输入的文本
        ip = String.valueOf(ipEdit.getText());
        port = String.valueOf(portEdit.getText());
    }
}