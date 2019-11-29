
package com.aidyn.expense.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aidyn.expense.model.Expense;
import com.aidyn.expense.service.ExpenseService;
import com.aidyn.expense.vo.CalendarEvent;
import com.aidyn.expense.vo.ExpenseListVO;

@RestController
@RequestMapping("/expense")
public class ExpenseController extends BaseController{

	@Autowired
	ExpenseService service;
	
	@GetMapping("/all")
	public List<Expense> getAllExpense(){
		return service.getAllExpense();
	}
	
	@GetMapping("/all/unapproved")
	public List<Expense> getAllUnApprovedExpense(){
		return service.getAllUnapprovedExpenses();
	}
	
	@GetMapping("/all/day")
	public List<CalendarEvent> getAllExpenseByDay(){
		return service.getExpensesByDay();
	}
	
	@PostMapping("/save")
	public void saveExpense(@RequestBody Expense exp) {
		service.saveControllerExpense(exp);
	}
	
	@PostMapping("/updateCategory")
	public void updateCategory(@RequestBody Expense exp) throws Exception {
		service.updateCategory(exp);
	}
	
	@PostMapping("/getAllExpenseByDay")
	public List<ExpenseListVO> getExpenses(@RequestBody Date date){
		return service.getAllExpenseByGivenDay(date);
	}
}
