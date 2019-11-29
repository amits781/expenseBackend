package com.aidyn.expense.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aidyn.expense.dao.SubCategoryDao;
import com.aidyn.expense.model.SubCategory;
import com.aidyn.expense.vo.SubCategoryVo;
import com.aidyn.expense.vo.SubCategoryVo2;

@Service
public class SubCategoryService {

	@Autowired
	SubCategoryDao subDao;
	
	@Autowired
	CategoryService catService;
	
	
	public void save(SubCategoryVo subCat) throws Exception {
		if((subDao.getSubCatByName(subCat.getName()) == null)
				&& (catService.getCatByName(subCat.getCategory().getName()) != null)) {
			if(subCat.getName()!=null && subCat.getName() !="" ) {
			SubCategory subCategory = new SubCategory();
			subCategory.setName(subCat.getName());
			subCategory.setCategory(catService.getCatByName(subCat.getCategory().getName()));
			subDao.saveSubCat(subCategory);
			}else {
				throw new Exception("Empty Sub Category");
			}
		} else {
			throw new Exception("Error adding sub category");
		}
			
	}
	
	public void saveTags(SubCategoryVo2 subCat) throws Exception {
		if((subDao.getSubCatById(subCat.getId())) != null) {
			SubCategory subCategory = subDao.getSubCatById(subCat.getId());
			subCategory.getTags().addAll(subCat.getTags());
			subDao.saveSubCat(subCategory);
		} else {
			throw new Exception("Error adding sub category");
		}
			
	}
	
	public void save(SubCategory entity) {
		subDao.saveSubCat(entity);
	}
	
	public SubCategory getSubCatByName(String name) {
		return subDao.getSubCatByName(name);
	}
	
	public List<SubCategory> getAllByCategory(String name){
		return subDao.getAllByCategory(name);
	}
	
	public List<SubCategory> getAll(){
		return subDao.getAll();
	}

	public void delete(int id) {
		subDao.delete(id);
		
	}
}
