package com.SmartHome.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.SmartHome.Activity.RuleActivity;
import com.SmartHome.DataType.Mode;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.Rule;
import com.SmartHome.R;

/**
 * Created by p on 14-5-18.
 */
public class ModeDetailAdaptor extends BaseAdapter {
    Context context = null;
    PublicState ps = PublicState.getInstance();
    LayoutInflater inflater = null;
    Mode cmode = null;
    public ModeDetailAdaptor(Context ctx,Mode mode){
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        cmode = mode;

    }
    public void changeRoom(){

    }
    @Override
    public int getCount() {
        return cmode.rules.size();
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
        icon.setImageResource(R.drawable.rulers);
        final Rule crule = ps.getRuleById(cmode.rules.get(i));
        if(crule != null){
            text.setText(crule.name);
            v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, RuleActivity.class);
                    intent.putExtra("ruleid",crule.id);
                    context.startActivity(intent);
                }
        });
        }else {
            text.setText("未知规则");
        }
        return v;
    }
}
