package com.aidyn.expense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aidyn.expense.model.SubCategory;
import com.aidyn.expense.service.SubCategoryService;
import com.aidyn.expense.vo.SubCategoryVo;
import com.aidyn.expense.vo.SubCategoryVo2;

@RestController
@RequestMapping("/subCat")
public class SubCategoryController extends BaseController{

	
	@Autowired
	SubCategoryService service;
	
	@GetMapping("/all")
	public List<SubCategory> getAllSubCategories(){
		return service.getAll();
	}
	
	@GetMapping("/name/{name}")
	public SubCategory getByName(@PathVariable String name){
		return service.getSubCatByName(name);
	}
	
	@GetMapping("/all/category/{catName}")
	public List<SubCategory> getAllByCategory(@PathVariable String catName){
		return service.getAllByCategory(catName);
	}
	
	@PostMapping("/save")
	public void saveSubCat(@RequestBody SubCategoryVo subcatVo) throws Exception {
		//System.out.println(subcatVo.getName() + "||" + subcatVo.getCategory().getName());
		service.save(subcatVo);
	}
	
	@PostMapping("/save/tags")
	public void saveSubCatTags(@RequestBody SubCategoryVo2 subcatVo) throws Exception {
		service.saveTags(subcatVo);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteSubCat(@PathVariable int id) {
		service.delete(id);
	}
}
