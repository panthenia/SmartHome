package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.SmartHome.DataType.Area;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;
import com.SmartHome.Util.EnvironmentTask;
import com.SmartHome.Util.InfoParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Timer;

/**
 * Created by p on 14-4-24.
 */
public class MainActivity extends Activity {

    PublicState ps = PublicState.getInstance();
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //ps.rule_info = "";//"<Rules><Rule><RuleId>序号</RuleId><RuleName>模式名</RuleName><timeXML>xml</timeXML><RuleContent><RuleCondition><groupRelation>and</groupRelation><conditionGroup><conditionRelation>or</conditionRelation><condition><ConditionID>8</ConditionID><NodeID>变量所属节点</NodeID><VarID>变量序号</VarID><VarName>变量名称</VarName><VarOper>比较运算符</VarOper><VarValue>比较数值</VarValue></condition></conditionGroup><conditionGroup><conditionRelation>or</conditionRelation><condition><ConditionID>7</ConditionID><NodeID>变量所属节点</NodeID><VarID>变量序号</VarID><VarName>变量名称</VarName><VarOper>比较运算符</VarOper><VarValue>比较数值</VarValue></condition><condition><ConditionID>6</ConditionID><NodeID>变量所属节点</NodeID><VarID>变量序号</VarID><VarName>变量名称</VarName><VarOper>比较运算符</VarOper><VarValue>比较数值</VarValue></condition></conditionGroup></RuleCondition><RuleCommand><DeviceId>设备编号2</DeviceId><Operator>操作命令2</Operator><CommandName>操作名称</CommandName></RuleCommand><RuleCommand><DeviceId>设备编号2</DeviceId><Operator>操作命令2</Operator><CommandName>操作名称</CommandName></RuleCommand></RuleContent></Rule></Rules>";

       // Log.d("rule_info:","start");

        try {
            InfoParser.parseDeviceInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            InfoParser.parseRuleInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            InfoParser.parseModeInfo();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.main_layout);
        TextView textView = (TextView)findViewById(R.id.current_user);
        textView.setText("当前用户："+ps.user_act);
        ps.printAreas();
        if(ps.room_list.size()>0)
            ps.selected_room = ps.room_list.get(0);
        else ps.selected_room = new Area("未知区域","null");

        Timer envi_timer=new Timer(true);
        EnvironmentTask mtask=new EnvironmentTask(ps);
        envi_timer.schedule(mtask, 100,1000*90);

        Timer fresh_timer=new Timer(true);
        EnvironmentTask mtask2=new EnvironmentTask(ps);
        fresh_timer.schedule(mtask2, 10,1000*60*10);
    }

   public void printDevices(){
       PublicState ps = PublicState.getInstance();
       for(int i=0;i<ps.device_list.size();++i){
          if(ps.device_list.get(i).name.contains("sblight"))
              ps.device_list.get(i).type="lamp";
       }
       for(int i=0;i<ps.room_list.size();++i)
           Log.d("device_list",ps.room_list.get(i).name);
   }

    public void onSecureIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,SecureActivity.class);
        startActivity(intent);
    }
    public void onModeIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,ModeActivity.class);
        startActivity(intent);
    }
    public void onEnvironmenIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,EnvironmenActivity.class);
        startActivity(intent);
    }
    public void onSettingIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(intent);
    }
    public void onLightIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,LightActivity.class);
        startActivity(intent);
    }
    public void onMediaIconClicked(View v){
        Intent intent = new Intent(MainActivity.this,MediaActivity.class);
        startActivity(intent);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog isExit = new AlertDialog.Builder(this)
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this,FinishActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create();
            isExit.setTitle("系统提示");
            isExit.setMessage("确定要退出吗");

            isExit.show();

        }

        return false;

    }
}
