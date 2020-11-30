package com.xxx.schoolBillServer.entity;

import java.util.Date;

public class BillItem {
	private int id;
	private double num;
	private String note;
	private Date date;
	private int typeId;
	private int year;
	private int month;
	private int day;
	
	public BillItem() {
		super();
	}

	public BillItem(int id, double num, String note, Date date, int typeId) {
		super();
		this.id = id;
		this.num = num;
		this.note = note;
		this.date = date;
		this.typeId = typeId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "BillItem [id=" + id + ", num=" + num + ", note=" + note + ", date=" + date + ", typeId=" + typeId
				+ ", year=" + year + ", month=" + month + ", day=" + day + "]";
	}
	
}
