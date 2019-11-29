package com.aidyn.expense.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.SubCategory;

@Repository
public interface SubCategoryRepo extends CrudRepository<SubCategory, Integer> {

	public List<SubCategory> findAll();
	
	public SubCategory findByName(String name);
	
	public List<SubCategory> findAllByCategoryName(String name);
}
