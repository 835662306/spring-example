package com.example.action;

import com.example.pageObject.LoginPage;
import com.example.utils.TestBaseCase;

import java.io.IOException;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 10:24
 * @Version 1.0
 */
public class LoginAction extends TestBaseCase {
    public LoginAction(String Url, String UserName, String PassWord) throws IOException{
        LoginPage loginPage = new LoginPage();
        loginPage.open(Url);

        System.out.println(webdriver.getCurrentUrl());

        ElementAction action = new ElementAction();
        action.click(loginPage.userLogin());
        action.sleep(1);
        webdriver.switchTo().frame(0);
        action.clear(loginPage.passWordInput());
        action.type(loginPage.userInput(),UserName);
        action.clear(loginPage.passWordInput());
        action.type(loginPage.passWordInput(),PassWord);
        action.click(loginPage.loginButton());
    }
}
