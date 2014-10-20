package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.SmartHome.Adaptor.ModeDetailAdaptor;
import com.SmartHome.DataType.Mode;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

/**
 * Created by p on 14-5-18.
 */
public class ModeDetailActivity extends Activity {
    public PublicState ps = PublicState.getInstance();
    ModeDetailAdaptor adaptor = null;
    TextView room_name = null;
    Mode cmode =null;
    int mode_index;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_level_layout);
        TextView tv = (TextView)findViewById(R.id.acitivity_name);
        ps.current_ui_content = this;
        ps.activitis.put(getClass().getName(),this);
        Intent intent = getIntent();
        if(intent.hasExtra("modenum")){
            mode_index = intent.getIntExtra("modenum",-1);
            if(mode_index != -1)
                cmode = ps.mode_list.get(mode_index);
        }
        RelativeLayout rs = (RelativeLayout)findViewById(R.id.room_selector);
        rs.setVisibility(View.INVISIBLE);
        tv.setText(cmode.name+"详情");
        adaptor = new ModeDetailAdaptor(this,cmode);
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