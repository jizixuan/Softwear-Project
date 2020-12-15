package com.example.myapplication.entity;

public class Budget {
    private int id;
    private double budget;

    public Budget() {
        super();
    }

    public Budget(int id, double budget) {
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
        return "Budget{" +
                "id=" + id +
                ", budget=" + budget +
                '}';
    }
}
