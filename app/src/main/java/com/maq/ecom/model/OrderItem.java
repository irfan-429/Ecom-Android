package com.maq.ecom.model;

public class OrderItem {

    private String orderId, orderDate,orderAmount,nag , crate, weight, status, sNo, itemName, image, qty, unit, rate, itemTotalAmt;

    public OrderItem(String orderId, String orderDate, String orderAmount, String nag, String crate, String weight, String status, String sNo, String itemName, String image, String qty, String unit, String rate, String itemTotalAmt) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
        this.nag = nag;
        this.crate = crate;
        this.weight = weight;
        this.status = status;
        this.sNo = sNo;
        this.itemName = itemName;
        this.image = image;
        this.qty = qty;
        this.unit = unit;
        this.rate = rate;
        this.itemTotalAmt = itemTotalAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getNag() {
        return nag;
    }

    public String getCrate() {
        return crate;
    }

    public String getWeight() {
        return weight;
    }

    public String getStatus() {
        return status;
    }

    public String getsNo() {
        return sNo;
    }

    public String getItemName() {
        return itemName;
    }

    public String getImage() {
        return image;
    }

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getRate() {
        return rate;
    }

    public String getItemTotalAmt() {
        return itemTotalAmt;
    }
}
