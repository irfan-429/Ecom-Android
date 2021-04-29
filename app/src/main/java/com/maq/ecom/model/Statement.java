package com.maq.ecom.model;

public class Statement {

    private String trnNo, trnDate, recvAmount, orderAmount, customerCode;

    public Statement(String trnNo, String trnDate, String recvAmount, String orderAmount, String customerCode) {
        this.trnNo = trnNo;
        this.trnDate = trnDate;
        this.recvAmount = recvAmount;
        this.orderAmount = orderAmount;
        this.customerCode = customerCode;
    }

    public String getTrnNo() {
        return trnNo;
    }

    public String getTrnDate() {
        return trnDate;
    }

    public String getRecvAmount() {
        return recvAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getCustomerCode() {
        return customerCode;
    }
}
