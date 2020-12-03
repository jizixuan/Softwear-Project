package com.example.myapplication;


import java.util.Date;
import java.util.List;

public class DateBill {
    private Date date;
    private double income;
    private double expenditure;
    private List<BillItem> bills;

    public DateBill() {
    }

    public DateBill(Date date, double income, double expenditure) {
        this.date = date;
        this.income = income;
        this.expenditure = expenditure;
    }

    public DateBill(Date date, double income, double expenditure, List<BillItem> bills) {
        this.date = date;
        this.income = income;
        this.expenditure = expenditure;
        this.bills = bills;
    }

    public List<BillItem> getBills() {
        return bills;
    }

    public void setBills(List<BillItem> bills) {
        this.bills = bills;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }
}
