package com.pinyougou.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/9/26.
 */
public class Result implements Serializable {
    private boolean success;
    private String message;

    public Result() {
        this.success = true;
        this.message = "成功";
    }

    public Result(String message) {
        this.message = message;
        this.success = false;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
