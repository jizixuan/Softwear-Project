package com.example.myapplication.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BillItem implements Serializable {
    private Bitmap img;
    private String type;
    private double num;
    private String numType;
    private String note;

    public BillItem() {
    }

    public BillItem(Bitmap img, String type, double num, String numType, String note) {
        this.img = img;
        this.type = type;
        this.num = num;
        this.numType = numType;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "img=" + img +
                ", type='" + type + '\'' +
                ", num=" + num +
                ", numType='" + numType + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
