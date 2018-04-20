package com.example.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 功能：微信异步/同步通知返回给微信的报文
 * 作者：Zhang_XinGang
 * 时间：2017/5/3 16:55
 */
@XStreamAlias("xml")
public class NotifyResultData {
    /**
     * 返回状态码 SUCCESS/FAIL
     */
    private String return_code;
    /**
     * 返回信息 返回信息，如非空，为错误原因：
     签名失败
     */
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
    public String toXml(){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        String xmlStr = xstream.toXML(this);
        //String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xmlStr = xmlStr;
        return xmlStr;
    }
}
