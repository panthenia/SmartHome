package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import com.SmartHome.Adaptor.MediaAdaptor;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

/**
 * Created by p on 14-4-29.
 */
public class MediaActivity extends Activity {

    public PublicState ps = PublicState.getInstance();
    TextView room_name = null;
    MediaAdaptor adaptor = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_level_layout);
        TextView tv = (TextView)findViewById(R.id.acitivity_name);
        tv.setText("影音");
        room_name = (TextView)findViewById(R.id.room_name);
        room_name.setText(ps.selected_room.name);
        adaptor = new MediaAdaptor(this);
        GridView gridView = (GridView) findViewById(R.id.content_grid);
        gridView.setAdapter(adaptor);
    }
    public void onRoomSelectorClicked(View v){

        String[] rooms = new String[ps.room_list.size()];
        for(int i=0;i<ps.room_list.size();++i)
            rooms[i] = ps.room_list.get(i).name;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("请选择区域")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(rooms,-1,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("before_change", ps.selected_room.id);
                        ps.selected_room = ps.room_list.get(i);
                        room_name.setText(ps.selected_room.name);
                        adaptor.changeRoom();
                        Log.d("after_change", ps.selected_room.id);
                        adaptor.notifyDataSetChanged();

                        dialogInterface.dismiss();

                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }
}