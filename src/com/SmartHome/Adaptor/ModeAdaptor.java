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
            if(mlist.get(i).status.contains("true"))
                mode_state[i] = true;
            else mode_state[i] = false;
    }
    public void changeRoom(){

    }

    public void showResult(String rst,String rsn){
        if(rst.contains("success")){
            Toast.makeText(context, "场景操作成功！", Toast.LENGTH_SHORT).show();

        }
        else Toast.makeText(context, "场景操作失败！原因："+rsn, Toast.LENGTH_LONG).show();
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
        if(mlist.get(i).status.contains("true")){
            text.setTextColor(Color.rgb(107, 193, 242));

        }else {
            text.setTextColor(Color.rgb(0x48,0x6a,0x00));

        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://"+ps.getNetAddress();
                if(mlist.get(i).status.contains("true")){
                    mode_state[i] = false;
                    mlist.get(i).status = "false";
                    url += "/wsnRest/sceneClose/"+ps.user_act+"/"+ps.getMd5();

                }
                else {
                    mode_state[i] = true;
                    mlist.get(i).status = "true";

                    url += "/wsnRest/sceneAdopt/"+ps.user_act+"/"+ps.getMd5();
                }
                Toast.makeText(context,"开始执行操作..", Toast.LENGTH_LONG).show();
                Log.d("start-scine:",url);
                String edata = null;
                try {
                    edata = ps.securityDemo.getEncodeData(cmode.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ps.controlRequest(url,edata,context);
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
