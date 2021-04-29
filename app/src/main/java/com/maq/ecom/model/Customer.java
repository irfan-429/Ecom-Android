package com.maq.ecom.model;

public class Customer {
    String customerCode, customerName, openingBalance,openingCrt, address, route;
    String filterBy;

    public Customer(String customerCode, String customerName, String openingBalance, String openingCrt, String address, String filterBy) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.openingBalance=openingBalance;
        this.openingCrt=openingCrt;
        this.address=address;
        this.filterBy = filterBy;
    }

    public Customer(String route, String filterBy) {
        this.route = route;
        this.filterBy = filterBy;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public String getOpeningCrt() {
        return openingCrt;
    }

    public String getAddress() {
        return address;
    }

    public String getRoute() {
        return route;
    }

    @Override
    public String toString() {
        if (filterBy == null) {
            return customerName;
        } else {
            if (filterBy.equals("CustomerCode"))
                return customerName;
            else return route;
        }
    }
}
