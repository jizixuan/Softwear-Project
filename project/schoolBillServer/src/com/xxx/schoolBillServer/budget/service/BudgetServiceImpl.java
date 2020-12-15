package com.xxx.schoolBillServer.budget.service;

import com.xxx.schoolBillServer.budget.dao.BudgetDaoImpl;
import com.xxx.schoolBillServer.entity.Budget;

public class BudgetServiceImpl {
	private BudgetDaoImpl budgetDaoImpl;
	public Budget getBudgetById(int id) {
		budgetDaoImpl = new BudgetDaoImpl();
		return budgetDaoImpl.getBudgetById(id);
	}
	public boolean addBudget(Budget budget) {
		budgetDaoImpl = new BudgetDaoImpl();
		return budgetDaoImpl.addBudget(budget);
	}
	public boolean deleteBudget(int id) {
		budgetDaoImpl = new BudgetDaoImpl();
		return budgetDaoImpl.deleteBudget(id);
	}
	public boolean updateBudget(Budget budget) {
		budgetDaoImpl = new BudgetDaoImpl();
		return budgetDaoImpl.updateBudget(budget);
	}
}
