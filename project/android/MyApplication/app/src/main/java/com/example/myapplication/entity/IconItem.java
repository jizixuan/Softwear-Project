package com.example.myapplication.entity;

import android.graphics.Bitmap;

import java.util.Date;

public class IconItem {
    private Bitmap img;
    private String type;
    private double num;
    private String numType;
    private String note;
    private String date;

    public IconItem() {
    }

    public IconItem(Bitmap img, String type, double num, String numType, String note, String date) {
        this.img = img;
        this.type = type;
        this.num = num;
        this.numType = numType;
        this.note = note;
        this.date = date;
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

    public String getNumType() {
        return numType;
    }

    public void setNumType(String numType) {
        this.numType = numType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
