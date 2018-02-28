package com.example.utils;

import com.example.action.AsserAction;
import com.example.action.ElementAction;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 11:46
 * @Version 1.0
 */
public class TestListener extends TestListenerAdapter {
    
    Logger log = Logger.getLogger(TestListener.class);
    //输出失败结果详情
    public static StringBuffer sb=new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<failed>\n");
    String path="test-output\\failed.xml";
    File file=new File(path);
    FileWriter fileWriter=null;
    //输出成功结果详细信息
    public static StringBuffer sb2=new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<passed>\n");
    String path2="test-output\\passed.xml";
    File file2=new File(path2);
    FileWriter fileWriter2=null;
    FileManger fileManger=new FileManger();
    @Override
    public void onTestStart(ITestResult tr)
    {
        //tr.getTestContext()
        //方法开始前初始化报表描述信息
        AsserAction.errors.clear();
        AsserAction.errorIndex=0;
        AsserAction.messageList.clear();
        ElementAction.noSuchElementExceptions.clear();
        log.info("测试用例:"+tr.getMethod().getDescription()+"---start");
    }
    @Override
    public void onTestFailure( ITestResult  tr)
    {
        //this.handAsserAction(tr);
        TestBaseCase testBaseCase=(TestBaseCase) tr.getInstance();
        WebDriver driver=testBaseCase.webdriver;
        ScreenShot screenShot=new ScreenShot(driver);
        //设置截图名字
        screenShot.setscreenName(tr.getMethod().getDescription()+AsserAction.errorIndex.toString());
        log.error(AsserAction.errorIndex.toString());
        screenShot.takeScreenshot();
        for(int i=0;i<AsserAction.messageList.size();i++)
        {
            if (tr.getParameters().length>0) {
                sb.append("<err_assert_info"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"."+tr.getEndMillis()+"\">\n");
            }
            else {
                sb.append("<err_assert_info"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"\">\n");
            }
            if(AsserAction.messageList.get(i).contains("pass"))
            {
                sb.append("<span class=\"pass_span\">"+AsserAction.messageList.get(i)+"</span></br>\n");
            }
            else if(AsserAction.messageList.get(i).contains("failed"))
            {
                sb.append("<span class=\"err_span\">"+AsserAction.messageList.get(i)+"</span></br>\n");
            }
            else if(AsserAction.messageList.get(i).contains("点击查看大图"))
            {
                sb.append("<span class=\"err_span\">"+AsserAction.messageList.get(i)+"</span>\n");
            }
            sb.append("</err_assert_info>\n");
        }


        for (Exception e : ElementAction.noSuchElementExceptions) {
            StackTraceElement[] errorTraces = e.getStackTrace();
            StackTraceElement[] et = this.getKeyStackTrace(tr, errorTraces);
            if (tr.getParameters().length>0) {
                sb.append("<err_assert_info_StackTrace"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"."+tr.getEndMillis()+"\">\n");
            }
            else {
                sb.append("<err_assert_info_StackTrace"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"\">\n");
            }
            sb.append("<span class=\"err_span\" >"+e.getMessage()+"</span></br>\n");
            for(int i=0;i<et.length;i++)
            {
                sb.append("<span class=\"err_span\">"+et[i].toString()+"</span></br>\n");
            }
            sb.append("</err_assert_info_StackTrace>\n");


        }

        for (Error e : AsserAction.errors) {
            StackTraceElement[] errorTraces = e.getStackTrace();
            StackTraceElement[] et = this.getKeyStackTrace(tr, errorTraces);
            if (tr.getParameters().length>0) {
                sb.append("<err_assert_info_StackTrace"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"."+tr.getEndMillis()+"\">\n");
            }
            else {
                sb.append("<err_assert_info_StackTrace"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"\">\n");
            }
            sb.append("<span class=\"err_span\" >"+e.getMessage()+"</span></br>\n");
            for(int i=0;i<et.length;i++)
            {
                sb.append("<span class=\"err_span\">"+et[i].toString()+"</span></br>\n");
            }

            sb.append("</err_assert_info_StackTrace>\n");


        }
		/*try {
			if(file.exists())
			{
				file.delete();
			}
			fileWriter=new FileWriter(file, true);
			BufferedWriter output = new BufferedWriter(fileWriter);
			output.write(sb.toString()+"</failed>");
			//output.write("</failed>");
			output.flush();
			output.close();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}*/
        if(file.exists())
        {
            file.delete();;
        }
        fileManger.writeWithEncode(file,"utf-8",true,sb.toString()+"</failed>");
        // log.error(sb.toString());
        //this.handAsserAction(tr);
        log.error("测试用例: "+tr.getMethod().getDescription()+"--failed");
        log.info("测试用例:"+tr.getMethod().getDescription()+"---end");
    }
    @Override
    public void onTestSkipped(ITestResult tr) {
        TestBaseCase testBaseCase=(TestBaseCase) tr.getInstance();
        WebDriver driver=testBaseCase.webdriver;
        ScreenShot screenShot=new ScreenShot(driver);
        //设置截图名字
        screenShot.setscreenName(tr.getMethod().getDescription());
        screenShot.takeScreenshot();
        log.warn("测试用例: "+tr.getMethod().getDescription()+"--skipped");
        log.info("测试用例:"+tr.getMethod().getDescription()+"---end");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        for(int i=0;i<AsserAction.messageList.size();i++)
        {
            if (tr.getParameters().length>0) {
                sb2.append("<pass_assert_info"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"."+tr.getEndMillis()+"\">\n");
            }
            else {
                sb2.append("<pass_assert_info"+"   method=\""+tr.getTestClass().getName()+"."+tr.getMethod().getMethodName()+"\">\n");
            }
            sb2.append("<span class=\"pass_span\">"+AsserAction.messageList.get(i)+"</span></br>\n");

            sb2.append("</pass_assert_info>\n");
        }
		
        if(file2.exists())
        {
            file2.delete();;
        }
        fileManger.writeWithEncode(file2,"utf-8",true,sb2.toString()+"</passed>");
        log.info("测试用例: "+tr.getMethod().getDescription()+"--passed");
        log.info("测试用例:"+tr.getMethod().getDescription()+"---end");

    }
    private StackTraceElement[] getKeyStackTrace(ITestResult tr, StackTraceElement[] stackTraceElements){
        List<StackTraceElement> ets = new ArrayList<StackTraceElement>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if(stackTraceElement.getClassName().equals(tr.getTestClass().getName())){
                ets.add(stackTraceElement);
            }
        }
        StackTraceElement[] et = new StackTraceElement[ets.size()];
        for (int i = 0; i < et.length; i++) {
            et[i] = ets.get(i);
        }
        return et;
    }
}
