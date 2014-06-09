package com.SmartHome.DataType;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by p on 14-5-16.
 */

public class Rule {
    RuleCondition temccd = null;
    RuleOperation temopt = null;
    ConditionGroup temgrp = null;
    PublicState ps = PublicState.getInstance();
    public String id,name,hour="",minute="",cycle_type="",cycle_content="";
    public boolean has_time = false;
    public ArrayList<ConditionGroup> cdt_groups = null;
    public ArrayList<RuleOperation> rule_ops = null;
    public ArrayList<String> group_relation = null;
    public Rule(){
        cdt_groups = new ArrayList<ConditionGroup>();
        rule_ops = new ArrayList<RuleOperation>();
        group_relation = new ArrayList<String>();
    }

    public void createGrp() {
        temgrp = new ConditionGroup();
    }
    public void setCdtRelation(String rlt){
        if(temgrp != null)
            temgrp.condition_relation = rlt;
    }
    public void setGrpRelation(String rlt){
        group_relation.add(rlt);
    }
    public void createCcd(){
        temccd = new RuleCondition();
    }
    public void setCcdId(String id){
        if(temccd != null)
            temccd.id = id;
    }
    public void setCcdNode(String nd){
        if(temccd != null)
            temccd.nodeid = nd;
    }
    public void setCcdVar(String var){
        if (temccd != null)
            temccd.varid = var;
    }
    public void setCcdOper(String op){
        if (temccd != null)
            temccd.oper = op;
    }
    public void setCcdval(String val){
        if (temccd != null)
            temccd.val = val;
    }
    public void setCcdvarName(String val){
        if (temccd != null)
            temccd.varname = val;
    }

    public void createOpt(){
        temopt = new RuleOperation();
    }
    public void setOptId(String id){
        if(temopt != null)
            temopt.device_id = id;
    }
    public void setOptoper(String var){
        if (temopt != null)
            temopt.operator = var;
    }

    public void setOptname(String val){
        if (temopt != null)
            temopt.opname = val;
    }
    public void saveOpt(){
        if (temopt != null)
            rule_ops.add(temopt);
    }
    public void saveCcd(){
        if(temgrp != null && temccd != null)
            temgrp.conditions.add(temccd);
    }
    public void saveGrp(){
        if(temgrp != null)
            cdt_groups.add(temgrp);
    }
    public String getOpStr(int i){
        return ps.getDeviceById(rule_ops.get(i).device_id).name+":"+rule_ops.get(i).opname;
    }
    public int getGroupConditionlength(int i){
        return cdt_groups.get(i).conditions.size();
    }
    public String getConditionRelation(int i){
        return cdt_groups.get(i).condition_relation;
    }
    public String getGroupRelation(int i){
        return group_relation.get(i);
    }
    public ArrayList<String> getGroupCondition(int grp,int cdt){
        ArrayList<String> cd = new ArrayList<String>();

        RuleCondition rcdt = cdt_groups.get(grp).conditions.get(cdt);
        cd.add(rcdt.varname);cd.add(rcdt.oper);cd.add(rcdt.val);
        cd.add(rcdt.nodeid);cd.add(rcdt.varid);cd.add(rcdt.id);

        return cd;
    }
    public void printInfo(){
        Log.d("rule_info:","group_size="+cdt_groups.size());
        for(int i=0;i<cdt_groups.size();++i){
            Log.d("rule_info:","group-"+i);
            cdt_groups.get(i).printInfo();
        }
    }
    public void setConditionOper(int grp,int cdt,String op){
        cdt_groups.get(grp).conditions.get(cdt).oper = op;
    }
    public void setConditionVal(int grp,int cdt,String val){
        cdt_groups.get(grp).conditions.get(cdt).val = val;
    }
    class RuleOperation {
        public String device_id,operator,opname;
    }
    class RuleCondition {
        public String nodeid,varid,oper,val,varname,id;
    }
    class ConditionGroup {
        public void printInfo(){
            Log.d("rule_info:","condition_relation:"+condition_relation);
            for(int i=0;i<conditions.size();++i){
                Log.d("rule_info:","cdt-"+i);
                Log.d("rule_info:","cdt-id"+conditions.get(i).id);
                Log.d("rule_info:","cdt-nodeid"+conditions.get(i).nodeid);
                Log.d("rule_info:","cdt-varid"+conditions.get(i).varid);
                Log.d("rule_info:","cdt-varname"+conditions.get(i).varname);
                Log.d("rule_info:","cdt-oper"+conditions.get(i).oper);
                Log.d("rule_info:","cdt-val"+conditions.get(i).val);
            }
        }
        public String condition_relation = "and";
        public String group_relation = "and";
        public ArrayList<RuleCondition> conditions = new ArrayList<RuleCondition>();
    }
}
