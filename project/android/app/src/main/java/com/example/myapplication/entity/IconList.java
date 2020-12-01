package com.example.myapplication.entity;

import java.util.Date;
import java.util.List;

public class IconList {
    private Date date;
    private List<IconItem> bills;

    public IconList(Date date, List<IconItem> bills) {
        this.date = date;
        this.bills = bills;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<IconItem> getBills() {
        return bills;
    }

    public void setBills(List<IconItem> bills) {
        this.bills = bills;
    }
}
