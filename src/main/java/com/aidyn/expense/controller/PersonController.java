package com.aidyn.expense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aidyn.expense.model.Person;
import com.aidyn.expense.service.UserService;

@RestController
@RequestMapping("/user")
public class PersonController extends BaseController{

	
	@Autowired
	UserService service;
	
	@GetMapping("/all")
	public List<Person> getAllPerson(){
		return service.getAll();
	}
	
	@GetMapping("/me")
	public Person getMyObject(){
		return service.getCurrentUserObject();
	}
}
