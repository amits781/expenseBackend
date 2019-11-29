package com.aidyn.expense.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aidyn.expense.service.ExpenseService;
import com.aidyn.expense.service.UserService;
import com.aidyn.expense.splitwise.Splitwise;
import com.github.scribejava.core.model.OAuth1AccessToken;



@RestController
@RequestMapping("/splitwise")
public class SplitwiseController extends BaseController{

	public Splitwise splitwise;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ExpenseService expenseService;
	
	@GetMapping("/url")
	public String getAuthUrl() throws InterruptedException, ExecutionException, IOException {
		//splitwise = new Splitwise("LkeSXZcNJmVi8jmyxBZEZw0bJl2SYEE5MQz3X2E2", "3SzfwJc1gTwbi3ftsf2nkrWzOLSaQUoRYqY1Qzwc");
		splitwise = new Splitwise("HcSuiFhrgASS9Nt91sNgI8ZvUQRxQn4sB3cogng2", "Hw113B6qFCn6r53vCKZ908t2BmiKdSDhRkcKq2c2");
		String authorizationURL = splitwise.getAuthorizationUrl();
		return "<a href="+authorizationURL+">"+authorizationURL+"</a>";
		}
	
	/*
	 * @PostMapping("/init") public void initLogin(@RequestParam String token)
	 * throws InterruptedException, ExecutionException, IOException {
	 * splitwise.getUtil().setAccessToken(token); OAuth1AccessToken accessToken =
	 * (OAuth1AccessToken) splitwise.getUtil().getAccessToken();
	 * splitwise.getUtil().setAccessToken(accessToken.getToken(),
	 * accessToken.getTokenSecret(), accessToken.getRawResponse() ); }
	 */
	
	@GetMapping("/init")
	public String initLoginDefault(@RequestParam String oauth_verifier, @RequestParam String oauth_token) throws Exception {
		splitwise.getUtil().setAccessToken(oauth_verifier);
		OAuth1AccessToken accessToken = (OAuth1AccessToken) splitwise.getUtil().getAccessToken();
		splitwise.getUtil().setAccessToken(accessToken.getToken(),
		                accessToken.getTokenSecret(),
		                accessToken.getRawResponse()
		        );
		if(splitwise.getUtil().getAccessToken() != null) {
			getCurrentUser();
			getExpenses();
			getCurrentUserFriend();/*
									 * return "Splitwise initialized successfully" +
									 * "<br><a href='/homepage'>Go Home</a>";
									 */
			
			  return "Splitwise initialized successfully" +
			  "<br><a href='https://expense781ui.herokuapp.com/dash'>Go Home</a>";
			 
		}
		return "Error in initialization";
	}
	
	@GetMapping("/getUser")
	public String getCurrentUser() throws Exception {
		String userJson = splitwise.getCurrentUser();
		String userName = userService.createUser(userJson).getFirst_name();
		return userName;
	}
	
	@GetMapping("/getFriends")
	public String getCurrentUserFriend() throws Exception {
		String userJsonFriends = splitwise.getFriends();
		userService.createUserFriend(userJsonFriends);
		return userJsonFriends;
	}
	
	@GetMapping("/getExpenses")
	public String getExpenses() throws Exception {
		String expensesJsonString = splitwise.getExpenses();
		expenseService.createExpenses(expensesJsonString);
		return expensesJsonString;
	}
	
	@GetMapping("/refreshSplitwise")
	public void refreshSplitStatus() throws Exception {
		if(splitwise != null) {
			getCurrentUser();
			getCurrentUserFriend();
			getExpenses();
		} else {
			throw new Exception("Splitwise not login");
		}
	}
	
	@GetMapping("/get")
	public List<String> getSplitWiseInitData() throws Exception {
		List<String> splitwiseData = new ArrayList<String>();
		if(splitwise != null && splitwise.getUtil().getAccessToken() != null) {
			splitwiseData.add("true");
			splitwiseData.add(getCurrentUser());
		}
		return splitwiseData;
	}
}
