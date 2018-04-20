package com.example.alipay;

import com.alibaba.fastjson.JSON;
import com.example.alipay.config.AlipayConfig;
import com.example.alipay.util.AlipayNotify;
import com.example.alipay.util.AlipaySubmit;
import com.example.bean.User;
import com.example.controller.WxPayController;
import com.example.utils.ActionResult;
import com.example.utils.MemberBenefitCode;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**用户充值
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/4/20 14:28
 * @Version 1.0
 */
@Controller
@RequestMapping("/recharge")
public class AlipayController {
    private static Logger log = LoggerFactory.getLogger(WxPayController.class);
    /**
     * 创建充值记录跳转支付宝付款页面
     * @param request
     * @param point
     * @param amount
     * @param givingPoint
     * @param welfareCode
     * @param id
     * @return
     */
    @RequestMapping("/alipayapi.do")
    public ModelAndView alipayapi(HttpServletRequest request, int point, BigDecimal amount,
                                  @RequestParam(defaultValue = "0",required = false)Integer givingPoint,
                                  MemberBenefitCode welfareCode, Long id) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView();

        ActionResult actionResult = new ActionResult();
        if(Boolean.parseBoolean(actionResult.get("success")+"")){
            try{
                    Map<String, String> sParaTemp = createPayParamMap();
                    //把请求参数打包成数组
                    //建立请求
                    String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
                    view.setViewName("/alipayapi");
                    view.addObject("html", sHtmlText);
                    view.addObject("tradeNo","");
                    //todo:自己支付后的逻辑
//                    rechargeService.saveRechargeLog(dto.getTradeNo(),"REQ", AlipayCore.createLinkString(sParaTemp));
            }catch (Exception e){
                log.error("购买点数错误",e);
                view.addObject("error","购买点数错误,创建订单失败");
            }
        }else{
            log.error("购买点数与应付款金额不匹配");
            view.addObject("html", actionResult.get("msg"));
        }
        return view;
    }



    /**
     * 支付宝付款接口参数
     * @param
     * @return
     */
    private Map<String, String> createPayParamMap(/*RechargeRecordDto dto*/) {
        Map<String, String> sParaTemp = new HashMap();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
        //需要的参数
//        sParaTemp.put("total_fee", dto.getTotalAmount() + "");
//        sParaTemp.put("out_trade_no", dto.getTradeNo());
//        sParaTemp.put("subject", "购买点数："+dto.getPoint()+"点");
        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");
        return sParaTemp;
    }
    /**
     * 支付宝支付完成同步回调地址
     * 外网必须可以访问，不需要用户登录
     * @param request
     * @return
     */
    @RequestMapping("/returnUrl.show")
    public ModelAndView returnUrl(HttpServletRequest request){
        //获取支付宝GET过来反馈信息
        Map<String,String> params = getParams(request);
        log.info("支付宝支付完成同步回调,参数："+ JSON.toJSONString(params));
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
        //交易状态
        String trade_status = request.getParameter("trade_status");
        //计算得出通知验证结果
        boolean verify_result = false;
        try{
            verify_result = AlipayNotify.verify(params);
        }catch (Exception e){
            //签名错误保存日志
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"ERR","同步验证签名错误:"+e);
        }
        ModelAndView view = new ModelAndView("redirect:/usercenter/myhomepage.do");
        //认证成功保存日志
//        rechargeService.saveRechargeLog(params.get("out_trade_no"), "RES", "支付宝同步(returnUrl)返回参数：" + AlipayCore.createLinkString(params));
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
//                rechargeService.finishTrade(params);
            }
//            view.addObject("success",true);
            //////////////////////////////////////////////////////////////////////////////////////////
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"NORMAL","阿里同步返回结果，交易完成");
        }else{
//            view.addObject("success", false);
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"ERR","同步验证签名失败");
        }
        return view;
    }

    /**
     * request参数转换
     * @param request
     * @return
     */
    private Map<String,String> getParams(HttpServletRequest request){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 支付宝异步通知
     * 外网必须可以访问，不需要用户登录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/notifyUrl.show")
    public String notifyUrl(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = getParams(request);
        log.info("支付宝异步通知,参数："+ JSON.toJSONString(params));
        //交易状态
        String trade_status = request.getParameter("trade_status");
//        rechargeService.saveRechargeLog(params.get("out_trade_no"),"RES","支付宝异步(notifyUrl)返回参数："+AlipayCore.createLinkString(params));
        boolean verify_result = false;
        try{
            verify_result = AlipayNotify.verify(params);
        }catch (Exception e){
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"ERR","异步验证签名错误:"+e);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        String returnStr = "fail";
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
//                rechargeService.finishTrade(params);
            } else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
//                rechargeService.finishTrade(params);
            }
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//            out.print("success");	//请不要修改或删除
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"NORMAL","阿里异步返回结果，交易完成");
            returnStr = "success";
            //////////////////////////////////////////////////////////////////////////////////////////
        }else{
//            rechargeService.saveRechargeLog(params.get("out_trade_no"),"ERR","异步验证签名失败");
        }
        return returnStr;
    }

    /**
     * 判断支付是否成功
     * @param tradeNo
     * @return
     */
    @RequestMapping("/isPay.do")
    @ResponseBody
    public Map<String,Object> isPaySuccess(String tradeNo){
        Map<String,Object> map=new HashMap<>();
//        RechargeRecordDto recordDto=rechargeService.findRecordByTradeNo(tradeNo);
//        if (recordDto.getStatus()==1){
//            map.put("success",true);
//            MemberLevelChangeRecord changeRecord=changeRecordService.findByTradeNo(tradeNo);
//            map.put("changeRecord",changeRecord);
//        }else {
//            map.put("success",false);
//        }
        return map;
    }

    /**
     * 对账单
     * @return
     */
    @RequestMapping("/checkingBill.do")
    public String checkingBill(ModelMap map){
        map.put("currDay", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        return "sysManage/rechargeBill/checkingBill";
    }

}
