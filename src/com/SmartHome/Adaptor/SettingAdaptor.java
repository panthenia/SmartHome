package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.SmartHome.Activity.LoginActivity;
import com.SmartHome.R;

/**
 * Created by p on 14-4-28.
 */
public class SettingAdaptor extends BaseAdapter {

    Context context = null;
    LayoutInflater inflater = null;
    public SettingAdaptor(Context ctx){
        context = ctx;
        inflater = LayoutInflater.from(ctx);
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
        View v = inflater.inflate(R.layout.normal_block_layout, null);

        ImageView icon = (ImageView) v.findViewById(R.id.block_icon);
        TextView text = (TextView) v.findViewById(R.id.block_text);
        icon.setImageResource(R.drawable.users);
        text.setText("退出登录");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,LoginActivity.class);
                SharedPreferences ps = context.getSharedPreferences(context.getResources().getString(R.string.login_preference_name),context.MODE_PRIVATE);
                SharedPreferences.Editor editor = ps.edit();
                editor.clear();
                editor.commit();
                context.startActivity(intent);
            }
        });
        return v;
    }
}
