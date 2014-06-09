package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.SmartHome.Adaptor.ModeAdaptor;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;


/**
 * Created by p on 14-5-16.
 */
public class ModeActivity extends Activity {

    public PublicState ps = PublicState.getInstance();
    ModeAdaptor adaptor = null;
    TextView room_name = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_level_layout);
        TextView tv = (TextView)findViewById(R.id.acitivity_name);
        tv.setText("场景");
        RelativeLayout rs = (RelativeLayout)findViewById(R.id.room_selector);
        rs.setVisibility(View.INVISIBLE);
        adaptor = new ModeAdaptor(this);
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
                .setSingleChoiceItems(rooms,0,new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ps.selected_room = ps.room_list.get(i);
                        room_name.setText(ps.selected_room.name);
                        adaptor.changeRoom();
                        adaptor.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }
}