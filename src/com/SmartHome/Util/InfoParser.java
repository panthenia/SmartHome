package com.SmartHome.Util;

import android.util.Log;
import android.util.Xml;
import com.SmartHome.DataType.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by p on 14-4-28.
 * 该类所有方法为静态方法，用于处理从网关获取数据的解析，当前网关获取的数据为XML格式数据
 *
 *
 */
public class InfoParser {
    public static void parseModeInfo() throws IOException,XmlPullParserException{
        PublicState ps = PublicState.getInstance();
        Mode cmode = null;
        ArrayList<Mode> mlist = new ArrayList<Mode>();
        int eventType = 0;
        XmlPullParser parser = Xml.newPullParser();

        InputStream xml = new ByteArrayInputStream(ps.mode_info.getBytes());
        parser.setInput(xml, "UTF-8");
        eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                String tagnm = parser.getName();
                String text ;
                if(tagnm.equals("Scene")){
                    cmode = new Mode();
                }else if(tagnm.equals("SceneId")){
                    text = parser.nextText().trim();
                    if(cmode != null)
                        cmode.id = text;
                }else if(tagnm.equals("SceneName")){
                    text = parser.nextText().trim();
                    if(cmode != null)
                        cmode.name = text;
                }else if(tagnm.equals("RuleId")){
                    text = parser.nextText().trim();
                    if(cmode != null)
                        cmode.rules.add(text);
                }
            } else if (eventType == XmlPullParser.TEXT) {

            } else if (eventType == XmlPullParser.END_TAG) {
                String tagnm = parser.getName();
                if(tagnm.equals("Scene")){
                    if(cmode != null)
                        mlist.add(cmode);
                }
            }

            eventType = parser.next();

        }
        ps.mode_list = mlist;

    }
    public static void parseRuleInfo() throws IOException,XmlPullParserException{
        PublicState ps = PublicState.getInstance();
        Rule crule = null ;
        ArrayList<Rule> rlist = new ArrayList<Rule>();
        int eventType = 0;
        XmlPullParser parser = Xml.newPullParser();

        InputStream xml = new ByteArrayInputStream(ps.rule_info.getBytes());
        Log.d("rule_info:",ps.rule_info);
        parser.setInput(xml, "UTF-8");
        eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                String tagnm = parser.getName();
                String text ;
                if(tagnm.equals("Rule")){
                    crule = new Rule();
                }else if(tagnm.equals("RuleId")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.id = text;
                }else if(tagnm.equals("hour")){
                    text = parser.nextText().trim();
                    if(crule != null){
                        crule.hour = text;
                        crule.has_time=true;
                    }
                }else if(tagnm.equals("minute")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.minute = text;
                }else if(tagnm.equals("type")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.cycle_type = text;
                }else if(tagnm.equals("daysOfWeek")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.cycle_content = text;
                }else if(tagnm.equals("interval")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.cycle_content = text;
                }else if(tagnm.equals("RuleName")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.name = text;
                }else if (tagnm.equals("groupRelation")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.setGrpRelation(text);

                }else if (tagnm.equals("conditionGroup")){

                    if(crule != null)
                        crule.createGrp();

                }else if (tagnm.equals("conditionRelation")){
                    text = parser.nextText().trim();
                    if(crule != null)
                        crule.setCdtRelation(text);

                }else if (tagnm.equals("condition")){
                    if(crule != null)
                        crule.createCcd();

                }else if (tagnm.equals("ConditionID")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdId(text);
                }else if (tagnm.equals("NodeID")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdNode(text);
                }else if (tagnm.equals("VarID")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdVar(text);
                }else if (tagnm.equals("VarName")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdvarName(text);
                }else if (tagnm.equals("VarOper")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdOper(text);
                }else if (tagnm.equals("VarValue")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setCcdval(text);
                }else if (tagnm.equals("RuleCommand")){

                    if (crule != null)
                        crule.createOpt();
                }else if (tagnm.equals("DeviceId")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setOptId(text);
                }else if (tagnm.equals("Operator")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setOptoper(text);
                }else if (tagnm.equals("CommandName")){
                    text = parser.nextText().trim();
                    if (crule != null)
                        crule.setOptname(text);
                }


            } else if (eventType == XmlPullParser.TEXT) {

            } else if (eventType == XmlPullParser.END_TAG) {
                String tagnm = parser.getName();
                if(tagnm.equals("Rule")){
                    if(crule != null)
                    rlist.add(crule);
                }else if (tagnm.equals("condition")){
                    if(crule != null)
                        crule.saveCcd();
                }else if (tagnm.equals("conditionGroup")){
                    if(crule != null)
                        crule.saveGrp();
                }else if (tagnm.equals("RuleCommand")){
                    if(crule != null)
                        crule.saveOpt();
                }

            }
            eventType = parser.next();
        }
        if(crule == null){
            Log.d("rule_info:","crule==null");
        }else Log.d("rule_info:","crule@=null");
        ps.rule_list = rlist;

    }
    public static void parseDeviceInfo() throws IOException, XmlPullParserException {

        ArrayList<Device> devices = new ArrayList<Device>();
        ArrayList<Area> rooms = new ArrayList<Area>();
        XmlPullParser parser = Xml.newPullParser();
        Device cdevice =  null;
        Area care = null;
        PublicState ps = PublicState.getInstance();
        int eventType = 0;


        InputStream xml = new ByteArrayInputStream(ps.device_info.getBytes());
        parser.setInput(xml, "UTF-8");
        eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d("XML", "Start document");
            } else if (eventType == XmlPullParser.START_TAG) {
                String tagnm = parser.getName();
                String text ;

                if (tagnm.equals("Result")) {

                        String result = parser.nextText();
                        result = result.trim();
                        if(result.contains("Success"))
                            ps.login_result = true;
                        else ps.login_result = false;

                }else if(tagnm.equals("Area")){
                    care = new Area();
                }else if (tagnm.equals("AreaName")) {
                   text = parser.nextText().trim();
                    if(care!=null)
                        care.name = text;
                }else if(tagnm.equals("AreaID")){
                    text = parser.nextText().trim();
                    if(cdevice!=null)
                        cdevice.room = text;
                } else if(tagnm.equals("RoomID")){
                    text = parser.nextText().trim();
                        if(care!=null)
                            care.id = text;
                }else if (tagnm.equals("Node")) {
                        cdevice = new Device();
                } else if (tagnm.equals("NodeID")) {
                    text = parser.nextText().trim();
                    if(cdevice != null){
                        cdevice.id = text;
                    }
                } else if (tagnm.equals("NodeName")) {
                    text = parser.nextText().trim();
                    if(cdevice != null){
                        cdevice.name = text;
                    }
                } else if (tagnm.equals("NodeType")) {
                    text = parser.nextText().trim();
                    if(cdevice != null){
                        cdevice.type = text;
                    }
                } else if (tagnm.equals("NodeVariables")) {

                } else if (tagnm.equals("VariableName")) {

                } else if (tagnm.equals("VariableValue")) {
                }
            } else if (eventType == XmlPullParser.TEXT) {
                // Log.d("XML", "Text ");
            } else if (eventType == XmlPullParser.END_TAG) {
                String tagnm = parser.getName();
                if(tagnm.equals("Node")){
                    if(cdevice != null)
                        devices.add(cdevice);
                }else if(tagnm.equals("Area")){
                    if(care!=null)
                        rooms.add(care);
                }
            }

            eventType = parser.next();

        }
        ps.room_list = rooms;
        ps.device_list = devices;
        for(int i=0;i<ps.device_list.size();++i)
            Log.d("device_list",ps.device_list.get(i).id);
    }
    public static String makeRuleTimeInfo(Rule crule,ArrayList<String> tri_time) throws IOException,XmlPullParserException{
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter rst = new StringWriter();


        try {
            serializer.setOutput(rst);
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "Rule");
            serializer.startTag(null, "RuleId");
            serializer.text(crule.id);
            serializer.endTag(null, "RuleId");

            serializer.startTag(null, "RuleName");
            serializer.text(crule.name);
            serializer.endTag(null, "RuleName");

            serializer.startTag(null, "timeXML");
            serializer.startTag(null, "time");

            serializer.startTag(null, "hour");
            serializer.text(tri_time.get(0));
            serializer.endTag(null, "hour");

            serializer.startTag(null, "minute");
            serializer.text(tri_time.get(1));
            serializer.endTag(null, "minute");

            serializer.startTag(null, "startTime");
            serializer.text("1388505661");
            serializer.endTag(null, "startTime");
            serializer.startTag(null, "endTime");
            serializer.text("7384233661");
            serializer.endTag(null, "endTime");

            serializer.startTag(null, "repeats");

            serializer.startTag(null, "type");
            serializer.text("weekly");
            serializer.endTag(null, "type");

            serializer.startTag(null, "daysOfWeek");
            serializer.text(tri_time.get(2));
            serializer.endTag(null, "daysOfWeek");

            serializer.endTag(null, "repeats");

            serializer.endTag(null, "time");
            serializer.endTag(null, "timeXML");

            serializer.endTag(null, "Rule");
            serializer.endDocument();
            rst.flush();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String r=rst.toString();
        return r;
    }
    public static String makeRuleConditionInfo(Rule crule, int grp, int cdt) throws IOException,XmlPullParserException{
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter rst = new StringWriter();
        ArrayList<String> condition_info = crule.getGroupCondition(grp,cdt);

        try {
            serializer.setOutput(rst);
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "Rule");
            serializer.startTag(null, "RuleId");
            serializer.text(crule.id);
            serializer.endTag(null, "RuleId");

            serializer.startTag(null, "RuleName");
            serializer.text(crule.name);
            serializer.endTag(null, "RuleName");

            serializer.startTag(null, "RuleContent");
            serializer.startTag(null, "condition");

            serializer.startTag(null, "ConditionID");
            serializer.text(condition_info.get(5));
            serializer.endTag(null, "ConditionID");

            serializer.startTag(null, "NodeID");
            serializer.text(condition_info.get(3));
            serializer.endTag(null, "NodeID");

            serializer.startTag(null, "VarID");
            serializer.text(condition_info.get(4));
            serializer.endTag(null, "VarID");

            serializer.startTag(null, "VarName");
            serializer.text(condition_info.get(0));
            serializer.endTag(null, "VarName");

            serializer.startTag(null, "VarOper");
            serializer.text(condition_info.get(1));
            serializer.endTag(null, "VarOper");

            serializer.startTag(null, "VarValue");
            serializer.text(condition_info.get(2));
            serializer.endTag(null, "VarValue");

            serializer.endTag(null, "condition");
            serializer.endTag(null, "RuleContent");
            serializer.endTag(null, "Rule");
            serializer.endDocument();
            rst.flush();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String r=rst.toString();
        return r;
    }

}
