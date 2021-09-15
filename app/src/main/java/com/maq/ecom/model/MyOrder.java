package com.maq.ecom.model;

public class MyOrder {

    private String orderNo, nag, items, orderDate, itemAmount, delCharge, disc, total, advance, address,orderAmount, city, state, pin, gstNo, aadharNo, name, emailId, mobileNo, status;


    public MyOrder(String orderNo, String nag, String items, String orderDate, String itemAmount, String delCharge, String disc, String total, String advance, String address, String orderAmount, String city, String state, String pin, String gstNo, String aadharNo, String name, String emailId, String mobileNo, String status) {
        this.orderNo = orderNo;
        this.nag = nag;
        this.items = items;
        this.orderDate = orderDate;
        this.itemAmount = itemAmount;
        this.delCharge = delCharge;
        this.disc = disc;
        this.total = total;
        this.advance = advance;
        this.address = address;
        this.orderAmount = orderAmount;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.gstNo = gstNo;
        this.aadharNo = aadharNo;
        this.name = name;
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getNag() {
        return nag;
    }

    public String getItems() {
        return items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public String getDelCharge() {
        return delCharge;
    }

    public String getDisc() {
        return disc;
    }

    public String getTotal() {
        return total;
    }

    public String getAdvance() {
        return advance;
    }

    public String getAddress() {
        return address;
    }

    public String getOrderAmount() {
        return orderAmount;
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

    public String getGstNo() {
        return gstNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getStatus() {
        return status;
    }
}
