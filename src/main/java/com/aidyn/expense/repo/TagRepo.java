package com.aidyn.expense.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.aidyn.expense.model.Tags;

@Repository
public interface TagRepo extends CrudRepository<Tags, Integer> {

	public List<Tags> findAll();
}
