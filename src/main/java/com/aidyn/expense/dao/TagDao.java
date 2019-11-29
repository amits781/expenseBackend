package com.aidyn.expense.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Tags;
import com.aidyn.expense.repo.TagRepo;

@Repository
public class TagDao {

	@Autowired
	TagRepo repository;
	
	public void save(Tags entity) {
		repository.save(entity);
	}
	
	public Tags getTagById(int id) {
		return repository.findById(id).get();
	}
	
	public List<Tags> getAll(){
		return repository.findAll();
	}
	
	public boolean hasEntry(int id) {
		return repository.existsById(id);
	}
	
	@Transactional
	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
