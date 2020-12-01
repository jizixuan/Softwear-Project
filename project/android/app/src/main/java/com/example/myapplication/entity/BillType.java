package com.example.myapplication.entity;

import android.graphics.Bitmap;

public class BillType {
    private Bitmap img;
    private String name;

    public BillType() {
    }

    public BillType(Bitmap img, String name) {
        this.img = img;
        this.name = name;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "img=" + img +
                ", name='" + name + '\'' +
                '}';
    }
}
