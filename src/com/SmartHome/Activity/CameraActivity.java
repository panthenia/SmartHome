package com.SmartHome.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;
/**
 * Created by p on 14-4-11.
 */
public class CameraActivity extends Activity {

    WebView web,web_control;
    ImageView left,right,up,down;
    String cam_de;String cam_ip;
    PublicState ps = PublicState.getInstance();
    private Thread mThread;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    web.reload();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_activity);
        web_control = (WebView) findViewById(R.id.camera_control);
        web = (WebView) findViewById(R.id.camera_content);
        left = (ImageView) findViewById(R.id.camera_left);
        right = (ImageView) findViewById(R.id.camera_right);
        up = (ImageView) findViewById(R.id.camera_up);
        down = (ImageView) findViewById(R.id.camera_down);
        TextView textView = (TextView)findViewById(R.id.current_user);
        textView.setText("当前用户："+ps.user_act);
        if(ps.net_ip.contains("192.168.1."))
            cam_ip = "192.168.1.103";
        else cam_ip = "10.103.240.23";

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+cam_ip+":50000"+"/nphControlCamera?Direction=PanLeft";
                web_control.loadUrl(url);
                //"http://192.168.1.103:50000/nphControlCamera?Direction=PanLeft"
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+cam_ip+":50000"+"/nphControlCamera?Direction=PanRight";
                web_control.loadUrl(url);
                //web_control.loadUrl("http://192.168.1.103:50000/nphControlCamera?Direction=PanRight");

            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+cam_ip+":50000"+"/nphControlCamera?Direction=TiltUp";
                web_control.loadUrl(url);

                //web_control.loadUrl("http://192.168.1.103:50000/nphControlCamera?Direction=TiltUp");

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+cam_ip+":50000"+"/nphControlCamera?Direction=TiltDown";
                web_control.loadUrl(url);
                //web_control.loadUrl("http://192.168.1.103:50000/nphControlCamera?Direction=TiltDown");

            }
        });
        //web.loadUrl("http://192.168.1.110:50000/SnapshotJPEG?Resolution=[640x480]&amp;Quality=[Clarity]");
        String url = "http://"+cam_ip+":50000"+"/SnapshotJPEG?Resolution=[640x480]&amp;Quality=[Clarity]";
        web.loadUrl(url);

        mThread = new Thread(runnable);
        mThread.start();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while(true){
                mHandler.obtainMessage(0, null).sendToTarget();
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}