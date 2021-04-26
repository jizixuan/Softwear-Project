package com.xxx.schoolBillServer.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	@Column(name = "img")
	private String photo;//头像
	private String sex;//性别
	private String name;//昵称
	private String pwd;//密码
	private String phone;//电话号码
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;//用户id
//	@OneToMany(mappedBy="user", targetEntity=BillItem.class, 
//	        cascade=CascadeType.ALL)
//	private Set items = new HashSet<BillItem>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String photo, String sex, String name, String pwd, String phone, int id) {
		super();
		this.photo = photo;
		this.sex = sex;
		this.name = name;
		this.pwd = pwd;
		this.phone = phone;
		this.id = id;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
//	public Set getItems() {
//		return items;
//	}
//	public void setItems(Set items) {
//		this.items = items;
//	}
	@Override
	public String toString() {
		return "User [photo=" + photo + ", sex=" + sex + ", name=" + name + ", pwd=" + pwd + ", phone=" + phone
				+ ", id=" + id + "]";
	}
	

}
