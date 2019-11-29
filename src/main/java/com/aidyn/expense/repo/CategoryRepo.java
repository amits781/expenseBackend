package com.aidyn.expense.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Category;

@Repository
public interface CategoryRepo extends CrudRepository<Category, Integer> {

	public List<Category> findAll();
	
	public Category findByName(String name);
}
