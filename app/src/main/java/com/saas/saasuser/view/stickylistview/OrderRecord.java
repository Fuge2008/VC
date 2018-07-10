package com.saas.saasuser.view.stickylistview;



public class OrderRecord {

    /**
     * vehiclename : 法拉利
     * ordertype : 1
     * dname : 张毅阳-司机
     * usetime : 2017-12-28T00:00:00
     * endsite : 广东省深圳市福田区福荣路68-13号靠近中国邮政储蓄银行(深圳下沙支行)
     * ostate : 已完善
     * dphone : 18588458890
     * cname : 符振文
     * vehiclenum : 粤Z8888港
     * id : 11225
     * starttime : 2017/12/28 9:42:53
     * cphone : 13672780753
     * businseename : 太子辉集团
     * useweek : 星期四
     * endtime : 2017/12/28 9:43:55
     * startsite : 广东省深圳市福田区福荣路72号靠近中国邮政储蓄银行(深圳下沙支行)
     */

    private String vehiclename;
    private String ordertype;
    private String dname;
    private String usetime;
    private String endsite;
    private String ostate;
    private String dphone;
    private String cname;
    private String vehiclenum;
    private String id;
    private String starttime;
    private String cphone;
    private String businseename;
    private String useweek;
    private String endtime;
    private String startsite;
    
    public OrderRecord(OrderRecord orderRecord) {
        if (orderRecord == null) return;
        this.vehiclename = orderRecord.vehiclename;
        this.ordertype = orderRecord.ordertype;
        this.dname = orderRecord.dname;
        this.usetime = orderRecord.usetime;
        this.endsite = orderRecord.endsite;
        this.ostate = orderRecord.ostate;
        this.dphone = orderRecord.dphone;
        this.cname = orderRecord.cname;
        this.vehiclenum = orderRecord.vehiclenum;
        this.id = orderRecord.id;
        this.starttime = orderRecord.starttime;
        this.cphone = orderRecord.cphone;
        this.businseename = orderRecord.businseename;
        this.useweek = orderRecord.useweek;
        this.endtime = orderRecord.endtime;
        this.startsite = orderRecord.startsite;
    }


    public OrderRecord(String vehiclename, String ordertype, String dname, String usetime, String endsite, String ostate, String dphone, String cname, String vehiclenum, String id, String starttime, String cphone, String businseename, String useweek, String endtime, String startsite) {
        super();
        this.vehiclename = vehiclename;
        this.ordertype = ordertype;
        this.dname = dname;
        this.usetime = usetime;
        this.endsite = endsite;
        this.ostate = ostate;
        this.dphone = dphone;
        this.cname = cname;
        this.vehiclenum = vehiclenum;
        this.id = id;
        this.starttime = starttime;
        this.cphone = cphone;
        this.businseename = businseename;
        this.useweek = useweek;
        this.endtime = endtime;
        this.startsite = startsite;
    }

    public OrderRecord() {
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    public String getEndsite() {
        return endsite;
    }

    public void setEndsite(String endsite) {
        this.endsite = endsite;
    }

    public String getOstate() {
        return ostate;
    }

    public void setOstate(String ostate) {
        this.ostate = ostate;
    }

    public String getDphone() {
        return dphone;
    }

    public void setDphone(String dphone) {
        this.dphone = dphone;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getVehiclenum() {
        return vehiclenum;
    }

    public void setVehiclenum(String vehiclenum) {
        this.vehiclenum = vehiclenum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getBusinseename() {
        return businseename;
    }

    public void setBusinseename(String businseename) {
        this.businseename = businseename;
    }

    public String getUseweek() {
        return useweek;
    }

    public void setUseweek(String useweek) {
        this.useweek = useweek;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStartsite() {
        return startsite;
    }

    public void setStartsite(String startsite) {
        this.startsite = startsite;
    }

    @Override
    public String toString() {
        return "OrderRecord{" +
                "vehiclename='" + vehiclename + '\'' +
                ", ordertype='" + ordertype + '\'' +
                ", dname='" + dname + '\'' +
                ", usetime='" + usetime + '\'' +
                ", endsite='" + endsite + '\'' +
                ", ostate='" + ostate + '\'' +
                ", dphone='" + dphone + '\'' +
                ", cname='" + cname + '\'' +
                ", vehiclenum='" + vehiclenum + '\'' +
                ", id=" + id +
                ", starttime='" + starttime + '\'' +
                ", cphone='" + cphone + '\'' +
                ", businseename='" + businseename + '\'' +
                ", useweek='" + useweek + '\'' +
                ", endtime='" + endtime + '\'' +
                ", startsite='" + startsite + '\'' +
                '}';
    }
}
