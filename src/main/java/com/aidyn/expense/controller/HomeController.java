package com.aidyn.expense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aidyn.expense.model.Expense;
import com.aidyn.expense.service.ExpenseService;
import com.aidyn.expense.service.UserService;
import com.aidyn.expense.splitwise.Splitwise;

@Controller
public class HomeController {

	@Autowired
	SplitwiseController splitwiseController;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ExpenseService expenseService;
	
	Splitwise splitwise;

	@GetMapping("/homepage")
	public String homepage(Model model) {
		splitwise = splitwiseController.splitwise;
		if(splitwise != null && splitwise.getUtil().getAccessToken() != null) {
			model.addAttribute("init", "true");
			model.addAttribute("name",userService.getCurrentUserObject().getFirst_name());
		} else {
			model.addAttribute("init", "false");
		}
		return "homepage";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		splitwise = splitwiseController.splitwise;
		if(splitwise != null && splitwise.getUtil().getAccessToken() != null) {
			model.addAttribute("name",userService.getCurrentUserObject().getFirst_name());
			model.addAttribute("init", "true");
		} else {
			model.addAttribute("name","Anonymous");
			model.addAttribute("init", "false");
		}
		return "dashboard";
	}
	
	@GetMapping("/expenses")
	public String dashboard_expenses(Model model) {
		splitwise = splitwiseController.splitwise;
		if(splitwise != null && splitwise.getUtil().getAccessToken() != null) {
			model.addAttribute("name",userService.getCurrentUserObject().getFirst_name());
			model.addAttribute("init", "true");
		} else {
			model.addAttribute("name","Anonymous");
			model.addAttribute("init", "false");
		}
		model = setExpenses(model);
		return "allexpense";
	}
	
	@PostMapping("/getName")
	public String getName(Model model, @RequestParam("username") String username) {
		model.addAttribute("name", username);
		return "homepage";
	}
	
	@PostMapping("/category/update")
	public String updateCategory(Model model, @RequestParam("id") Long id, @RequestParam("category") String category) {
		Expense exp = expenseService.getById(id);
		//exp.setCategory(category);
		expenseService.updateExpense(exp);
		model = setExpenses(model);
		splitwise = splitwiseController.splitwise;
		if(splitwise != null && splitwise.getUtil().getAccessToken() != null) {
			model.addAttribute("name",userService.getCurrentUserObject().getFirst_name());
			model.addAttribute("init", "true");
		} else {
			model.addAttribute("name","Anonymous");
			model.addAttribute("init", "false");
		}
		return "allexpense";
	}
	
	private Model setExpenses(Model model) {
		List<Expense> allExpenses  = expenseService.getAllExpense();
		model.addAttribute("expenses",allExpenses);
		return model;
	}
	
	@GetMapping(value = "/")
	public String index() {
		return "index";
	}
}
