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

        for(int i=0;i<devices.size();++i)
            if(devices.get(i).room.equals(ps.selected_room.id)){
                room_devices.add(devices.get(i));
            }
    }
    @Override
    public int getCount() {
        return 1;
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
        text.setText("一号摄像头");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CameraActivity.class);
                context.startActivity(intent);
            }
        });
        return v;
    }
}
