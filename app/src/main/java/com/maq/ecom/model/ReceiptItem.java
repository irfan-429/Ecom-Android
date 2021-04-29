package com.maq.ecom.model;

public class ReceiptItem {

    private String recvId, hiddenTrnId, showTrnId, recvDate, recvAmount, recvImage, remarks, status, bankName;

    public ReceiptItem(String recvId, String hiddenTrnId, String showTrnId, String recvDate, String recvAmount, String recvImage, String remarks, String status, String bankName) {
        this.recvId = recvId;
        this.hiddenTrnId = hiddenTrnId;
        this.showTrnId = showTrnId;
        this.recvDate = recvDate;
        this.recvAmount = recvAmount;
        this.recvImage = recvImage;
        this.remarks = remarks;
        this.status = status;
        this.bankName = bankName;
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

    public String getRecvAmount() {
        return recvAmount;
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

    public String getBankName() {
        return bankName;
    }
}
