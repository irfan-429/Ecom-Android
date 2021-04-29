package com.maq.ecom.model;


public class Child  {
    public String category, variants, itemName, unit, price, image, mWt, itemCode;

    public int lastEditedVal;

    public Child(String category, String variants, String itemName, String unit, String price, String image, String mWt, String itemCode) {
        this.category = category;
        this.variants = variants;
        this.itemName = itemName;
        this.unit = unit;
        this.price = price;
        this.image = image;
        this.mWt = mWt;
        this.itemCode=itemCode;
    }

    public Child(String category, String variants) {
        this.category = category;
        this.variants = variants;
    }

    public String getCategory() {
        return category;
    }

    public String getVariants() {
        return variants;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getmWt() {
        return mWt;
    }

    public String getItemCode() {
        return itemCode;
    }

    //    protected Child(Parcel in) {
//        itemName = in.readString();
//    }
//
//
//    public static final Creator<Child> CREATOR = new Creator<Child>() {
//        @Override
//        public Child createFromParcel(Parcel in) {
//            return new Child(in);
//        }
//
//        @Override
//        public Child[] newArray(int size) {
//            return new Child[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(itemName);
//    }


    public int getLastEditedVal() {
        return lastEditedVal;
    }

    public void setLastEditedVal(int lastEditedVal) {
        this.lastEditedVal = lastEditedVal;
    }
}
