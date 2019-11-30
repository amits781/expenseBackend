package com.aidyn.expense.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.aidyn.expense.dto.ExpenseDTO;
import com.aidyn.expense.service.ExpenseService;
import com.aidyn.expense.service.UserService;
import com.aidyn.expense.splitwise.Splitwise;
import com.github.scribejava.core.model.OAuth1AccessToken;



@RestController
@RequestMapping("/splitwise")
public class SplitwiseController extends BaseController {

  public Splitwise splitwise;

  @Value("${splitwise.consumer.key}")
  private String consumerKey;

  @Value("${splitwise.consumer.secret}")
  private String consumerSecret;

  @Value("${app.url}")
  private String appUrl;

  @Value("${app.environment}")
  private String appEnv;

  @Autowired
  UserService userService;

  @Autowired
  ExpenseService expenseService;

  @GetMapping("/url")
  public String getAuthUrl() throws InterruptedException, ExecutionException, IOException {
    // splitwise = new Splitwise("LkeSXZcNJmVi8jmyxBZEZw0bJl2SYEE5MQz3X2E2",
    // "3SzfwJc1gTwbi3ftsf2nkrWzOLSaQUoRYqY1Qzwc");
    splitwise = new Splitwise(consumerKey, consumerSecret);
    String authorizationURL = splitwise.getAuthorizationUrl();
    return "<a href=" + authorizationURL + ">" + authorizationURL + "</a>";
  }

  /*
   * @PostMapping("/init") public void initLogin(@RequestParam String token) throws
   * InterruptedException, ExecutionException, IOException {
   * splitwise.getUtil().setAccessToken(token); OAuth1AccessToken accessToken = (OAuth1AccessToken)
   * splitwise.getUtil().getAccessToken();
   * splitwise.getUtil().setAccessToken(accessToken.getToken(), accessToken.getTokenSecret(),
   * accessToken.getRawResponse() ); }
   */

  @GetMapping("/init")
  public RedirectView initLoginDefault(@RequestParam String oauth_verifier,
      @RequestParam String oauth_token, RedirectAttributes attributes) throws Exception {
    splitwise.getUtil().setAccessToken(oauth_verifier);
    OAuth1AccessToken accessToken = (OAuth1AccessToken) splitwise.getUtil().getAccessToken();
    splitwise.getUtil().setAccessToken(accessToken.getToken(), accessToken.getTokenSecret(),
        accessToken.getRawResponse());
    if (splitwise.getUtil().getAccessToken() != null) {
      getCurrentUser();
      getExpenses();
      getCurrentUserFriend();
    }
    return new RedirectView(appUrl + "/dash");
  }

  @GetMapping("/redirectWithRedirectView")
  public RedirectView redirectWithUsingRedirectView(RedirectAttributes attributes) {
    attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
    attributes.addAttribute("attribute", "redirectWithRedirectView");
    return new RedirectView("redirectedUrl");
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
  public List<ExpenseDTO> getExpenses() throws Exception {
    String expensesJsonString = splitwise.getExpenses();
    expenseService.createExpenseV2(expensesJsonString);
    return expenseService.parseExpenseList(expensesJsonString);
  }

  @GetMapping("/refreshSplitwise")
  public void refreshSplitStatus() throws Exception {
    if (splitwise != null) {
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
    if (splitwise != null && splitwise.getUtil().getAccessToken() != null) {
      splitwiseData.add("true");
      splitwiseData.add(getCurrentUser());
    }
    return splitwiseData;
  }


}
