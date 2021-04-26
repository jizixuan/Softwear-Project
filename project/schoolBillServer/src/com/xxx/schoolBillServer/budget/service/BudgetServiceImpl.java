package com.xxx.schoolBillServer.budget.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxx.schoolBillServer.budget.dao.BudgetDaoImpl;
import com.xxx.schoolBillServer.entity.Budget;
@Service
@Transactional(readOnly = false)
public class BudgetServiceImpl {
	@Resource
	private BudgetDaoImpl budgetDaoImpl;
	public Budget getBudgetById(int id) {
		return budgetDaoImpl.getBudgetById(id);
	}
	public boolean addBudget(Budget budget) {
		return budgetDaoImpl.addBudget(budget);
	}
	public boolean updateBudget(Budget budget) {
		return budgetDaoImpl.updateBudget(budget);
	}
}
