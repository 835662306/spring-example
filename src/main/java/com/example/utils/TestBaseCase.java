package com.example.utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 9:45
 * @Version 1.0
 */
public class TestBaseCase {
    public static WebDriver webdriver;
    //方法描述
    public static String description;
    public Logger logger = Logger.getLogger(TestBaseCase.class);

    @BeforeTest
    @Parameters({"driver", "nodeURL"})
    public void setup(String driver, String nodeURL) throws MalformedURLException{
        logger.info("-------开始执行测试-------");
        if(nodeURL.equals("") || nodeURL.isEmpty()){
            logger.info("读取testng.xml配置的"+driver+"浏览器并将其初始化");
            try {
                webdriver = setDriver(driver);
            } catch (Exception e) {
                logger.error("浏览器环境配置错误");
                e.printStackTrace();
            }
            System.out.println(nodeURL);
            webdriver.manage().window().maximize();
        } else {
            logger.info("读取xml配置：浏览器"+driver+"; gridNodeURL:"+nodeURL);
            try {
                webdriver = setRemoteDriver(driver, nodeURL);
            } catch (Exception e) {
                logger.info("浏览器环境配置错误");
                e.printStackTrace();
            }
            webdriver.manage().window().maximize();
        }
    }

    private WebDriver setRemoteDriver(String driver, String nodeURL) throws MalformedURLException {
        switch (driver)
        {

            case "FirefoxDriver" :
                DesiredCapabilities capabilities=DesiredCapabilities.firefox();
                capabilities.setBrowserName("firefox");
                capabilities.setPlatform(Platform.WINDOWS);
                //driver= new RemoteWebDriver(new URL("http://192.168.0.205:4455/wd/hub"), capabilities);
                webdriver= new RemoteWebDriver(new URL(nodeURL), capabilities);
                break;
            case "ChromeDriver":
                // System.setProperty("webdriver.chrome.driver", "E:\\autotest\\autotmaiton\\resource\\chromedriver.exe");
                //driver=new ChromeDriver();
                DesiredCapabilities dcchorme=DesiredCapabilities.chrome();
                dcchorme.setBrowserName("chrome");
                dcchorme.setVersion("46.0.2490.86 m");
                dcchorme.setPlatform(Platform.WINDOWS);
                webdriver=new RemoteWebDriver(new URL(nodeURL), dcchorme);
                break;
            case "InternetExplorerDriver-8":
                DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
                dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                dc.setCapability("ignoreProtectedModeSettings", true);
                dc.setBrowserName("internet explorer");
                dc.setVersion("8.0.6001.18702");
                dc.setPlatform(Platform.XP);
                webdriver= new RemoteWebDriver(new URL(nodeURL), dc);
                break;
            case "InternetExplorerDriver-9":
                DesiredCapabilities dc2 = DesiredCapabilities.internetExplorer();
                dc2.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                dc2.setCapability("ignoreProtectedModeSettings", true);
                dc2.setBrowserName("internet explorer");
                dc2.setVersion("9.0.8112.16421");
                dc2.setPlatform(Platform.WINDOWS);
                webdriver= new RemoteWebDriver(new URL(nodeURL), dc2);
                //driver=new InternetExplorerDriver(dc2);
                break;
//            case "HtmlUnitDriver":
//                webdriver=new HtmlUnitDriver();
//                break;
            default:
                webdriver=new FirefoxDriver();
                break;
        }
        return webdriver;
    }

    /**
     * 用枚举类型猎取浏览器列表，用于设置浏览器类型的函数参数
     * @param driver
     * @return
     */
    private WebDriver setDriver(String driver) {
        switch (driver){
            case "FireforDriver":
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, "F:\\geckodriver.exe");
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                webdriver = new FirefoxDriver(firefoxProfile);
                break;
            case "ChromeDriver":
                System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "F:\\chromedriver.exe");
                webdriver= new ChromeDriver();
                break;
            default:
                webdriver = new FirefoxDriver();
        }
        return webdriver;
    }

    public void tearDown() {
        webdriver.close();
        webdriver.quit();
        logger.info("-----结束测试，并关闭退出浏览器------");
    }
}
