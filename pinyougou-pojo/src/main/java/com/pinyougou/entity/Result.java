package com.pinyougou.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/9/26.
 */
public class Result implements Serializable {
    private boolean success;
    private int code;

    public Result() {
        this.success = true;
        this.code = 200;
    }

    public Result(boolean success, int code) {
        this.success = success;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
