package com.xxx.schoolBillServer.entity;

public class BillMonth {
	private int year;//���
	private int month;//�·�
	private double bill;//֧�����������ܺ�
	private String type;//�鿴�ܺ������뻹��֧��
	public BillMonth() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BillMonth(int year, int month, double bill, String type) {
		super();
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
	@Override
	public String toString() {
		return "BillMonth [year=" + year + ", month=" + month + ", bill=" + bill + ", type=" + type + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
