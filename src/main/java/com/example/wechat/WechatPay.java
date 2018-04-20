package com.example.wechat;

import com.alibaba.fastjson.JSONObject;
import com.example.utils.HttpUtils;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 支付相关方法
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 13:25
 * @Version 1.0
 */
public class WechatPay {

    private static String partnerKey = "bluefocus2018bluefocus2018bluemc";
    private static String partnerId = "1498143142";
    private static String notifyUrl = "http://lonemonkey.xicp.net:48188/wxRecharge/asynNotify.show";
    private static String appId = "wx8ce94c34d4ec8d1d";
    private static String paySignKey = "";

    // 发货通知接口
    private static final String DELIVERNOTIFY_URL = "https://api.weixin.qq.com/pay/delivernotify?access_token=";

    //统一下单
    private static final String UNIFIEDORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";

    //下载对账单
    private static final String DOWNLOADBILL_URL="https://api.mch.weixin.qq.com/pay/downloadbill";

    /**
     * @param billDate
     * @return
    ﻿交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,商品名称,商户数据包,手续费,费率
    `2018-04-18 17:08:23,`wx8ce94c34d4ec8d1d,`1498143142,`0,`WEB,`4200000077201804180423198134,`BV1R180418170252DS,`oa15W04fxj6H8WrwGsUmFysb7Sv0,`NATIVE,`SUCCESS,`BOC_DEBIT,`CNY,`0.01,`0.00,`BlueMC-购买点数1点,`,`0.00000,`0.60%
    总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
    `1,`0.01,`0.00,`0.00,`0.00000

     * @throws Exception
     */
    public static String downloadBill(Date billDate) throws Exception {

//        String notifyUrl="test.bluemc.com";
//        String partnerId="1498143142";
//        String partnerKey="bluefocus2018bluefocus2018bluemc";
//        String appId="wx8ce94c34d4ec8d1d";
        // 公共参数
        DownloadBillRequestData requestData = new DownloadBillRequestData();
        requestData.setAppid(appId);
        requestData.setMch_id(partnerId);
        String nonceStr = RandomStringUtils.random(8, true,true);
        requestData.setNonce_str(nonceStr);
        requestData.setBill_date(billDate);
        requestData.setBill_type("SUCCESS");
//        requestData.setSign_type("MD5");
//        requestData.setTar_type("GZIP");
        String s = createSign(requestData.toMap(), false);
        s+="&key="+partnerKey;

        String signValue = DigestUtils.md5Hex(s).toUpperCase();
        requestData.setSign(signValue);
        String s2=requestData.toXml();
        String s1 = HttpUtils.post(DOWNLOADBILL_URL,s2 );
        return  s1;
    }

    /**
     * 商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值	商品简单描述，该字段请按照规范传递，具体请见参数规定
     * 商品详情	detail	否	String(6000)	 	商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
     * 附加数据	attach	否	String(127)	深圳分店	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
     * 商户订单号	out_trade_no	是	String(32)	20150806125346	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。详见商户订单号
     * 标价币种	fee_type	否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
     * 标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
     * 终端IP	spbill_create_ip	是	String(16)	123.12.12.123	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
     * 商品ID	product_id	否	String(32)	12235413214070356458058	trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
     * -场景信息	scene_info	否	String(256)
     {"store_info" : {
     "id": "SZTX001",
     "name": "腾大餐厅",
     "area_code": "440305",
     "address": "科技园中一路腾讯大厦" }}

     该字段用于上报场景信息，目前支持上报实际门店信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }} ，字段详细说明请点击行前的+展开

     -门店id	id	否	String(32)	SZTX001	门店唯一标识
     -门店名称	name	否	String(64)	腾讯大厦腾大餐厅	门店名称
     -门店行政区划码	area_code	否	String(6)	440305	门店所在地行政区划码，详细见《最新县及县以上行政区划代码》
     -门店详细地址	address	否	String(128)	科技园中一路腾讯大厦	门店详细地址
     * @param params
     * @return
     */
    public static UnifiedorderResponseData getOrderCodeUrl(UnifiedorderRequestData params) throws Exception {

//        String notifyUrl="test.bluemc.com";
//        String partnerId="1498143142";
//        String partnerKey="bluefocus2018bluefocus2018bluemc";
//        String appId="wx8ce94c34d4ec8d1d";
        // 公共参数
        params.setAppid(appId);
        params.setMch_id(partnerId);
        params.setDevice_info("WEB");
        String nonceStr = RandomStringUtils.random(8, true,true);
        params.setNonce_str(nonceStr);
        Date date = new Date();
        params.setTime_start(DateFormatUtils.format(date,"yyyyMMddHHmmss"));
        params.setTime_expire(DateFormatUtils.format(DateUtils.addMinutes(date,30),"yyyyMMddHHmmss"));
        params.setNotify_url(notifyUrl);

        params.setTrade_type("NATIVE");

        String s = createSign(params.toMap(), false);
        s+="&key="+partnerKey;

        String signValue = DigestUtils.md5Hex(s).toUpperCase();
        params.setSign(signValue);
        String s2=params.toXml();
        String s1 = HttpUtils.post(UNIFIEDORDER_URL,s2 );

        XStream xs = XStreamFactory.init(true);
        xs.alias("xml", UnifiedorderResponseData.class);
        UnifiedorderResponseData responseData = (UnifiedorderResponseData) xs.fromXML(s1);
        return responseData;

    }


    /**
     * 构造签名
     * @param params
     * @param encode
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static String createSign(Map<String, Object> params, boolean encode) throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueString, "UTF-8"));
            } else {
                temp.append(valueString);
            }
        }
        return temp.toString();
    }

    /**
     * 支付回调校验签名
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static boolean verifySign(AsynNotifyResultData resultData) throws IOException {


        String sign = resultData.getSign();
        resultData.setSign(null);
        String string1 = createSign(resultData.toMap(), false);
        string1+="&key="+partnerKey;
        String signValue = DigestUtils.md5Hex(string1).toUpperCase();

        return signValue.equalsIgnoreCase(sign)&&resultData.getAppid().equals(appId);
    }


    /**
     * 发货通知签名
     * @param paras
     * @return
     * @throws java.io.UnsupportedEncodingException
     *
     * @参数 appid、appkey、openid、transid、out_trade_no、deliver_timestamp、deliver_status、deliver_msg；
     */
    private static String deliverSign(Map<String, Object> paras) throws UnsupportedEncodingException {
        paras.put("appkey", paySignKey);
        String string1 = createSign(paras, false);
        String paySign = DigestUtils.shaHex(string1);
        return paySign;
    }


    /**
     * 发货通知
     * @param access_token
     * @param openid
     * @param transid
     * @param out_trade_no
     * @return
     * @throws java.io.IOException
     * @throws java.security.NoSuchProviderException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */

    public static boolean delivernotify(String access_token, String openid, String transid, String out_trade_no) throws Exception {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("appid", appId);
        paras.put("openid", openid);
        paras.put("transid", transid);
        paras.put("out_trade_no", out_trade_no);
        paras.put("deliver_timestamp", (System.currentTimeMillis() / 1000) + "");
        paras.put("deliver_status", "1");
        paras.put("deliver_msg", "ok");
        // 签名
        String app_signature = deliverSign(paras);
        paras.put("app_signature", app_signature);
        paras.put("sign_method", "sha1");
        String json = HttpUtils.post(DELIVERNOTIFY_URL.concat(access_token), JSONObject.toJSONString(paras));
        if (StringUtils.isNotBlank(json)) {
            JSONObject object = JSONObject.parseObject(json);
            if (object.containsKey("errcode")) {
                int errcode = object.getIntValue("errcode");
                return errcode == 0;
            }
        }
        return false;
    }
}
