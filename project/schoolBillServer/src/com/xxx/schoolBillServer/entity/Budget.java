package com.xxx.schoolBillServer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "budget")
public class Budget {
	@Id
	@Column(name = "user_id")
	private int id;//用户id
	@Column(name = "budget")
	private double budget;//用户预算
	public Budget() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Budget(int id, double budget) {
		super();
		this.id = id;
		this.budget = budget;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	@Override
	public String toString() {
		return "Budget [id=" + id + ", budget=" + budget + "]";
	}
	
}
