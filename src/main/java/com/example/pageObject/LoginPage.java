package com.example.pageObject;

import com.example.utils.BaseAction;
import com.example.utils.Locator;

import java.io.IOException;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 11:19
 * @Version 1.0
 */
public class LoginPage extends BaseAction{

    private String path = "src/main/resources/UILibrary.xml";

    public LoginPage() {
        //工程内读取对象文件
        setXmlObjectPath(path);
        getLocatorMap();
    }

    /**
     * 账户登录
     * @return
     * @throws IOException
     */
    public Locator userLogin() throws IOException {
        Locator locator = getLocator("账户登录");
        return locator;
    }

    /**
     * 用户名
     * @return
     * @throws IOException
     */
    public Locator userInput() throws IOException {
        Locator locator = getLocator("用户名输入框");
        return locator;
    }

    /**
     * 密码输入框
     * @return
     * @throws IOException
     */
    public Locator passWordInput() throws IOException {
        Locator locator = getLocator("密码输入框");
        return locator;
    }

    /**
     * 登录
     * @return
     * @throws IOException
     */
    public Locator loginButton() throws IOException {
        Locator locator = getLocator("登录按钮");
        return locator;
    }
}
