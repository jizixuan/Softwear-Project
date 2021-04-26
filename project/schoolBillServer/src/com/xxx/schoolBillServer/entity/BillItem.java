package com.xxx.schoolBillServer.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "bill_item")
public class BillItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private double num;
	private String note;
	private Date date;
	@Column(name = "type_id")
	private int typeId;
	@Column(name = "user_id")
	private int userId;
	private int year;
	private int month;
	private int day;
	
	//级联刷新
//	@OneToOne(cascade = CascadeType.REFRESH)
//	@JoinColumn(name="type_id" ,insertable =false ,updatable =false)
//	private BillType billType;
//	@ManyToOne
//	@JoinColumn(name="user_id",insertable =false ,updatable =false)
//	private User user;
	
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "BillItem [id=" + id + ", num=" + num + ", note=" + note + ", date=" + date + ", typeId=" + typeId
				+ ", userId=" + userId + ", year=" + year + ", month=" + month + ", day=" + day + "]";
	}

	
//	public BillType getBillType() {
//		return billType;
//	}
//
//	public void setBillType(BillType billType) {
//		this.billType = billType;
//	}
//	
//	
//	
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	

	

	
	
}
