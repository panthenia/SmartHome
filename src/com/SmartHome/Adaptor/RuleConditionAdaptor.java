package com.SmartHome.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.DataType.RequestInfo;
import com.SmartHome.DataType.Rule;
import com.SmartHome.R;
import com.SmartHome.Util.InfoParser;
import com.SmartHome.Util.ServiceRequest;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by p on 14-5-26.
 */
public class RuleConditionAdaptor extends BaseExpandableListAdapter {

    Context context = null;
    LayoutInflater inflater = null;
    PublicState ps = PublicState.getInstance();
    Rule crule = null;
    public RuleConditionAdaptor(Context ctx,Rule rule){
        context = ctx;
        crule = rule;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getGroupCount() {
        return crule.cdt_groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return crule.getGroupConditionlength(i);
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v ;

        v = inflater.inflate(R.layout.condition_group,null);
        TextView tv = (TextView)v.findViewById(R.id.grp_name);
        tv.setText("条件-"+(i+1));
        if(i != 0){
            tv = (TextView)v.findViewById(R.id.grp_condition);
            if(crule.getGroupRelation(i-1).contains("and"))
                tv.setText("并且");
            else tv.setText("或者");
        }
        return v;
    }

    @Override
    public View getChildView(final int grp, final int cdt, boolean b, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.rule_condition,null);


        ArrayList<String> cd = crule.getGroupCondition(grp,cdt);
        String op;
        TextView vname = (TextView)v.findViewById(R.id.val_name);
        vname.setText("当:"+cd.get(cd.size()-1)+"的 "+cd.get(0));

        if(cdt != 0){
            vname = (TextView)v.findViewById(R.id.cdt_relation);
            if(crule.getConditionRelation(grp).contains("and")){
                vname.setText("并且");
            }else {
                vname.setText("或者");
            }
        }
        final TextView voper = (TextView)v.findViewById(R.id.val_ops);
        op = cd.get(1).replace("&lt;","<");
        op = op.replace("&gt;",">");
        voper.setText(op);
        final TextView vval = (TextView)v.findViewById(R.id.val_value);
        vval.setText(cd.get(2));

        voper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String op[]={">","=","<",">=","<=","!="};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("修改比较方式");
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setSingleChoiceItems(op, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView vv = (TextView) voper;
                        vv.setText(op[i]);
                        crule.setConditionOper(grp, cdt, op[i]);
                        String getmode_url="http://"+ps.getNetAddress();
                        String new_rule = "";
                        try {
                            new_rule = InfoParser.makeRuleConditionInfo(crule, grp, cdt);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        getmode_url+="/wsnRest/schedulerUpdate/"+ps.user_act+"/"+ps.getMd5();
                        RequestInfo rf2= null;
                        String encode_rule = null;
                        try {
                            encode_rule = ps.securityDemo.getEncodeData(new_rule);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            rf2 = new RequestInfo(getmode_url, encode_rule);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        ServiceRequest sr2=new ServiceRequest("control");
                        sr2.execute(rf2);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        vval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(context);
                new AlertDialog.Builder(context).setTitle("请输入新值").setIcon(
                        android.R.drawable.ic_dialog_info).setView(
                        editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = editText.getText().toString();
                        TextView tv = (TextView)vval;
                        crule.setConditionVal(grp, cdt, text);
                        String getmode_url="http://"+ps.getNetAddress();
                        String new_rule = "";
                        try {
                            new_rule = InfoParser.makeRuleConditionInfo(crule, grp, cdt);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        getmode_url+="/wsnRest/schedulerUpdate/"+ps.user_act+"/"+ps.getMd5();
                        RequestInfo rf2= null;
                        String encode_rule = null;
                        try {
                            encode_rule = ps.securityDemo.getEncodeData(new_rule);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            rf2 = new RequestInfo(getmode_url, encode_rule);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        ServiceRequest sr2=new ServiceRequest("control");
                        sr2.execute(rf2);

                        tv.setText(text);
                    }
                })
                        .setNegativeButton("取消", null).show();
            }
        });
        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

}
