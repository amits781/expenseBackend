package com.aidyn.expense.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Expense;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, Long> {

	public List<Expense> findAll();
	
	public List<Expense> findAllByIsApproved(boolean isApproved);
}
