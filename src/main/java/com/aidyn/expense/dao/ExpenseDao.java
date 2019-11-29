package com.aidyn.expense.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Expense;
import com.aidyn.expense.repo.ExpenseRepo;

@Repository
public class ExpenseDao {

	@Autowired
	ExpenseRepo repository;
	
	public void save(Expense entity) {
		repository.save(entity);
	}
	
	public Expense getExpenseById(Long id) {
		return repository.findById(id).get();
	}
	
	public List<Expense> getAll(){
		return repository.findAll();
	}
	
	public boolean hasEntry(long id) {
		return repository.existsById(id);
	}
	
	public List<Expense> getAllUnApprovedExpenses(){
		return repository.findAllByIsApproved(false);
	}
	
	@Transactional
	public void deleteById(long id) {
		repository.deleteById(id);
	}
}
