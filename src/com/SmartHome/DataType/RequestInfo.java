package com.SmartHome.DataType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by p on 14-4-23.
 */
public class RequestInfo {

    URL url;
    String xml;
    public RequestInfo(String url) throws MalformedURLException {
        this.url=new URL(url);
        xml=null;
    }
    public RequestInfo(String url,String xml) throws MalformedURLException{
        this.url=new URL(url);
        this.xml=xml;
    }
    public URL getUrl(){
        return url;
    }
    public String getXml(){
        return xml;
    }
    public boolean hasXml(){
        if(xml==null)return false;
        else return true;
    }
}