package com.example.utils;

/**
 * @Company
 * @Discription
 * @Author guoxiaojing
 * @CreateDate 2018/2/28 10:32
 * @Version 1.0
 */
public class Locator {
    private String element;
    private int waitSec;
    private String locatorName;

    public enum ByType{
        xpath, id, linkText, name, className, cssSelector, partialLinkText, tagName
    }

    private ByType byType;

    public Locator() {

    }

    public Locator(String element){
        this.element = element;
        this.waitSec = 3;
        this.byType = ByType.xpath;
    }
    public Locator(String element, int waitSec) {
        this.waitSec = waitSec;
        this.element = element;
        this.byType = ByType.xpath;
    }
    public Locator(String element, int waitSec, ByType byType) {
        this.waitSec = waitSec;
        this.element = element;
        this.byType = byType;
    }
    public Locator(String element, int waitSec, ByType byType,String locatorName) {
        this.waitSec = waitSec;
        this.element = element;
        this.byType = byType;
        this.locatorName=locatorName;
    }
    public String getElement() {
        return element;
    }
    public int getWaitSec() {
        return waitSec;
    }
    public ByType getBy() {
        return byType;
    }
    public void setBy(ByType byType) {
        this.byType = byType;
    }
    public String getLocalorName()
    {
        return locatorName;
    }
}
