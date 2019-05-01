package com.example.controlinventario;

public class Product {
    private String Name;
    private int Price;
    private String Description;
    private String ImageAddress;

    public Product() {
    }

    public Product(String name, int price, String description, String imageAddress) {
        Name = name;
        Price = price;
        Description = description;
        ImageAddress = imageAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageAddress() {
        return ImageAddress;
    }

    public void setImageAddress(String imageAddress) {
        ImageAddress = imageAddress;
    }
}