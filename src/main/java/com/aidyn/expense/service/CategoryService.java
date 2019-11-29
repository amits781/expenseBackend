package com.aidyn.expense.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aidyn.expense.dao.CategoryDao;
import com.aidyn.expense.model.Category;

@Service
public class CategoryService {

	@Autowired
	CategoryDao dao;
	
	public void saveCategory(Category category) throws Exception {
		if(dao.getCatByName(category.getName()) == null) {
			if(category.getName()!=null && category.getName() !="" )
			dao.saveCat(category);
			else
				throw new Exception("Empty Category");
		} else {
			throw new Exception("Duplicate Category");
		}
	}
	
	public List<Category> getAll() {
		return dao.getAll();
	}
	
	public Category getCatByName(String name) {
		return dao.getCatByName(name);
	}
}
