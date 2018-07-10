package com.saas.saasuser.bean;

/**
 * 作者：张毅阳 on 2017/10/21 10:54
 */

public class EnterpriseRegisterBean {


    /**
     * ErrCode : 1
     * ErrMsg : 操作成功
     * Data : {"EnterShortName":"保时捷"}
     */

    private int ErrCode;
    private String ErrMsg;
    private DataBean Data;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * EnterShortName : 保时捷
         */

        private String EnterShortName;

        public String getEnterShortName() {
            return EnterShortName;
        }

        public void setEnterShortName(String EnterShortName) {
            this.EnterShortName = EnterShortName;
        }
    }
}
