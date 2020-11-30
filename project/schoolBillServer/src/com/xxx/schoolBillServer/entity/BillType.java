package com.xxx.schoolBillServer.entity;

public class BillType {
	private String name;
	private String img;
	private String numType;
	private int id;
	public BillType() {
		super();
	}
	public BillType(String name, String img, String numType) {
		super();
		this.name = name;
		this.img = img;
		this.numType = numType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getNumType() {
		return numType;
	}
	public void setNumType(String numType) {
		this.numType = numType;
	}
	@Override
	public String toString() {
		return "BillType [name=" + name + ", img=" + img + ", numType=" + numType + "]";
	}
	
}
