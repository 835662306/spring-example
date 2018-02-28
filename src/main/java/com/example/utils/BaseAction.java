package com.example.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 10:30
 * @Version 1.0
 */
public class BaseAction extends TestBaseCase{

    protected HashMap<String, Locator> locatorHashMap;
    public  String path = null;
    public InputStream path_inputStream_1;
    public InputStream path_inputStream_2;

    Logger logger = Logger.getLogger(BaseAction.class);
    public void setXmlObjectPath(String path){
        this.path = path;
    }

    public void setXmlObjectPathForLocator(InputStream path_inputStream){this.path_inputStream_1 = path_inputStream;}
    public void setXmlObjectPathForPageURL(InputStream path_inputStream){this.path_inputStream_2 = path_inputStream;}

    public BaseAction(){}

    public void getLocatorMap() {
        XmlReadUtil xmlReadUtil = new XmlReadUtil();
        try {
            if((path==null || path.isEmpty())){
                locatorHashMap = xmlReadUtil.readXMLDocument(path_inputStream_1, this.getClass().getCanonicalName());
            }else{
                locatorHashMap = xmlReadUtil.readXMLDocument(path, this.getClass().getCanonicalName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static By getBy(Locator.ByType byType, Locator locator){
        switch (byType) {
            case id:
                return By.id(locator.getElement());
            case cssSelector:
                return By.cssSelector(locator.getElement());
            case name:
                return By.name(locator.getElement());
            case xpath:
                return By.xpath(locator.getElement());
            case className:
                return By.className(locator.getElement());
            case tagName:
                return By.tagName(locator.getElement());
            case linkText:
                return By.linkText(locator.getElement());
            case partialLinkText:
                return By.partialLinkText(locator.getElement());
            //return null也可以放到switch外面
            default:
                return null;
        }
    }

    public Locator getLocator(String locatorName){
        Locator locator = locatorHashMap.get(locatorName);
        if(locator == null){
           logger.error("没有找到"+locatorName+"页面元素");
        }
        return locator;
    }

    public String getPageURL(){
        String pageURL = null;
        try {
            if(path == null || path.isEmpty()){
                pageURL = XmlReadUtil.getXmlPageURL(path_inputStream_1, this.getClass().getCanonicalName());
            }else{
                pageURL = XmlReadUtil.getXmlPageURL(path, this.getClass().getCanonicalName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageURL;
    }

    /**
     * 打开浏览器
     * @param url
     */
    public  void open(String url){
        webdriver.navigate().to(url);
        logger.info("打开浏览器，访问"+url+"网址");
    }

    public void close(){
        webdriver.close();
        logger.info("关闭浏览器窗口");
    }

    public void quit(){
        webdriver.quit();
        logger.info("退出浏览器");
    }

    public void forward() {
        webdriver.navigate().forward();
        logger.info("浏览器前进");
    }

    public void back() {
        webdriver.navigate().back();
        logger.info("浏览器后退");
    }

    public void refresh(){
        webdriver.navigate().refresh();
        logger.info("浏览器刷新");
    }

    public WebElement findElement(final Locator locator) throws IOException
    {
        /**
         * 查找某个元素等待几秒
         */
        Waitformax(Integer.valueOf(locator.getWaitSec()));
        WebElement webElement;
        webElement=getElement(locator);
        return webElement;


    }

    public void Waitformax(int t) {
        webdriver.manage().timeouts().implicitlyWait(t, TimeUnit.SECONDS);
    }

    /**
     * 通过定位信息获取元素
     * @param locator
     * @return
     * @throws NoSuchElementException
     */
    public WebElement getElement(Locator locator) {
        /**
         * locator.getElement(),获取对象库对象定位信息
         */
        //locator=getLocator(locatorMap.get(key));
        WebElement webElement;
        switch (locator.getBy())
        {
            case xpath :
                //log.info("find element By xpath");
                webElement=webdriver.findElement(By.xpath(locator.getElement()));
                /**
                 * 出现找不到元素的时候，记录日志文件
                 */
                break;
            case id:
                //log.info("find element By xpath");
                webElement=webdriver.findElement(By.id(locator.getElement()));
                break;
            case cssSelector:
                //log.info("find element By cssSelector");
                webElement=webdriver.findElement(By.cssSelector(locator.getElement()));
                break;
            case name:
                //log.info("find element By name");
                webElement=webdriver.findElement(By.name(locator.getElement()));
                break;
            case className:
                //log.info("find element By className");
                webElement=webdriver.findElement(By.className(locator.getElement()));
                break;
            case linkText:
                //log.info("find element By linkText");
                webElement=webdriver.findElement(By.linkText(locator.getElement()));
                break;
            case partialLinkText:
                //log.info("find element By partialLinkText");
                webElement=webdriver.findElement(By.partialLinkText(locator.getElement()));
                break;
            case tagName:
                //log.info("find element By tagName");
                webElement=webdriver.findElement(By.partialLinkText(locator.getElement()));
                break;
            default :
                //log.info("find element By xpath");
                webElement=webdriver.findElement(By.xpath(locator.getElement()));
                break;

        }
        return webElement;
    }
}
