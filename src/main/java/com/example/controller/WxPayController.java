package com.example.controller;

import com.example.bean.User;
import com.example.utils.ActionResult;
import com.example.utils.MemberBenefitCode;
import com.example.wechat.*;
import com.example.wechat.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

/**
 * weixin支付controller
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 13:41
 * @Version 1.0
 */
@Controller
@RequestMapping("/wechat")
public class WxPayController {
    private static Logger log = LoggerFactory.getLogger(WxPayController.class);


    /**
     * 微信支付统一下单，拿到生成二维码的code_url,页面通过code_url生成二维码，供用户扫描
     * @param request
     * @param point
     * @param amount
     * @param givingPoint
     * @param welfareCode
     * @param id
     * @return
     */
    @RequestMapping(value = "/createOrder.do")
    @ResponseBody
    public ActionResult createOrder(HttpServletRequest request, int point, BigDecimal amount,
                                    @RequestParam(defaultValue = "0",required = false)Integer givingPoint,
                                    MemberBenefitCode welfareCode, Long id){
        User user = (User) request.getSession().getAttribute("user");

        //判断用户逻辑
        ActionResult actionResult = new ActionResult();
        if(Boolean.parseBoolean(actionResult.get("success")+"")){
            try{
                //TODO houfengli 2018/4/18 9:26
//                    rechargeService.saveRechargeLog(dto.getTradeNo(),"REQ", AlipayCore.createLinkString(sParaTemp));
                UnifiedorderRequestData requestData = new UnifiedorderRequestData();
                requestData.setBody(String.format("BlueMC-购买点数%s点",point));
                requestData.setDetail(requestData.getBody());
                requestData.setOut_trade_no("");
                //单位：分
                requestData.setTotal_fee((amount.multiply(new BigDecimal(100)).intValue()));
                //此处需要部署项目的ip
//                String ip = Utils.getRemoteIp(request);
                String ip="192.168.102.36";
                requestData.setSpbill_create_ip(ip);
                //请求微信，统一下单接口
                UnifiedorderResponseData orderCodeUrl = WechatPay.getOrderCodeUrl(requestData);
                if(null!=orderCodeUrl&&"SUCCESS".equals(orderCodeUrl.getReturn_code())){
                    //保存订单记录
//                    rechargeService.saveRecord(dto);
                    actionResult.put("code_url",orderCodeUrl.getCode_url());
                }else{
                    actionResult.setSuccess(false);
                    actionResult.setMsg(orderCodeUrl.getReturn_msg());
                }
            }catch (Exception e){
                actionResult.setSuccess(false);
                actionResult.setMsg("购买点数错误,创建订单失败");
                log.error("购买点数错误",e);
            }
        }else{
            actionResult.setSuccess(false);
            actionResult.setMsg("购买点数与应付款金额不匹配");
        }
        return actionResult;
    }

    /**
     * 接收处理微信支付异步通知
     * @param request
     * <xml><appid><![CDATA[wx8ce94c34d4ec8d1d]]></appid>
    <bank_type><![CDATA[BOC_DEBIT]]></bank_type>
    <cash_fee><![CDATA[1]]></cash_fee>
    <device_info><![CDATA[WEB]]></device_info>
    <fee_type><![CDATA[CNY]]></fee_type>
    <is_subscribe><![CDATA[Y]]></is_subscribe>
    <mch_id><![CDATA[1498143142]]></mch_id>
    <nonce_str><![CDATA[fS2aBXas]]></nonce_str>
    <openid><![CDATA[oa15W04fxj6H8WrwGsUmFysb7Sv0]]></openid>
    <out_trade_no><![CDATA[BV1R180418170252DS]]></out_trade_no>
    <result_code><![CDATA[SUCCESS]]></result_code>
    <return_code><![CDATA[SUCCESS]]></return_code>
    <sign><![CDATA[95C976749A30048003210D87FB5DDBCF]]></sign>
    <time_end><![CDATA[20180418170823]]></time_end>
    <total_fee>1</total_fee>
    <trade_type><![CDATA[NATIVE]]></trade_type>
    <transaction_id><![CDATA[4200000077201804180423198134]]></transaction_id>
    </xml>
     * @return
     */
    @RequestMapping(value = "/asynNotify.show")
    @ResponseBody
    public String weChatNotify(HttpServletRequest request) {
        NotifyResultData notifyResultData = new NotifyResultData();
        try {
            InputStream is = request.getInputStream();
            String xmlString =  Tools.inputStream2String(is);
            log.info("weChat pay notify xml : " +xmlString);
            AsynNotifyResultData asynNotifyResultData = AsynNotifyResultData.xmlToBean(xmlString);
            if(asynNotifyResultData == null||
                    asynNotifyResultData.getReturn_code().trim().equals("FAIL")||
                    asynNotifyResultData.getResult_code().trim().equals("FAIL")){
                notifyResultData.setReturn_code("FAIL");
                notifyResultData.setReturn_msg("处理失败");
                return notifyResultData.toXml();
            }
            Long orderId = null;
            if(StringUtils.isEmpty(asynNotifyResultData.getOut_trade_no())){
                log.error(notifyResultData.toXml());
                notifyResultData.setReturn_code("FAIL");
                notifyResultData.setReturn_msg("商家订单号为空");
                return notifyResultData.toXml();
            }
            //校验报文签名
            if(!WechatPay.verifySign(asynNotifyResultData)){
                log.error("校验报文签名不合法："+notifyResultData.toXml());
                notifyResultData.setReturn_code("FAIL");
                notifyResultData.setReturn_msg("报文签名不合法");
                return notifyResultData.toXml();
            }
            //校验通过，处理业务逻辑,微信支付完成
            //rechargeService.finishTradeOfWeiPay(asynNotifyResultData);

            notifyResultData.setReturn_code("SUCCESS");
            notifyResultData.setReturn_msg("OK");
        } catch (Exception e) {
            log.error("接收处理微信支付异步通知异常",e);
            notifyResultData.setReturn_code("FAIL");
            notifyResultData.setReturn_msg("处理失败");
        }
        return notifyResultData.toXml();
    }

    /**
     * 下载对账单

     * @return
     */
    @RequestMapping(value = "/downloadBill.do")
    @ResponseBody
    public String downloadBill(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        try {
            if(null==date)date=new Date();

            date = DateUtils.addDays(date, -2);
            String s = WechatPay.downloadBill(date);
            return s;
        } catch (Exception e) {
            log.error("微信支付下载对账单异常",e);
        }
        return "下载对账单异常";
    }
}
