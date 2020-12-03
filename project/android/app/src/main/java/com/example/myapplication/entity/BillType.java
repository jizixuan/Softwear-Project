package com.example.myapplication.entity;

import android.graphics.Bitmap;

public class BillType {
    private Bitmap img;
    private String name;
    private String numType;
    private int id;
    private String imgName;

    public BillType() {
    }

    public BillType(Bitmap img, String name, String numType) {
        this.img = img;
        this.name = name;
        this.numType = numType;
    }

    public BillType(Bitmap img, String name) {
        this.img = img;
        this.name = name;
    }

    public Bitmap getImg() {
        return img;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public String toString() {
        return "BillType{" +
                "img=" + img +
                ", name='" + name + '\'' +
                ", numType='" + numType + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
