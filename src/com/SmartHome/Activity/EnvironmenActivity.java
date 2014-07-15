package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import com.SmartHome.Adaptor.EnvironmentAdaptor;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

/**
 * Created by p on 14-4-25.
 *
 * 该activity为环境activity，不控制显示，所有的显示控制由EvironmenAdaptor控制
 *
 */
public class EnvironmenActivity extends Activity {

    public PublicState ps = PublicState.getInstance();
    TextView area_name = null;
    EnvironmentAdaptor environmentAdaptor = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_level_layout);
        TextView textView = (TextView)findViewById(R.id.current_user);
        textView.setText("当前用户："+ps.user_act);
        environmentAdaptor = new EnvironmentAdaptor(this);
        GridView gridView = (GridView) findViewById(R.id.content_grid);
        TextView editText = (TextView) findViewById(R.id.acitivity_name);
        area_name = (TextView)findViewById(R.id.room_name);
        editText.setText("环境");
        gridView.setAdapter(environmentAdaptor);
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
                        ps.selected_room = ps.room_list.get(i);
                        dialogInterface.dismiss();
                        area_name.setText(ps.selected_room.name);
                        environmentAdaptor.changeRoom();
                        environmentAdaptor.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }
}