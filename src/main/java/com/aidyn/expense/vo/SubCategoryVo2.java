package com.aidyn.expense.vo;

import java.util.Set;

import com.aidyn.expense.model.Tags;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryVo2 {

	private int id;
	private Set<Tags> tags;
	
}
