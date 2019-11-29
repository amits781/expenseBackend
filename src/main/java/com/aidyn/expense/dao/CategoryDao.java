package com.aidyn.expense.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Category;
import com.aidyn.expense.repo.CategoryRepo;

@Repository
public class CategoryDao {

	@Autowired
	CategoryRepo repo;
	
	public void saveCat(Category category) {
		repo.save(category);
	}
	
	public Category getCatByName(String name) {
		return repo.findByName(name);
	}
	
	public List<Category> getAll(){
		return repo.findAll();
	}
	
}
