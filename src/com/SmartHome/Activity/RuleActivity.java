package com.SmartHome.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;

/**
 * Created by p on 14-5-16.
 * 显示规则的activity！
 */
public class RuleActivity extends Activity {

    TextView textViews[] = new TextView[3] ;//3个媒体类型
    TextView start_hour,start_mini,end_hour,end_mini;
    ImageView imageView[] = new ImageView[3];//3个括号
    ToggleButton toggleButtons[] = new ToggleButton[7];//77个星期
    ListView listView;//显示规则操作
    ExpandableListView con_list;
    RadioGroup radioGroup ;
    EditText interDay;
    String tem_cycle;
    RadioButton [] radioButtons= new RadioButton[2];
    PublicState ps = PublicState.getInstance();
    RelativeLayout relativeLayout[] = new RelativeLayout[2];//0-time , 1-val
    Rule crule = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rule_activity);
        ps.current_ui_content = this;
        ps.activitis.put(getClass().getName(),this);
        Intent intent = getIntent();
        if(intent.hasExtra("ruleid"))  {
           crule = ps.getRuleById(intent.getStringExtra("ruleid"));
           Log.d("debug-norespone","crule.id"+crule.id);
        }

        initRuleUi();

    }
    void initRuleUi(){
        Log.d("debug-norespone","inot init-ui");
        TextView textView = (TextView)findViewById(R.id.acitivity_name);
        textView.setText(crule.name+" 详情");

        radioGroup = (RadioGroup) findViewById(R.id.radiogrp);
        radioButtons[0] = (RadioButton) findViewById(R.id.cycle_week);
        radioButtons[1] = (RadioButton) findViewById(R.id.cycle_day);
        interDay = (EditText)findViewById(R.id.inter_day);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rg, int k) {
                if(k == radioButtons[0].getId()){
                    interDay.setVisibility(View.INVISIBLE);
                    for(int i=0;i<7;++i){
                        toggleButtons[i].setVisibility(View.VISIBLE);
                    }
                    tem_cycle = "weekly";
                }
                else {
                    tem_cycle = "daily";
                    interDay.setVisibility(View.VISIBLE);
                    for(int i=0;i<7;++i){
                        toggleButtons[i].setVisibility(View.INVISIBLE);
                    }

                }
            }
        });


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
                radioGroup.check(radioButtons[0].getId());
                interDay.setVisibility(View.INVISIBLE);
                String [] arrstr = crule.cycle_content.split(",");
                for(int i=0;i<7;++i){
                    toggleButtons[i].setVisibility(View.VISIBLE);
                }
                for (String anArrstr : arrstr) {

                    toggleButtons[Integer.valueOf(anArrstr) - 1].setChecked(true);
                }
            }else if(crule.cycle_type.contains("daily")){
                interDay.setVisibility(View.VISIBLE);
                radioGroup.check(radioButtons[1].getId());
                interDay.setText(crule.cycle_content);
                for(int i=0;i<7;++i){
                    toggleButtons[i].setVisibility(View.INVISIBLE);
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
        ArrayList<ArrayList<String>> ops = null;
        if(crule != null){//
            ops = new ArrayList<ArrayList<String>>();
            for(int i=0;i < crule.rule_ops.size();++i)
                ops.add(crule.getOpStr(i));
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
        if(!crule.has_time)
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
        if(hour < 10)
            start_hour.setText("0"+String.valueOf(hour));
        else start_hour.setText(String.valueOf(hour));
    }
    public void onStartHourDownClicked(View v){

        int hour = Integer.valueOf(start_hour.getText().toString());
        if(hour == 0)
            hour = 23;
        else hour = hour-1;
        if(hour < 10)
            start_hour.setText("0"+String.valueOf(hour));
        else start_hour.setText(String.valueOf(hour));    }
    public void onStartMiniUpClicked(View v){

        int mini = Integer.valueOf(start_mini.getText().toString());
        if(mini == 59 ){
            mini = 0;
            onStartHourUpClicked(null);
        }
        else mini = mini+1;
        if(mini < 10)
            start_mini.setText("0"+String.valueOf(mini));
        else start_mini.setText(String.valueOf(mini));
    }
    public void onStartMiniDownClicked(View v){

        int mini = Integer.valueOf(start_mini.getText().toString());
        if(mini == 0 ){
            mini = 59;
            onStartHourDownClicked(null);
        }
        else mini = mini-1;
        if(mini < 10)
            start_mini.setText("0"+String.valueOf(mini));
        else start_mini.setText(String.valueOf(mini));    }
    public void onTimeSaveClicked(View v){
        String new_timer = "";
        String weeks = "";
        String getmode_url="http://"+ps.getNetAddress();
        ArrayList<String> tri_time = new ArrayList<String>();

        String start_date_str = String.valueOf(crule.st_date[0])+String.valueOf(crule.st_date[1])+String.valueOf(crule.st_date[2]);
        String end_date_str = String.valueOf(crule.ed_date[0])+String.valueOf(crule.ed_date[1])+String.valueOf(crule.ed_date[2]);

        if(end_date_str.compareTo(start_date_str) <0){
            Toast.makeText(this, "结束日期早于起始日期，请修改！", Toast.LENGTH_SHORT).show();
            return;
        }
        tri_time.add(start_hour.getText().toString());
        tri_time.add(start_mini.getText().toString());
        tri_time.add(tem_cycle);
        crule.hour = start_hour.getText().toString();
        crule.minute = start_mini.getText().toString();
        if(tem_cycle.contains("weekly")){
            for(int i=0;i<toggleButtons.length;++i){
                if(toggleButtons[i].isChecked()){
                    weeks += ((i+1)+",");
                }
            }
            weeks = weeks.substring(0,weeks.length()-1);
            tri_time.add(weeks);
        }else{
            tri_time.add(interDay.getText().toString());
        }
        tri_time.add(crule.st_stamp);
        tri_time.add(crule.ed_stamp);
        try {
            new_timer = InfoParser.makeRuleTimeInfo(crule,tri_time);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Log.d("tri_timer",new_timer);
        getmode_url+="/wsnRest/schedulerUpdate/"+ps.user_act+"/"+ps.getMd5();
        String encode_data = null;
        try {
            encode_data = ps.securityDemo.getEncodeData(new_timer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestInfo rf2= null;
        try {
            rf2 = new RequestInfo(getmode_url, encode_data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr2=new ServiceRequest("control");
        sr2.execute(rf2);
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }
    public void onSetStartDateClicked(View v){
        new DatePickerDialog(this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                                            crule.st_date[0] = year;
                        crule.st_date[1] = monthOfYear;
                            crule.st_date[2] = dayOfMonth;
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);

                        Log.d("set-date-start",String.valueOf(cal.getTimeInMillis()));
                        crule.st_stamp = String.valueOf(cal.getTimeInMillis()/1000);
                                            }
                }
                // 设置初始日期
                ,crule.st_date[0], crule.st_date[1], crule.st_date[2]).show();

    }
    public void onSetEndDateClicked(View v){
        new DatePickerDialog(this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        crule.ed_date[0] = year;
                        crule.ed_date[1] = monthOfYear;
                        crule.ed_date[2] = dayOfMonth;
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);
                        Log.d("set-date-end",String.valueOf(cal.getTimeInMillis()));

                        crule.ed_stamp = String.valueOf(cal.getTimeInMillis()/1000);
                    }
                }
                // 设置初始日期
                ,crule.ed_date[0], crule.ed_date[1], crule.ed_date[2]).show();
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