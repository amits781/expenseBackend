package com.aidyn.expense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aidyn.expense.dao.TagDao;
import com.aidyn.expense.model.Tags;

@Service
public class TagsService {

	@Autowired
	TagDao dao;
	
	public Tags getTagById(int id) {
		return dao.getTagById(id);
	}
	
	public void saveTag(Tags tag) {
		dao.save(tag);
	}
	
	public void deleteTagById(int id) {
		dao.deleteById(id);
	}
}
