package com.example.appsportshop.model;

import java.io.Serializable;

public class Product  implements Serializable {

    long id;
    private String nameProduct;
    private int stockQuantity;
    private float price;
    private String category;
    private String description;
    private String urlImage;
    private String publicId;

    private boolean isSelect;

    public Product() {

    }

    public long getId() {
        return id;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Product(long id, String nameProduct, int stockQuantity, float price, String category, String description, String urlImage, String publicId, boolean isSelect) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.category = category;
        this.description = description;
        this.urlImage = urlImage;
        this.publicId = publicId;
        this.isSelect = isSelect;
    }
}
