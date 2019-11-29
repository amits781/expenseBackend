package com.aidyn.expense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aidyn.expense.model.Category;
import com.aidyn.expense.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController{

	
	@Autowired
	CategoryService service;
	
	@GetMapping("/all")
	public List<Category> getAllCategory(){
		return service.getAll();
	}
	
	@GetMapping("/name/{name}")
	public Category getCategory(@PathVariable String name){
		return service.getCatByName(name);
	}
	
	@PostMapping("/save")
	public void saveCategory(@RequestBody Category category) throws Exception {
		service.saveCategory(category);
		
		//System.out.println(category.getName());
	}
}
