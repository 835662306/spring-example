package com.example.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.InputStream;

/**
 * 功能：统一下单接口回参对象
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 13:33
 * @Version 1.0
 */
@XStreamAlias("xml")
public class UnifiedorderResponseData {
    /**
     * 返回状态码 SUCCESS/FAIL
     */
    private String return_code;
    /**
     * 返回信息 返回信息，如非空，为错误原因
     */
    private String return_msg;
    /**
     * 公众账号ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 设备号
     */
    private String device_info;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;
    /**
     * 业务结果 SUCCESS/FAIL
     */
    private String result_code;
    /**
     * 错误代码
     */
    private String err_code;
    /**
     * 错误代码描述
     */
    private String err_code_des;
    /**
     * 交易类型
     */
    private String trade_type;
    /**
     * 预支付交易会话标识
     */
    private String prepay_id;
    /**
     * 二维码链接 trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
     */
    private String code_url;

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

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }
    public String toXml(){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        String xmlStr = xstream.toXML(this);
        String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xmlStr = res+xmlStr;
        return xmlStr;
    }
    public static UnifiedorderResponseData xmlToBean(String xmlStr){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        //xstream.alias("xml", UnifiedorderRequestData.class);
        xstream.setClassLoader(UnifiedorderResponseData.class.getClassLoader());
        xstream.processAnnotations(UnifiedorderResponseData.class);
        UnifiedorderResponseData data2 = (UnifiedorderResponseData)xstream.fromXML(xmlStr);
        return data2;
    }
    public static UnifiedorderResponseData xmlToBean(InputStream is){
        UnifiedorderResponseData data2 = null;
        try {
            XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            xstream.autodetectAnnotations(true);
            xstream.setClassLoader(UnifiedorderResponseData.class.getClassLoader());
            xstream.processAnnotations(UnifiedorderResponseData.class);
            //xstream.alias("xml", UnifiedorderRequestData.class);
            data2 = (UnifiedorderResponseData)xstream.fromXML(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return data2;
    }

    public static void main(String[] a){
        String xml = "<xml><return_code>FAIL</return_code>\n" +
                "<return_msg>商户号mch_id或sub_mch_id不存在</return_msg>\n" +
                "</xml>";
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        xstream.setClassLoader(UnifiedorderResponseData.class.getClassLoader());
        xstream.processAnnotations(UnifiedorderResponseData.class);
        UnifiedorderResponseData data2 = (UnifiedorderResponseData)xstream.fromXML(xml);
        System.out.println(data2.getReturn_code());
        /*UnifiedorderResponseData responseData = new UnifiedorderResponseData();
        responseData.setReturn_code("FAIL");
        responseData.setReturn_msg("商户号mch_id或sub_mch_id不存在");

        String xml = responseData.toXml();
        System.out.println(xml);

        UnifiedorderResponseData response = UnifiedorderResponseData.xmlToBean(xml);
        System.out.println(response.getReturn_code());*/
    }
}
