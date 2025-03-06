package com.example.appsportshop.model;

public class HoaDon {

    String id_Order;
    String productName;
    long quantity;
    float price;
    String shippingAdress;
    String nameReciver;
    String sdt;

    public HoaDon() {

    }

    public String getId_Order() {
        return id_Order;
    }

    public void setId_Order(String id_Order) {
        this.id_Order = id_Order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShippingAdress() {
        return shippingAdress;
    }

    public void setShippingAdress(String shippingAdress) {
        this.shippingAdress = shippingAdress;
    }

    public String getNameReciver() {
        return nameReciver;
    }

    public void setNameReciver(String nameReciver) {
        this.nameReciver = nameReciver;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
