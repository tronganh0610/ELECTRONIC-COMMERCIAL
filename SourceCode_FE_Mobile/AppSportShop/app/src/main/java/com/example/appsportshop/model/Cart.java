package com.example.appsportshop.model;

import java.io.Serializable;

public class Cart implements Serializable {

    long id;
    //full name của user
    private String nameProduct;
    private String idProduct;
    private float price_total;
    private int quantity;
    private String urlImage;
    private Boolean isSelected = false;

    long id_order_status =1;

    public long getId_order_status() {
        return id_order_status;
    }

    public void setId_order_status(long id_order_status) {
        this.id_order_status = id_order_status;
    }

    public Cart() {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public float getPrice_total() {
        return price_total;
    }

    public void setPrice_total(float price_total) {
        this.price_total = price_total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
