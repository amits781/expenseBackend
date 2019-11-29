package com.aidyn.expense.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseListVO {

	long id;
	String description;
	float myPaidShare;
	float myOwedShare;
	float totalCost;
}
