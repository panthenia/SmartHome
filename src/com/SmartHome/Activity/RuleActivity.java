package com.SmartHome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.SmartHome.Adaptor.RuleConditionAdaptor;
import com.SmartHome.Adaptor.RuleOpsAdaptor;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.RequestInfo;
import com.SmartHome.DataType.Rule;
import com.SmartHome.R;
import com.SmartHome.Util.InfoParser;
import com.SmartHome.Util.ServiceRequest;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.zip.Inflater;

/**
 * Created by p on 14-5-16.
 */
public class RuleActivity extends Activity {

    TextView textViews[] = new TextView[3] ;//3个媒体类型
    TextView start_hour,start_mini,end_hour,end_mini;
    ImageView imageView[] = new ImageView[3];//3个括号
    ToggleButton toggleButtons[] = new ToggleButton[7];//77个星期
    ListView listView;//显示规则操作
    ExpandableListView con_list;
    PublicState ps = PublicState.getInstance();
    RelativeLayout relativeLayout[] = new RelativeLayout[2];//0-time , 1-val
    Rule crule = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rule_activity);
        Intent intent = getIntent();
        if(intent.hasExtra("ruleid"))  {
           crule = ps.getRuleById(intent.getStringExtra("ruleid"));
        }

        initRuleUi();

    }
    void initRuleUi(){
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        textView.setText(crule.name+" 详情");

        relativeLayout[0] = (RelativeLayout)findViewById(R.id.time_triger);
        relativeLayout[1] = (RelativeLayout)findViewById(R.id.val_triger);
        relativeLayout[0].setVisibility(View.INVISIBLE);
        relativeLayout[1].setVisibility(View.INVISIBLE);

        toggleButtons[0] = (ToggleButton)findViewById(R.id.tb1);
        toggleButtons[1] = (ToggleButton)findViewById(R.id.tb2);
        toggleButtons[2] = (ToggleButton)findViewById(R.id.tb3);
        toggleButtons[3] = (ToggleButton)findViewById(R.id.tb4);
        toggleButtons[4] = (ToggleButton)findViewById(R.id.tb5);
        toggleButtons[5] = (ToggleButton)findViewById(R.id.tb6);
        toggleButtons[6] = (ToggleButton)findViewById(R.id.tb7);

        //初始化时间条件的时间值

        start_hour = (TextView)findViewById(R.id.st_hour);
        start_hour.setText(crule.hour);
        start_mini = (TextView)findViewById(R.id.st_mini);
        start_mini.setText(crule.minute);
        if(crule.has_time){
            if(crule.cycle_type.contains("weekly")){
                String [] arrstr = crule.cycle_content.split(",");
                for(int i=0;i<arrstr.length;++i){
                    toggleButtons[Integer.valueOf(arrstr[i])-1].setChecked(true);
                }
            }else if(crule.cycle_type.contains("daily")){
                int jg = Integer.valueOf(crule.cycle_content);
                for(int i=0;i<7;){
                    toggleButtons[i].setChecked(true);
                    i+=jg;
                }
            }
        }

        //初始化三个文本的标签
        textViews[0]=(TextView)findViewById(R.id.textView1);
        textViews[1]=(TextView)findViewById(R.id.textView2);
        textViews[2]=(TextView)findViewById(R.id.textView3);
        textViews[0].setSelected(true);//默认显示操作列表

        //初始化三个括号图片，并控制先显示第一个
        imageView[0]=(ImageView)findViewById(R.id.pic1);
        imageView[1]=(ImageView)findViewById(R.id.pic2);
        imageView[2]=(ImageView)findViewById(R.id.pic3);
        imageView[0].setVisibility(View.VISIBLE);
        imageView[1].setVisibility(View.INVISIBLE);
        imageView[2].setVisibility(View.INVISIBLE);


        //初始化操作列表
        listView = (ListView)findViewById(R.id.list);
        String[] ops = {};
        if(crule != null){
            ops = new String[crule.rule_ops.size()];
            for(int i=0;i<ops.length;++i)
                ops[i] = crule.getOpStr(i);
        }
        RuleOpsAdaptor adaptor = new RuleOpsAdaptor(this,ops);
        listView.setAdapter(adaptor);

        //初始化条件列表
        con_list = (ExpandableListView)findViewById(R.id.rule_condition);
        con_list.setGroupIndicator(null);
        RuleConditionAdaptor ruleConditionAdaptor = new RuleConditionAdaptor(this,crule);
        con_list.setAdapter(ruleConditionAdaptor);
    }
    public void onRuleOpsClicked(View v){// 在 textviews 中对应的位置 0
        listView.setVisibility(View.VISIBLE);
        relativeLayout[0].setVisibility(View.INVISIBLE);
        relativeLayout[1].setVisibility(View.INVISIBLE);
        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[0].setSelected(true);
        imageView[0].setVisibility(View.VISIBLE);
    }
    public void onTimeConditionClicked(View v){//1



        listView.setVisibility(View.INVISIBLE);
        relativeLayout[0].setVisibility(View.VISIBLE);
        relativeLayout[1].setVisibility(View.INVISIBLE);
        if(crule.has_time != true)
            relativeLayout[0].setVisibility(View.INVISIBLE);

        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[1].setSelected(true);
        imageView[1].setVisibility(View.VISIBLE);
    }
    public void onValConditionClicked(View v){//2
        listView.setVisibility(View.INVISIBLE);
        relativeLayout[0].setVisibility(View.INVISIBLE);
        relativeLayout[1].setVisibility(View.VISIBLE);
        for(int i=0;i<textViews.length;++i){
            textViews[i].setSelected(false);
            imageView[i].setVisibility(View.INVISIBLE);
        }
        textViews[2].setSelected(true);
        imageView[2].setVisibility(View.VISIBLE);
    }


    public void onStartHourUpClicked(View v){

        int hour = Integer.valueOf(start_hour.getText().toString());
        if(hour == 23)
            hour = 0;
        else hour = hour+1;
        start_hour.setText(String.valueOf(hour));
    }
    public void onStartHourDownClicked(View v){

        int hour = Integer.valueOf(start_hour.getText().toString());
        if(hour == 0)
            hour = 23;
        else hour = hour-1;
        start_hour.setText(String.valueOf(hour));
    }
    public void onStartMiniUpClicked(View v){

        int mini = Integer.valueOf(start_mini.getText().toString());
        if(mini == 59 ){
            mini = 0;
            onStartHourUpClicked(null);
        }
        else mini = mini+1;
        start_mini.setText(String.valueOf(mini));
    }
    public void onStartMiniDownClicked(View v){

        int mini = Integer.valueOf(start_mini.getText().toString());
        if(mini == 0 ){
            mini = 59;
            onStartHourDownClicked(null);
        }
        else mini = mini-1;
        start_mini.setText(String.valueOf(mini));
    }
    public void onTimeSaveClicked(View v){
        String new_timer = "";
        String weeks = "";
        String getmode_url="http://"+ps.getNetAddress();
        ArrayList<String> tri_time = new ArrayList<String>();
        tri_time.add(start_hour.getText().toString());
        tri_time.add(start_mini.getText().toString());
        for(int i=0;i<toggleButtons.length;++i){
            if(toggleButtons[i].isChecked()){
                weeks += ((i+1)+",");
            }
        }
        weeks = weeks.substring(0,weeks.length()-1);
        tri_time.add(weeks);
        try {
            new_timer = InfoParser.makeRuleTimeInfo(crule,tri_time);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Log.d("tri_timer",new_timer);
        getmode_url+="/wsnRest/schedulerUpdate/username=bupt/sdfd";
        RequestInfo rf2= null;
        try {
            rf2 = new RequestInfo(getmode_url, new_timer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr2=new ServiceRequest("control");
        sr2.execute(rf2);
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
    public void onEndHourUpClicked(View v){

        int hour = Integer.valueOf(end_hour.getText().toString());
        if(hour == 23)
            hour = 0;
        else hour = hour+1;
        end_hour.setText(String.valueOf(hour));
    }
    public void onEndHourDownClicked(View v){

        int hour = Integer.valueOf(end_hour.getText().toString());
        if(hour == 0)
            hour = 23;
        else hour = hour-1;
        end_hour.setText(String.valueOf(hour));
    }
    public void onEndMiniUpClicked(View v){

        int mini = Integer.valueOf(end_mini.getText().toString());
        if(mini == 59 ){
            mini = 0;
            onStartHourUpClicked(null);
        }
        else mini = mini+1;
        end_mini.setText(String.valueOf(mini));
    }
    public void onEndMiniDownClicked(View v){

        int mini = Integer.valueOf(end_mini.getText().toString());
        if(mini == 0 ){
            mini = 59;
            onStartHourDownClicked(null);
        }
        else mini = mini-1;
        end_mini.setText(String.valueOf(mini));
    }
}