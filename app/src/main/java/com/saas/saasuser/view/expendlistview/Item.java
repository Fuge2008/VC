//package com.saas.saasuser.view.expendlistview;
//
//import android.view.View;
//
///**
// * Simple POJO model for example
// */
//public class Item {
//    private String orderId;
//    private String orderNum;
//    private String isRole;
//    private String strTag;
//    private String phone;
//    private String image;
//    private String fromAddress;
//    private String toAddress;
//    private  String name;
//    private String date;
//    private String time;
//
//    private View.OnClickListener requestBtnClickListener;
//
//    public Item() {
//    }
//
//    public Item(String orderId, String orderNum, String isRole, String strTag, String phone, String image, String fromAddress, String toAddress, String name, String date, String time) {
//        this.orderId = orderId;
//        this.orderNum = orderNum;
//        this.isRole = isRole;
//        this.strTag = strTag;
//        this.phone = phone;
//        this.image = image;
//        this.fromAddress = fromAddress;
//        this.toAddress = toAddress;
//        this.name = name;
//        this.date = date;
//        this.time = time;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getOrderNum() {
//        return orderNum;
//    }
//
//    public void setOrderNum(String orderNum) {
//        this.orderNum = orderNum;
//    }
//
//    public String getIsRole() {
//        return isRole;
//    }
//
//    public void setIsRole(String isRole) {
//        this.isRole = isRole;
//    }
//
//    public String getStrTag() {
//        return strTag;
//    }
//
//    public void setStrTag(String strTag) {
//        this.strTag = strTag;
//    }
//
//    public String getphone() {
//        return phone;
//    }
//
//    public void setphone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getimage() {
//        return image;
//    }
//
//    public void setimage(String image) {
//        this.image = image;
//    }
//
//    public String getFromAddress() {
//        return fromAddress;
//    }
//
//    public void setFromAddress(String fromAddress) {
//        this.fromAddress = fromAddress;
//    }
//
//    public String getToAddress() {
//        return toAddress;
//    }
//
//    public void setToAddress(String toAddress) {
//        this.toAddress = toAddress;
//    }
//
//    public String getname() {
//        return name;
//    }
//
//    public void setname(String name) {
//        this.name = name;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public View.OnClickListener getRequestBtnClickListener() {
//        return requestBtnClickListener;
//    }
//
//    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
//        this.requestBtnClickListener = requestBtnClickListener;
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////
////        Item item = (Item) o;
////
////        if (name != item.name) return false;
////        if (phone != null ? !phone.equals(item.phone) : item.phone != null) return false;
////        if (image != null ? !image.equals(item.image) : item.image != null)
////            return false;
////        if (fromAddress != null ? !fromAddress.equals(item.fromAddress) : item.fromAddress != null)
////            return false;
////        if (toAddress != null ? !toAddress.equals(item.toAddress) : item.toAddress != null)
////            return false;
////        if (date != null ? !date.equals(item.date) : item.date != null) return false;
////        return !(time != null ? !time.equals(item.time) : item.time != null);
////
////    }
////
////    @Override
////    public int hashCode() {
////        int result = phone != null ? phone.hashCode() : 0;
////        result = 31 * result + (image != null ? image.hashCode() : 0);
////        result = 31 * result + (fromAddress != null ? fromAddress.hashCode() : 0);
////        result = 31 * result + (toAddress != null ? toAddress.hashCode() : 0);
////        result = 31 * result + (name != null ? name.hashCode() : 0);
////        result = 31 * result + (date != null ? date.hashCode() : 0);
////        result = 31 * result + (time != null ? time.hashCode() : 0);
////        return result;
////    }
//
//
//
//}
