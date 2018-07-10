package com.saas.saasuser.entity;

import java.util.List;

/**
 * Created by tanlin on 2017/10/16.
 */

public class GoongSiJianChengInfo {


    /**
     * ErrCode : 1
     * ErrMsg : 获取成功！
     *
     * Data : [{"ID":3,"SName":"太子辉集团","vvType":1},{"ID":10655,"SName":"测试客户","vvType":2},{"ID":20651,"SName":"44542","vvType":2},{"ID":20652,"SName":"vkmhvk","vvType":2}]
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
         * ID : 3
         * SName : 太子辉集团
         * vvType : 1
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
