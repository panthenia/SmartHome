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
import com.SmartHome.R;
/**
 * Created by p on 14-4-11.
 */
public class CameraActivity extends Activity {

    WebView web,web_control;
    ImageView left,right,up,down;
    String cam_de;
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

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web_control.loadUrl("http://192.168.1.110:50000/nphControlCamera?Direction=PanLeft");
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web_control.loadUrl("http://192.168.1.110:50000/nphControlCamera?Direction=PanRight");

            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web_control.loadUrl("http://192.168.1.110:50000/nphControlCamera?Direction=TiltUp");

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                web_control.loadUrl("http://192.168.1.110:50000/nphControlCamera?Direction=TiltDown");

            }
        });
        //web.loadUrl("http://192.168.1.110:50000/SnapshotJPEG?Resolution=[640x480]&amp;Quality=[Clarity]");
        web.loadUrl("http://192.168.1.110:50000/SnapshotJPEG?Resolution=[640x480]&amp;Quality=[Clarity]");

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