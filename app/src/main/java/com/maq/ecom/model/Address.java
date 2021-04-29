package com.maq.ecom.model;

import java.io.Serializable;

public class Address implements Serializable {

    private String id, userId, address, city, state, pin, addressType, altMobile, latitude, longtitude, isDefault, gstNo,aadharNo;
    private boolean selected;

    public Address(String id, String userId, String address, String city, String state, String pin, String addressType, String altMobile, String latitude, String longtitude, String isDefault, String gstNo, String aadharNo) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.addressType = addressType;
        this.altMobile = altMobile;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.isDefault = isDefault;
        this.gstNo = gstNo;
        this.aadharNo = aadharNo;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPin() {
        return pin;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getAltMobile() {
        return altMobile;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public String getGstNo() {
        return gstNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
