package com.example.myapplication.entity;

public class BillMonth {
    private int year;//年分
    private int month;//月份
    private double bill;//支出或者收入总和
    private String type;//查看总和是收入还是支出

    public BillMonth() {
        super();
    }

    public BillMonth(int year, int month, double bill, String type) {
        this.year = year;
        this.month = month;
        this.bill = bill;
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BillMonth{" +
                "year=" + year +
                ", month=" + month +
                ", bill=" + bill +
                ", type='" + type + '\'' +
                '}';
    }
}
