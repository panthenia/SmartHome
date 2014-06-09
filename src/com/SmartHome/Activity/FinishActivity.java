package com.SmartHome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by p on 14-4-29.
 */
public class FinishActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(FinishActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }
}