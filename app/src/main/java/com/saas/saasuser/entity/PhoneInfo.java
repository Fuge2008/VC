package com.saas.saasuser.entity;

/**
 * Created by tanlin on 2017/10/16.
 */

public class PhoneInfo {


    /**
     * ErrCode : 1
     * ErrMsg : 获取成功！
     * Data : 13723123123
     */

    private int ErrCode;
    private String ErrMsg;
    private String Data;

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int ErrCode) {
        this.ErrCode = ErrCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}
