package com.aidyn.expense.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Person;
import com.aidyn.expense.repo.UserRepo;

@Repository
public class UserDao {

	@Autowired
	UserRepo repository;
	
	public Person save(Person person) {
		return repository.save(person);
	}
	
	public Person getById(long id) {
		return repository.findById(id).get();
	}
	
	public boolean hasEntry(long id) {
		return repository.existsById(id);
	}
	
	public List<Person> getAll(){
		return repository.findAll();
	}
	
	public Person getMyObject() {
		return repository.findByIsMe(true);
	}
}
