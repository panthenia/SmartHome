package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.SmartHome.Activity.PlayerActivity;
import com.SmartHome.Activity.TelevisionActivity;
import com.SmartHome.DataType.Device;
import com.SmartHome.DataType.DeviceSatus;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 14-4-29.
 */
public class MediaAdaptor extends BaseAdapter {

    Context context = null;
    LayoutInflater inflater = null;
    ArrayList<Device> devices = null;
    ArrayList<Device> room_devices = null;
    PublicState ps = PublicState.getInstance();

    public MediaAdaptor(Context ctx) {
        context = ctx;
        room_devices = new ArrayList<Device>();
        devices = ps.getDeviceByType(context.getResources().getStringArray(R.array.MEDIA_CATEGORY));
        changeRoom();
        inflater = LayoutInflater.from(ctx);
    }

    public void changeRoom() {
        room_devices.clear();

        for (int i = 0; i < devices.size(); ++i)
            if (devices.get(i).room.equals(ps.selected_room.id)) {
                room_devices.add(devices.get(i));
            }
    }

    @Override
    public int getCount() {
        return room_devices.size();
    }

    @Override
    public Object getItem(int i) {
        return room_devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Device dv = room_devices.get(i);

        View v = inflater.inflate(R.layout.normal_block_layout, null);
        ImageView icon = (ImageView) v.findViewById(R.id.block_icon);
        TextView text = (TextView) v.findViewById(R.id.block_text);
        text.setText(dv.name);
        icon.setImageResource(dv.getIcon());
        DeviceSatus status = ps.getDeviceStatusById(room_devices.get(i).id);
        if (status.getVar("DEVICE_STATUS").contains("online")) {
            if (dv.type.equals("television")) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, TelevisionActivity.class);
                        intent.putExtra("device_id", dv.id);
                        context.startActivity(intent);
                    }
                });
            } else if (dv.type.equals("player")) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PlayerActivity.class);
                        intent.putExtra("device_id", dv.id);
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            text.setTextColor(Color.rgb(0x2e, 0x2e, 0x2e));
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "当前设备已离线", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return v;
    }
}
