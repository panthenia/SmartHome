package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.SmartHome.Activity.ModeDetailActivity;
import com.SmartHome.DataType.Mode;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 14-5-16.
 */
public class ModeAdaptor extends BaseAdapter {
    Context context = null;
    LayoutInflater inflater = null;
    PublicState ps = PublicState.getInstance();
    ArrayList<Mode> mlist= null;
    boolean mode_state[];//场景当前状态 true 启用 ,false 未启用
    public ModeAdaptor(Context ctx){
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        mlist = ps.mode_list;
        mode_state = new boolean[mlist.size()];
        for(int i=0;i<mode_state.length;++i)
            mode_state[i] = false;
    }
    public void changeRoom(){

    }
    @Override
    public int getCount() {
        return mlist.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.normal_block_layout,null);
        ImageView icon = (ImageView) v.findViewById(R.id.block_icon);
        TextView text = (TextView) v.findViewById(R.id.block_text);
        icon.setImageResource(R.drawable.scine);

        final Mode cmode = mlist.get(i);
        final int mode_index = i;
        text.setText(cmode.name);
        if(mode_state[i] == true){
            text.setTextColor(Color.rgb(107, 193, 242));
        }else text.setTextColor(Color.rgb(0x48,0x6a,0x00));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://"+ps.getNetAddress();
                if(mode_state[i] == true){
                    mode_state[i] = false;
                    url += "/wsnRest/sceneClose/"+cmode.id+"/asd/sad";
                    Toast.makeText(context, cmode.name+"已关闭", Toast.LENGTH_SHORT).show();
                }
                else {
                    mode_state[i] = true;
                    Toast.makeText(context, cmode.name+"已启动", Toast.LENGTH_SHORT).show();
                    url += "/wsnRest/sceneAdopt/"+cmode.id+"/asd/sad";
                }
                Log.d("start-scine:",url);
                ps.controlRequest(url);
                notifyDataSetChanged();
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, ModeDetailActivity.class);
                intent.putExtra("modenum",mode_index);
                context.startActivity(intent);
                return true;
            }
        });
        return v;
    }
}
