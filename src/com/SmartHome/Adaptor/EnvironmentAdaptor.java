package com.SmartHome.Adaptor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.SmartHome.Achat.PortLineChart;
import com.SmartHome.Activity.AirActivity;
import com.SmartHome.Activity.TelevisionActivity;
import com.SmartHome.DataType.Device;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by p on 14-4-25.
 * <p/>
 * 该类是适配器类，为二级Activity中的gridview 提供要显示的东西。
 * 其中的重写的getview函数返回每个需要显示的子项的界面
 */
public class EnvironmentAdaptor extends BaseAdapter {

    LayoutInflater inflater = null;
    Context context = null;
    PublicState ps = PublicState.getInstance();
    ArrayList<Device> room_devices = null;
    ArrayList<Device> devices = null;
    Device temperatrue,light,humidy;


    public EnvironmentAdaptor(Context ctx) {

        inflater = LayoutInflater.from(ctx);
        context = ctx;
        temperatrue = new Device("温度数据",ps.selected_room.name,"Temperature","null");
        temperatrue.icon = R.drawable.temprature;
        light = new Device("光照数据",ps.selected_room.name,"Light","null");
        light.icon = R.drawable.guangzhao;
        humidy = new Device("湿度数据",ps.selected_room.name,"Humidity","null");
        humidy.icon = R.drawable.shidu;
        room_devices = new ArrayList<Device>();
        devices = ps.getDeviceByType(context.getResources().getStringArray(R.array.ENVIRONMENT_CATEGORY));
        changeRoom();
    }
    public void changeRoom(){

        room_devices.clear();

        room_devices.add(temperatrue);
        room_devices.add(humidy);
        room_devices.add(light);

        //插入测试数据

        //
        for(int i=0;i<devices.size();++i)
            if(devices.get(i).room.equals(ps.selected_room.id)){
                room_devices.add(devices.get(i));
            }
    }
    @Override
    public int getCount() {
        return room_devices.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.normal_block_layout, null);

        final Device dv = room_devices.get(i);

        ImageView icon = (ImageView) v.findViewById(R.id.block_icon);
        TextView text = (TextView) v.findViewById(R.id.block_text);
        icon.setImageResource(dv.getIcon());
        text.setText(dv.name);
        if(dv.type.equals("air")){
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AirActivity.class);
                    intent.putExtra("device_id",dv.id);
                    context.startActivity(intent);
                }
            });
        }
        else v.setOnClickListener(new EnvironmentStateClicked(context,dv.type));

        return v;
    }
}

class EnvironmentStateClicked implements View.OnClickListener {
    int click_time;
    Context context = null;
    String environment_type = "";
    public EnvironmentStateClicked(Context ctx ,String type){
        context = ctx;
        environment_type = type;
    }
    @Override
    public void onClick(View view) {
        click_time = 1;

       DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(click_time == 0)
                            return;
                        else{
                            click_time -= 1;
                        }
                        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                        Log.d("selected-date",date);
                        PortLineChart tr = new PortLineChart(environment_type,date);
                        Intent intent = tr.execute(context);
                        if (intent == null) {
                            Toast toast = Toast.makeText(context, "当前区域无该类型数据！", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else context.startActivity(intent);
                    }
                }
                // 设置初始日期
                ,Calendar.YEAR, Calendar.MONTH+1, Calendar.DAY_OF_MONTH);//.show();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.JANUARY,1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        datePickerDialog.show();

    }
}
