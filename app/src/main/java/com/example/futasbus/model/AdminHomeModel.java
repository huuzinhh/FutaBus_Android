package com.example.futasbus.model;

public class AdminHomeModel {
    private String name;
    private int imageResource;

    public AdminHomeModel(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public AdminHomeModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
