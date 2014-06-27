package com.SmartHome.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.SmartHome.DataType.Device;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 14-4-28.
 */
public class LightAdaptor extends BaseAdapter {

    Context context = null;
    LayoutInflater inflater = null;
    ArrayList<Device> devices = null;
    ArrayList<Device> room_devices = null;
    PublicState ps = PublicState.getInstance();
    LightAdaptor adaptorSelf=null;

    public LightAdaptor(Context ctx){
        context = ctx;
        room_devices = new ArrayList<Device>();
        devices = ps.getDeviceByType(context.getResources().getStringArray(R.array.LIGHT_CATEGORY));
        changeRoom();
        adaptorSelf=this;
        inflater = LayoutInflater.from(ctx);
    }
    public void changeRoom(){
        room_devices.clear();

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
        Device dv = room_devices.get(i);
        final int device_sequense=i;
        SeekBar seekBar = null;
        ImageView icon = null;
        ToggleButton toggleButton = null;
        TextView text = null;
        Switch swt = null;
        View v=null;
        if(dv.type.equals("light")){
            v = inflater.inflate(R.layout.light_layout,null);
            seekBar = (SeekBar)v.findViewById(R.id.light_seekbar);
            if(dv.status.containsKey("light_level"))
                seekBar.setProgress(Integer.valueOf(dv.status.get("light_level")));
            else seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int i= seekBar.getProgress();
                    room_devices.get(device_sequense).status.put("light_level",String.valueOf(i));
                    if(i>0)
                        room_devices.get(device_sequense).status.put("power",String.valueOf(true));
                    else room_devices.get(device_sequense).status.put("power",String.valueOf(false));
                    String url="http://"+ps.getNetAddress();
                    if(i == 0){
                        url += "/wsnRest/control/"+room_devices.get(device_sequense).id+"/2/"+ps.user_act+"/2434";
                    }else{
                    url += "/wsnRest/control/"+room_devices.get(device_sequense).id+"/3/"
                            +String.valueOf(i * 2)+"/"+ps.user_act+"/2434";
                    }
                    Log.d("request-url:",url);

                    ps.controlRequest(url);
                    adaptorSelf.notifyDataSetChanged();

                }
            });

        }
        else if(dv.type.equals("lamp")){
            v = inflater.inflate(R.layout.normal_light,null);
            toggleButton = (ToggleButton)v.findViewById(R.id.light_toogle);
            if(dv.status.containsKey("power"))
                toggleButton.setChecked(Boolean.valueOf(dv.status.get("power")));
            else toggleButton.setChecked(false);
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    room_devices.get(device_sequense).status.put("power",String.valueOf(b));
                    String url="http://"+ps.getNetAddress();
                    url += "/wsnRest/control/" + room_devices.get(device_sequense).id;

                    if(b){
                        url += "/1/"+ps.user_act+"/2434";
                    }
                    else url += "/2/"+ps.user_act+"/2434";
                    Log.d("request-url:",url);

                     ps.controlRequest(url);
                     adaptorSelf.notifyDataSetChanged();

                }
            });

        }else if(dv.type.equals("curtain")){
            v = inflater.inflate(R.layout.curtain_layout,null);
            swt = (Switch)v.findViewById(R.id.curtain_switch);
            if(dv.status.containsKey("power"))
                swt.setChecked(Boolean.valueOf(dv.status.get("power")));
            else swt.setChecked(false);
            swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    room_devices.get(device_sequense).status.put("power",String.valueOf(b));

                    String url = "http://" + ps.getNetAddress()
                            + "/wsnRest/control/" + room_devices.get(device_sequense).id;
                    if(b){
                        url += "/1/"+ps.user_act+"/2434";
                    }else url += "/2/"+ps.user_act+"/2434";
                    Log.d("request-url:",url);
                    ps.controlRequest(url);

                    adaptorSelf.notifyDataSetChanged();


                }
            });

        }

        icon = (ImageView) v.findViewById(R.id.block_icon);
        text = (TextView) v.findViewById(R.id.block_text);
        text.setText(dv.name);
        icon.setImageResource(dv.getIcon());

        return v;
    }
}
