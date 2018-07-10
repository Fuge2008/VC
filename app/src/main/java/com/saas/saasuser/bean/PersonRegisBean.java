package com.saas.saasuser.bean;

/**
 * Created by tyh on 2017/9/13.
 */

public class PersonRegisBean {


    /**
     * ErrCode : 1
     * ErrMsg : null
     * Data : {"EnterShortName":"保时捷"}
     */

    private int ErrCode;
    private Object ErrMsg;
    private DataBean Data;

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int ErrCode) {
        this.ErrCode = ErrCode;
    }

    public Object getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(Object ErrMsg) {
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
