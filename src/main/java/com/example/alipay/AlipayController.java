package com.example.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.example.alipay.config.AlipayConfig;
import com.example.alipay.util.AlipayNotify;
import com.example.alipay.util.AlipaySubmit;
import com.example.bean.User;
import com.example.controller.WxPayController;
import com.example.utils.ActionResult;
import com.example.utils.DownLoadFile;
import com.example.utils.MemberBenefitCode;
import com.example.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
     * 获取对账单下载地址
     * 一般写在任务中
     * @param date
     * @return
     * @throws AlipayApiException
     */
    public static String getBillDownloadurlUrl(Date date){
        Assert.notNull(date);

        String billDownloadUrl=null;
        try {
            //*/
            AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                    "2017020605539373",
                    "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQE2KFpoAgLo4mbmAu8BficEeAzIyZNxJyiZx5/ESCyFIe6gx8cRHDu8bXUMxqv6lwmRV3hicD+HDFlUURGosqQkoA+RiZpA3x2ZCyuv5H9A/F+RBqok9dzrPjoxyZXO+q4j9wtb1KmGYWhv+ZkZWQIjItwsCy8DN9xzF1BbVCAB7Ra3mWebFtNryWUkedJiwW0551qgNVTSD/tV2spCGcfyOp8IPs1prAP6huYU8ayj9jlGeBSkM/uM322v4tDsEQ04VjqVFvCQ5wsnZ7H+/gFtNBnGfkk6dpjaAm59VLT6cNE+vdxt5Zuet0iLJ0oovj9ul/hSF+Kq4XQIMkpiUpAgMBAAECggEAXBIXPuOlM2us6cBVdQl3qfwopZWSMANypYFtXsMooQ8U866MJiY3vQbNziFTUNCEnnElt8kiO7ZTkuCOT5zP3ESaeD63Ss/9HVX001MOVrsQLWq54/svjvoeGVXOW+95NZH6CrQJcJctfi3tP506n+5KdNXlQTShIzrdTzRDnhXxL5NiFjDbhpgkpY526EHlpEMu89SdUUKX7bFXcqLKHV8pzSnuXtMeP2b+vqmFYG0xVAUY/R3v6e8QKZXgVW8Aazw6MKLVvWmPeqduoSNe4kbhUFnYBYZoGDwh6LbxwJ6Xw7SgEz9sd02Ev++rY/OPnlIEy5mdhuwaKzes/JKuAQKBgQDR0jdST/sJuWpQYZQc2UwQakzSaPPSKADEUeoCwqCaXURtDHTQ1Ti5oaAxdaBY0E99+yIdZbZXZz4UR3iJXcxoVKtE3xY2qXe8xRw/DTI7Yz1XGQU0MqWfZazD+GY5Rp9Pmv4GJTlRmri7fOo4IMr/OtPMmR46GWEP5L+ihBHsaQKBgQCvyO1GNrAhwU8uV7ltTtvc7wUfvJEFTtHx14RbX2j8bgmg4g41TB1Osj1bd5/Wf/X+BNC1yQQWJTK/17Ppp8Uem/btXQz5dPDxzLL0vnlETMtIK3scyf4JS3xOwaFcf9FtL6oDFh7zcFFabaymnxTtoxs1aeeutWuq2rvyrRZawQKBgQCD+m2P2f02/ajwzKAEkW41+Rc/VoLfUwhAdKH0gIXS8w2iZi5oWWYn5ZFE6w8kLkuCG+A2i47pZWh4Cwi3pwd3LKLaXFS1p11IoNeGlX9eOasQyQ0r6xugqqzES8/JATIeOYjFRs7KFL9UN1uAWKg3aMJmtH194A7cl28vA1He2QKBgGua/dR7abpgEU53GOVW3rQSBPr2fXfYViBLI5SjhSrxWSeI+dWacF5aMcEqK0gtMLJ81B5TnRLJVpWlP1cLAlnIc5G94lFaSpxaCDpV1vn+YHofU8+9vqqF8ORtF4/+Fn9WLCaThgLTgJlwhb3BqBgIPoGmANMr72q5V1AosISBAoGACjY3T5+2W/8uZUMuTGPZH11N+4PofeDBTrKiq9TEcAYAaiMhrdtN5RVZeur0X1BOwJRGMF/+igJURVgCovtvI8QSK/DcrtnDCitb9qaFe4dbWLjL2zJA5mNUXF2gfEyiQgTzeiD4CzmHNdNhyGIFP0W+oyrqSK//GdsvUBcVNl8=",
                    "json","GBK",
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwZn2LO+2Ub+aZzwUPbi+MXYWfS3gD39ItvSqQmhBKrTrzXZq0N2EYzgMW3LFnuhInU5JKluUwltVHIlwFhawsT6EjPXXfuVQcdZgfz4DcaXXm/XWjvoQjcExyyuPvlbJxnokLrka+r5tUIYFsbJMfejU/7xEgdn/IKTWpZQe4C5itcruXqD08Wlfy91/AJInMKpNSASdjX2317TFoor2vwA606In+0W+GtN6uARONzbe/8Lj+Dgs9Wq8TaoZi2Ra5xHaaQCKU9gle79+2aC49ZD4ksIxoPpFyX65m959cZaI7Tqk89gQbW++l2hcL6/tmj8Gv+71RL5EI7I5v3ot1QIDAQAB",
                    "RSA2");
            AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
            request.setBizContent("{\"bill_type\":\"trade\",\"bill_date\":\""+ DateFormatUtils.format(date,"yyyy-MM-dd")+"\"}");
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("获取对账单下载地址调用成功");
                billDownloadUrl = response.getBillDownloadUrl();
            } else {
                log.info("获取对账单下载地址调用失败");
            }
        } catch (AlipayApiException e) {
            log.error("获取对账单下载地址调用失败", e);
        }
        return billDownloadUrl;
    }

    private static String zipPath="download/bill_zip";
    private static String unZipPath="download/bills";

    private static String alipay_account="20885219024064740156";
    public void getBill() {
        //下载前一天的对账单
        Date date = DateUtils.addDays(new Date(), -1);
        String billDownloadurlUrl = getBillDownloadurlUrl(date);
        String dateStr= DateFormatUtils.format(date,"yyyyMMdd");
        if(StringUtils.isNotBlank(billDownloadurlUrl)){
            String path = Thread.currentThread().getContextClassLoader().getResource("/").getPath().replace("classes/","");
            String localFilePath = path + zipPath+"/"+alipay_account+"_" + dateStr + ".csv.zip";
            DownLoadFile.downloadFile(billDownloadurlUrl, localFilePath);

            ZipUtil.unzip(localFilePath, path + unZipPath);

//            alipayBillService.importBillFromDownloadCsvFile( path+unZipPath+"/"+alipay_account+"_"+dateStr+"_业务明细.csv");
        }
    }

}
