package com.aidyn.expense.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expense {

	@Id
	long id;
	Date date;
	String description;
	String category;
	String subCategory;
	boolean isApproved;
	boolean isSuggested;
	float myPaidShare;
	float myOwedShare;
	float totalCost;
}
