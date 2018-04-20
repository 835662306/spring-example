package com.example.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：异步通知结果实体类
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 13:37
 * @Version 1.0
 */
@XStreamAlias("xml")
public class AsynNotifyResultData {
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
     * 签名方式
     */
    private String sign_type;
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
     * 用户标识
     */
    private String openid;
    /**
     * 是否关注公众账号 Y-关注，N-未关注
     */
    private String is_subscribe;
    /**
     * 交易类型
     */
    private String trade_type;
    /**
     * 付款银行
     */
    private String bank_type;
    /**
     * 订单金额 订单总金额，单位为分
     */
    private Integer total_fee;
    /**
     * 应结订单金额 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private Integer settlement_total_fee;
    /**
     * 货币种类
     */
    private String fee_type;
    /**
     * 现金支付金额 现金支付金额订单现金支付金额
     */
    private Integer cash_fee;
    /**
     * 现金支付货币类型
     */
    private String cash_fee_type;
    /**
     * 总代金券金额 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额
     */
    private Integer coupon_fee;
    /**
     * 代金券使用数量
     */
    private Integer coupon_count;

    /*代金券类型	coupon_type_$n	否	Int	CASH
    代金券ID	coupon_id_$n	否	String(20)	10000	代金券ID,$n为下标，从0开始编号
    单个代金券支付金额	coupon_fee_$n*/
    /**
     * 微信支付订单号
     */
    private String transaction_id;
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 商家数据包
     */
    private String attach;
    /**
     * 支付完成时间 支付完成时间，格式为yyyyMMddHHmmss
     */
    private String time_end;

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

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public void setIs_subscribe(String is_subscribe) {
        this.is_subscribe = is_subscribe;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(Integer settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public Integer getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(Integer cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public Integer getCoupon_fee() {
        return coupon_fee;
    }

    public void setCoupon_fee(Integer coupon_fee) {
        this.coupon_fee = coupon_fee;
    }

    public Integer getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(Integer coupon_count) {
        this.coupon_count = coupon_count;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
    public String toXml(){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        String xmlStr = xstream.toXML(this);
        String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xmlStr = res+xmlStr;
        return xmlStr;
    }
    public static AsynNotifyResultData xmlToBean(String xmlStr){
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.autodetectAnnotations(true);
        //xstream.alias("xml", UnifiedorderRequestData.class);
        xstream.setClassLoader(AsynNotifyResultData.class.getClassLoader());
        xstream.processAnnotations(AsynNotifyResultData.class);
        AsynNotifyResultData data2 = (AsynNotifyResultData)xstream.fromXML(xmlStr);
        return data2;
    }
    public static AsynNotifyResultData xmlToBean(InputStream is){
        AsynNotifyResultData data2 = null;
        try {
            XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            xstream.autodetectAnnotations(true);
            xstream.setClassLoader(AsynNotifyResultData.class.getClassLoader());
            xstream.processAnnotations(AsynNotifyResultData.class);
            data2 = (AsynNotifyResultData)xstream.fromXML(is);
            is.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return data2;
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
