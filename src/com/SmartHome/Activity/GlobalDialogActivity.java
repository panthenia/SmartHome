package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.SmartHome.R;

/**
 * Created by p on 2014/10/20.
 */
public class GlobalDialogActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.global_dialog_activity);
        String information = null,title = null;
        Intent intent = getIntent();
        if(intent.hasExtra("msg")){
            information = intent.getStringExtra("msg");

        }
        if(intent.hasExtra("title")){
            title = intent.getStringExtra("title");

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(information)
                .setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(GlobalDialogActivity.this,FinishActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}