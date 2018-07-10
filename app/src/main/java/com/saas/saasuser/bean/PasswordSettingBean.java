package com.saas.saasuser.bean;

/**
 * 作者：张毅阳 on 2017/10/21 10:36
 */

public class PasswordSettingBean {
    private int ErrCode;
    private String ErrMsg;
    private Data mData;

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int errCode) {
        ErrCode = errCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public static class Data{
        private String EnterShortName;

        public String getEnterShortName() {
            return EnterShortName;
        }

        public void setEnterShortName(String enterShortName) {
            EnterShortName = enterShortName;
        }
    }
}
