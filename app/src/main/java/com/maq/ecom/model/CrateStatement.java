package com.maq.ecom.model;

public class CrateStatement {

    private String trnNo, trnDate, sendCrate, crateBill, recvImage, remarks, status;

    String recvId, hiddenTrnId, showTrnId, recvDate, crate,  customerName, customerCode;
    public CrateStatement(String trnNo, String trnDate, String sendCrate, String crateBill, String recvImage, String remarks, String status) {
        this.trnNo = trnNo;
        this.trnDate = trnDate;
        this.sendCrate = sendCrate;
        this.crateBill = crateBill;
        this.recvImage = recvImage;
        this.remarks = remarks;
        this.status = status;
    }

    public CrateStatement(String trnNo, String trnDate, String sendCrate, String crateBill, String recvImage, String remarks, String status, String customerCode) {
        this.trnNo = trnNo;
        this.trnDate = trnDate;
        this.sendCrate = sendCrate;
        this.crateBill = crateBill;
        this.recvImage = recvImage;
        this.remarks = remarks;
        this.status = status;
        this.customerCode=customerCode;
    }

    public CrateStatement(String recvId, String hiddenTrnId, String showTrnId, String recvDate, String crate, String recvImage, String remarks, String status, String customerName, String customerCode) {
        this.recvId = recvId;
        this.hiddenTrnId = hiddenTrnId;
        this.showTrnId = showTrnId;
        this.recvDate = recvDate;
        this.crate = crate;
        this.recvImage = recvImage;
        this.remarks = remarks;
        this.status = status;
        this.customerName = customerName;
        this.customerCode=customerCode;
    }

    public String getTrnNo() {
        return trnNo;
    }

    public String getTrnDate() {
        return trnDate;
    }

    public String getSendCrate() {
        return sendCrate;
    }

    public String getCrateBill() {
        return crateBill;
    }

    public String getRecvImage() {
        return recvImage;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }

    public String getRecvId() {
        return recvId;
    }

    public String getHiddenTrnId() {
        return hiddenTrnId;
    }

    public String getShowTrnId() {
        return showTrnId;
    }

    public String getRecvDate() {
        return recvDate;
    }

    public String getCrate() {
        return crate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }
}
