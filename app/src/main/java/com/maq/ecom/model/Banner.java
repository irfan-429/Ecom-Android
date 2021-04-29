package com.maq.ecom.model;

public class Banner {

    private String banId,  banName, banLink, banImg, banStatus;

    public Banner(String banId, String banName, String banLink, String banImg, String banStatus) {
        this.banId = banId;
        this.banName = banName;
        this.banLink = banLink;
        this.banImg = banImg;
        this.banStatus = banStatus;
    }

    public String getBanId() {
        return banId;
    }

    public String getBanName() {
        return banName;
    }

    public String getBanLink() {
        return banLink;
    }

    public String getBanImg() {
        return banImg;
    }

    public String getBanStatus() {
        return banStatus;
    }
}
