package com.aidyn.expense.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.SubCategory;
import com.aidyn.expense.repo.SubCategoryRepo;

@Repository
public class SubCategoryDao {

	@Autowired
	SubCategoryRepo repo;
	
	public void saveSubCat(SubCategory category) {
		repo.save(category);
	}
	
	public SubCategory getSubCatByName(String name) {
		return repo.findByName(name);
	}
	
	public List<SubCategory> getAll(){
		return repo.findAll();
	}
	
	public SubCategory getSubCatById(int id) {
		return repo.findById(id).get();
	}
	
	public List<SubCategory> getAllByCategory(String name){
		return repo.findAllByCategoryName(name);
	}
	
	@Transactional
	public void delete (int id) {
		repo.deleteById(id);
	}
}
