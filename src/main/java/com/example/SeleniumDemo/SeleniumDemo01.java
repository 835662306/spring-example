package com.example.SeleniumDemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/27 15:12
 * @Version 1.0
 */
public class SeleniumDemo01 {
    private WebDriver driver = null;
    @BeforeAll
    public void before() throws Exception{

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "F:\\chromedriver.exe");
        //System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY, "F:\\Firefox\\firefox.exe");
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\sssssa\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");

        driver = new ChromeDriver();
        /*/
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "C:\\Users\\chromedriver_win32\\chromedriver.exe");
//        System.setProperty(ChromeDriver.SystemProperty.BROWSER_BINARY, "D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
        driver = new ChromeDriver();
        /**/
        //最大化
        driver.manage().window().maximize();
    }

    @Test
    public void run() throws InterruptedException, IOException {
        WebDriver.Options manage = driver.manage();
        WebDriver.Timeouts timeouts = manage.timeouts();
        timeouts.implicitlyWait(40, TimeUnit.SECONDS);

        driver.get("https://www.panda.tv/");
        timeouts.implicitlyWait(40, TimeUnit.SECONDS);
        //*[@id="nav"]/div[1]/ul/li[1]
        driver=driver.switchTo().defaultContent();
        WebDriverWait webDriverWait = new WebDriverWait(driver,10,1000);
        WebElement product0 = driver.findElement(By.xpath("//*[@id=\"panda_header\"]/div/div/div[2]/ul/li[2]"));
        product0.click();

        WebElement product1 = driver.findElement(By.xpath("//*[@id=\"later-play-list\"]/li[8]/a/div[1]"));
        product1.click();

        //*[@id="side-tools-bar"]/div/div[1]/div[3]/div/a[1]
        WebElement product3 = webDriverWait.until(webDriver -> webDriver.findElement(By.xpath("//*[@id=\"side-tools-bar\"]/div/div[1]/div[3]/div/a[1]")));
        product3.click();

        WebElement loginName = driver.findElement(By.className("ruc-input-login-name"));
        loginName.sendKeys("15706017036");
        WebElement loginPass = driver.findElement(By.className("ruc-input-login-passport"));
        loginPass.sendKeys("6962641gxj?");
        WebElement click = driver.findElement(By.className("login-button-container"));
        click.click();

        Thread.sleep(10000);
        WebElement sendMs = driver.findElement(By.className("ruc-send-voice-verify-btn"));
        sendMs.click();

        Thread.sleep(15000);

        WebElement login = driver.findElement(By.className("login-button-container"));
        login.click();
        Thread.sleep(5000);
        WebElement pro = driver.findElement(By.xpath("//*[@id=\"side-tools-bar\"]/div/div[1]/div[1]/div[3]/a[1]"));
        pro.click();

    }
}
