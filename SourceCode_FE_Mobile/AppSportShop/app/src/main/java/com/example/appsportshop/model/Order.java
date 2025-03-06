package com.example.appsportshop.model;

public class Order {
    String id;
    String orderDate;
    String shippingAdress;
    String shippingDate;
    float totalAmount;
    String idCustomer;
    String idOderStatus;
    String idShippingMethod;
    String name_ceciver;
    String phoneNumber;

    public Order() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippingAdress() {
        return shippingAdress;
    }

    public void setShippingAdress(String shippingAdress) {
        this.shippingAdress = shippingAdress;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdOderStatus() {
        return idOderStatus;
    }

    public void setIdOderStatus(String idOderStatus) {
        this.idOderStatus = idOderStatus;
    }

    public String getIdShippingMethod() {
        return idShippingMethod;
    }

    public void setIdShippingMethod(String idShippingMethod) {
        this.idShippingMethod = idShippingMethod;
    }

    public String getName_ceciver() {
        return name_ceciver;
    }

    public void setName_ceciver(String name_ceciver) {
        this.name_ceciver = name_ceciver;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
