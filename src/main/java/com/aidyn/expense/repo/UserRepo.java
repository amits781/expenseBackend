package com.aidyn.expense.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Person;

@Repository
public interface UserRepo extends CrudRepository<Person, Long> {

	public List<Person> findAll();
	
	public Person findByIsMe(boolean value);
}
