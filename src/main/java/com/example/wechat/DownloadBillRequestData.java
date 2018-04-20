package com.example.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：下载对账单接口入参对象
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 13:28
 * @Version 1.0
 */
public class DownloadBillRequestData {
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 设备号
     */
    private String device_info="WEB";
    /**
     * 签名
     */
    private String sign;
    /**
     * 签名类型
     */
    private String sign_type;

    /**
     *对账单日期
     */
    private String bill_date;
    /**
     *账单类型
     * ALL，返回当日所有订单信息，默认值

     SUCCESS，返回当日成功支付的订单

     REFUND，返回当日退款订单

     RECHARGE_REFUND，返回当日充值退款订单
     */
    private String bill_type;

    /**
     * 压缩账单
     *
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式
     */
    private String tar_type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getBill_date() {
        return this.bill_date;
    }

    public void setBill_date(Date bill_date) {
        if(null==bill_date){
            bill_date=new Date();
        }
        this.bill_date = DateFormatUtils.format(bill_date,"yyyyMMdd");
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getTar_type() {
        return tar_type;
    }

    public void setTar_type(String tar_type) {
        this.tar_type = tar_type;
    }

    public String toXml(){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        String xmlStr = xstream.toXML(this);
        String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xmlStr = res+xmlStr;
        //log.info("UnifiedorderRequestData="+xmlStr);
        return xmlStr;
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //log.info("map:"+map);
        return map;
    }
}
