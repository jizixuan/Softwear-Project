package com.xxx.schoolBillServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bill_type")
public class BillType {
	private String name;
	private String img;
	
	@Column(name = "num_type")
	private String numType;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
//	@OneToOne(mappedBy="billType")
//	private BillItem billItem;
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
