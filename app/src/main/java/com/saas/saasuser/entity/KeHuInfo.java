package com.saas.saasuser.entity;

import java.util.List;

/**
 * Created by tanlin on 2017/10/16.
 */

public class KeHuInfo {


    /**
     * ErrCode : 1
     * ErrMsg : 获取成功！
     * Data : [{"ID":4,"SName":"太子辉","vvType":1},{"ID":157,"SName":"简松","vvType":1},{"ID":158,"SName":"唐永洪","vvType":1},{"ID":159,"SName":"阿斯顿发","vvType":1},{"ID":163,"SName":"测试1","vvType":1},{"ID":164,"SName":"下定","vvType":1},{"ID":20180,"SName":"测试审核功能","vvType":1}]
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
         * ID : 4
         * SName : 太子辉
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
