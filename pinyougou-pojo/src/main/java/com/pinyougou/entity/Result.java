package com.pinyougou.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/9/26.
 */
public class Result implements Serializable {
    private boolean success;
    private String code;

    public Result() {
        this.success = true;
        this.code = "200";
    }

    public Result(boolean success, String code) {
        this.success = success;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
