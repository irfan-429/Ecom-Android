package com.maq.ecom.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String productId, firmId, productCode, productName, categoryId, categoryName, price,
            discount, sellingPrice, shortDesc, description, status, isFeatured, isNew, isPopular, productCover,
            image1, image2, image3, image4, image5, image6, keyFeatures, isSize, stock;

    public Product(String productId, String firmId, String productCode, String productName, String categoryId, String categoryName, String price, String discount, String sellingPrice, String shortDesc, String description, String status, String isFeatured, String isNew, String isPopular, String productCover, String image1, String image2, String image3, String image4, String image5, String image6, String keyFeatures, String isSize, String stock) {
        this.productId = productId;
        this.firmId = firmId;
        this.productCode = productCode;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.discount = discount;
        this.sellingPrice = sellingPrice;
        this.shortDesc = shortDesc;
        this.description = description;
        this.status = status;
        this.isFeatured = isFeatured;
        this.isNew = isNew;
        this.isPopular = isPopular;
        this.productCover = productCover;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.keyFeatures = keyFeatures;
        this.isSize = isSize;
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public String getFirmId() {
        return firmId;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public String getIsNew() {
        return isNew;
    }

    public String getIsPopular() {
        return isPopular;
    }

    public String getProductCover() {
        return productCover;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getImage3() {
        return image3;
    }

    public String getImage4() {
        return image4;
    }

    public String getImage5() {
        return image5;
    }

    public String getImage6() {
        return image6;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public String getIsSize() {
        return isSize;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
