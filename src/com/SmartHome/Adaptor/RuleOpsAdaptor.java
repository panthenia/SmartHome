package com.SmartHome.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.SmartHome.R;

import java.util.ArrayList;

/**
 * Created by p on 2014/6/2.
 */
public class RuleOpsAdaptor extends BaseAdapter {
    String[] content = null;
    Context context = null;
    LayoutInflater inflater = null;
    public RuleOpsAdaptor(Context ctx,String[] ar){
        content = ar;
        context = ctx;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return content.length;
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
        View v = inflater.inflate(R.layout.rule_ops_layout,null);
        TextView tv =  (TextView)v.findViewById(R.id.rule_ops);
        tv.setText(content[i]);
        return v;
    }
}
