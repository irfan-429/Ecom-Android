package com.maq.ecom.model;

public class OrderDetailsItem {
    private String orderId, nag, orderDate, delCharge, promoCode, disc, remarks, subTotal, advance, noOfItems,
            addressId, orderAmount, address, city, state, pin,addressType, altMobile, gstNo, aadharNo, image, sNo, productId, productName, qty, unit, price,
            sellingPrice, amount, status, productCode, description;


    public OrderDetailsItem(String orderId, String nag, String orderDate, String delCharge, String promoCode, String disc, String remarks, String subTotal, String advance, String noOfItems, String addressId, String orderAmount, String address, String city, String state, String pin, String addressType, String altMobile, String gstNo, String aadharNo, String image, String sNo, String productId, String productName, String qty, String unit, String price, String sellingPrice, String amount, String status, String productCode, String description) {
        this.orderId = orderId;
        this.nag = nag;
        this.orderDate = orderDate;
        this.delCharge = delCharge;
        this.promoCode = promoCode;
        this.disc = disc;
        this.remarks = remarks;
        this.subTotal = subTotal;
        this.advance = advance;
        this.noOfItems = noOfItems;
        this.addressId = addressId;
        this.orderAmount = orderAmount;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.addressType = addressType;
        this.altMobile = altMobile;
        this.gstNo = gstNo;
        this.aadharNo = aadharNo;
        this.image = image;
        this.sNo = sNo;
        this.productId = productId;
        this.productName = productName;
        this.qty = qty;
        this.unit = unit;
        this.price = price;
        this.sellingPrice = sellingPrice;
        this.amount = amount;
        this.status = status;
        this.productCode = productCode;
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getNag() {
        return nag;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDelCharge() {
        return delCharge;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getDisc() {
        return disc;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getAdvance() {
        return advance;
    }

    public String getNoOfItems() {
        return noOfItems;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getOrderAmount() {
        return orderAmount;
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

    public String getGstNo() {
        return gstNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public String getImage() {
        return image;
    }

    public String getsNo() {
        return sNo;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getDescription() {
        return description;
    }
}
