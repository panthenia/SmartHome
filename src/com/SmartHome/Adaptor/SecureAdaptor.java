package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.SmartHome.Activity.CameraActivity;
import com.SmartHome.DataType.Device;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 14-4-29.
 *
 */
public class SecureAdaptor extends BaseAdapter {

    Context context = null;
    LayoutInflater inflater = null;
    ArrayList<Device> devices = null;
    ArrayList<Device> room_devices = null;
    PublicState ps = PublicState.getInstance();

    public SecureAdaptor(Context ctx){
        context = ctx;
        room_devices = new ArrayList<Device>();
        inflater = LayoutInflater.from(ctx);
        devices = ps.getDeviceByType(context.getResources().getStringArray(R.array.SECURETY_CATEGORY));
        changeRoom();
    }
    public void changeRoom(){
        room_devices.clear();

        for (Device device : devices)
            if (device.room.equals(ps.selected_room.id)) {
                room_devices.add(device);
            }
    }
    @Override
    public int getCount() {
        if(ps.user_act.contains("bupt") && ps.selected_room.name.contains("902"))
            return 1;
        else return 0;
        //return room_devices.size();
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
        View v = inflater.inflate(R.layout.normal_block_layout,null);
        ImageView icon = (ImageView) v.findViewById(R.id.block_icon);
        TextView text = (TextView) v.findViewById(R.id.block_text);
        icon.setImageResource(R.drawable.security_camera);

        //DeviceSatus status = ps.getDeviceStatusById(room_devices.get(i).id);

       // Log.d("status",String.valueOf(ps.status_list.size()));
        //text.setText(room_devices.get(i).name);
        text.setText("一号摄像头");
        //final String device_id = room_devices.get(i).id;
        //status.printInfo();
        /*if(status.getVar("DEVICE_STATUS").contains("online")){

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra("device_id",device_id);
                    context.startActivity(intent);
                }
            });

        }else{
            text.setTextColor(Color.rgb(0x2e,0x2e,0x2e));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "当前设备已离线", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CameraActivity.class);
                //intent.putExtra("device_id",device_id);
                context.startActivity(intent);
            }
        });


        return v;
    }
}
