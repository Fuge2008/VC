package com.saas.saasuser.entity;

import java.util.List;

/**
 * Created by tanlin on 2017/10/21.
 */

public class JJRolesInfo {


    /**
     * ErrCode : 1
     * ErrMsg : 获取成功！
     * Data : [{"ID":7,"SName":"测试2","vvType":0}]
     */

    private int ErrCode;
    private String ErrMsg;
    private List<DataBean> Data;

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

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * ID : 7
         * SName : 测试2
         * vvType : 0
         */

        private int ID;
        private String SName;
        private int vvType;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getSName() {
            return SName;
        }

        public void setSName(String SName) {
            this.SName = SName;
        }

        public int getVvType() {
            return vvType;
        }

        public void setVvType(int vvType) {
            this.vvType = vvType;
        }
    }
}
