package com.example.myapplication.entity;

import android.graphics.Bitmap;

import java.util.Date;

public class IoDetail {
    private Bitmap img;
    private String type;
    private Date date;
    private double num;
    private double percent;
    private float progress;
    private String note;

    public IoDetail() {
    }

    public IoDetail(Bitmap img, String type, Date date, double num, double percent, float progress) {
        this.img = img;
        this.type = type;
        this.date = date;
        this.num = num;
        this.percent = percent;
        this.progress = progress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "IoDetail{" +
                "img=" + img +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", num=" + num +
                ", percent=" + percent +
                ", progress=" + progress +
                '}';
    }
}
