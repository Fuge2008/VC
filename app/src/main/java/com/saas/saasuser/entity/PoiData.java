package com.saas.saasuser.entity;


/**
 * 保存定位产生的数据
 */
public class PoiData {
    private String name;
    private String address;

    public PoiData() {
    }

    public PoiData(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PoiData{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
