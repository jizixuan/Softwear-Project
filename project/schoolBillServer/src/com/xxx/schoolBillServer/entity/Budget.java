package com.xxx.schoolBillServer.entity;

public class Budget {
	private int id;//用户id
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
