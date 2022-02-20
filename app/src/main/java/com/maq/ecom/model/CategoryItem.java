package com.maq.ecom.model;

import java.io.Serializable;

public class CategoryItem implements Serializable {

    private String type;
    private String productId, firmId, productCode, productName, categoryId, categoryName,
            price, discount, sellingPrice, shortDesc, description, status, isFeatured, isNew, isPopular,
            productCover, image1, image2, image3, image4, image5, image6, keyFeatures, isSize, stock , unit,  categoryBanner;
    int qty = 0;

    public CategoryItem(String type, String productId, String productCode, String productName, String price, String discount, String sellingPrice, String shortDesc, String stock, String unit, String  productCover, String description, String image1, String image2, String image3, String image4, String image5, String image6, int qty) {
        this.type = type;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.sellingPrice = sellingPrice;
        this.shortDesc = shortDesc;
        this.stock = stock;
        this.unit = unit;
        this.productCover = productCover;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;
        this.qty = qty;
    }

    public CategoryItem(String productId, String firmId, String productCode, String productName, String categoryId, String categoryName, String price, String discount, String sellingPrice, String shortDesc, String description, String status, String isFeatured, String isNew, String isPopular, String productCover, String image1, String image2, String image3, String image4, String image5, String image6, String keyFeatures, String isSize, String stock, String unit, String categoryBanner) {
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
        this.unit = unit;
        this.categoryBanner = categoryBanner;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFirmId() {
        return firmId;
    }

    public void setFirmId(String firmId) {
        this.firmId = firmId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(String isPopular) {
        this.isPopular = isPopular;
    }

    public String getProductCover() {
        return productCover;
    }

    public void setProductCover(String productCover) {
        this.productCover = productCover;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public void setKeyFeatures(String keyFeatures) {
        this.keyFeatures = keyFeatures;
    }

    public String getIsSize() {
        return isSize;
    }

    public void setIsSize(String isSize) {
        this.isSize = isSize;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategoryBanner() {
        return categoryBanner;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
