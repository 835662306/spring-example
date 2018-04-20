package com.example.utils;

import java.util.HashMap;

/**
 * Date: 2015/6/19  11:13
 *
 * @author lyp
 */
public class ActionResult extends HashMap {
    public ActionResult() {
        this.put("success", true);
    }
    public ActionResult(String successMsg) {
        this.put("success", true);
        this.put("msg", successMsg);
    }
    public ActionResult(boolean isSuccess, String successMsg) {
        this.put("success", isSuccess);
        this.put("msg", successMsg);
    }
    public void setSuccess(boolean isSuccess){
        this.put("success", isSuccess);
    }
    public void setMsg(String msg){
        this.put("msg", msg);
    }
}
