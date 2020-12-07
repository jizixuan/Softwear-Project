package com.example.myapplication.entity;

public class BillItemMessage {
    private int id;
    private double num;
    private String note;
    private int typeId;
    private String date;
    private String typeName;
    private int flag;

    public BillItemMessage() {
    }


    public BillItemMessage(int id, double num, String note, int typeId, String date, String typeName) {
        this.id = id;
        this.num = num;
        this.note = note;
        this.typeId = typeId;
        this.date = date;
        this.typeName = typeName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
