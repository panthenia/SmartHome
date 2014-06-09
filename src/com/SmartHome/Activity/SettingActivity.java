package com.SmartHome.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.SmartHome.Adaptor.SettingAdaptor;
import com.SmartHome.R;

/**
 * Created by p on 14-4-28.
 */
public class SettingActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_level_layout);

        TextView tv = (TextView)findViewById(R.id.acitivity_name);
        tv.setText("设置");

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.room_selector);
        rl.setEnabled(false);
        rl.setVisibility(View.INVISIBLE);

        SettingAdaptor settingAdaptorAdaptor = new SettingAdaptor(this);
        GridView gridView = (GridView) findViewById(R.id.content_grid);
        gridView.setAdapter(settingAdaptorAdaptor);
    }
}