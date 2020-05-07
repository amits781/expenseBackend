package com.aidyn.expense.service;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aidyn.expense.dao.ExpenseDao;
import com.aidyn.expense.dto.ExpenseDTO;
import com.aidyn.expense.dto.UserDetailsDTO;
import com.aidyn.expense.model.Expense;
import com.aidyn.expense.model.SubCategory;
import com.aidyn.expense.model.Tags;
import com.aidyn.expense.vo.CalendarEvent;
import com.aidyn.expense.vo.ExpenseListVO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExpenseService {

	@Autowired
	UserService userService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SubCategoryService subCategoryService;

	@Autowired
	TagsService tagsService;

	@Autowired
	ExpenseDao dao;

	@Autowired
	RestTemplate restTemplate;

	private static JsonParser parser = new JsonParser();
	private static Gson gson = new Gson();

	@Scheduled(fixedDelay = 15000)
	public void scheduleFixedDelayTask() {
		try {
			String quote = restTemplate.getForObject("https://expense-app781.herokuapp.com/splitwise/get",
					String.class);
			System.out.println("rest call: " + quote.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
	}

	/*
	 * private JsonElement findCurrentUserEntry(JsonElement expenseUserDetails, Long
	 * id) { for (JsonElement expenseUserJson :
	 * expenseUserDetails.getAsJsonObject().get("users") .getAsJsonArray()) { if
	 * (expenseUserJson.getAsJsonObject().get("user_id").getAsLong() == id) {
	 * log.debug("Expense details user record found"); return expenseUserJson; } }
	 * log.debug("Expense record not found"); return null; }
	 * 
	 * private String findCurrentUserPaymentInfo(JsonElement expenseUserDetails,
	 * Long id) { for (JsonElement expenseUserJson :
	 * expenseUserDetails.getAsJsonObject().get("repayments") .getAsJsonArray()) {
	 * if (expenseUserJson.getAsJsonObject().get("from").getAsLong() == id) {
	 * log.debug("Payment Received found"); return "received"; } }
	 * log.debug("Payment done"); return "done"; }
	 */

	public List<ExpenseDTO> parseExpenseList(String expensesJsonString) {
		Type userListType = new TypeToken<ArrayList<ExpenseDTO>>() {
		}.getType();
		JsonElement userExpensesJson = parser.parse(expensesJsonString).getAsJsonObject().get("expenses");
		return gson.fromJson(userExpensesJson.getAsJsonArray(), userListType);
	}

	public void createExpenseV2(String expensesJsonString) {
		List<ExpenseDTO> expenseList = parseExpenseList(expensesJsonString);
		Long myId = userService.getCurrentUserObject().getId();
		for (ExpenseDTO expense : expenseList) {
			String payment = "expense";
			Expense newExpense = null;
			boolean isDeleted = expense.getDeleted_at() == null ? false : true;
			if (isDeleted) {
				Long id = expense.getId();
				log.debug("Deleted : " + isDeleted + " | " + id);
				if (dao.hasEntry(id)) {
					log.debug("Deleted from DB: " + isDeleted + " | " + id);
					dao.deleteById(id);
				}
				continue;
			}

			for (UserDetailsDTO user : expense.getUsers()) {
				if (Objects.equals(user.getUser().getId(), myId)) {
					newExpense = new Expense();
					log.debug("Expense details user record found for : " + expense.getDescription());
					newExpense.setApproved(false);
					newExpense.setDate(expense.getDate());
					newExpense.setDescription(expense.getDescription());
					newExpense.setId(expense.getId());
					newExpense.setTotalCost(expense.getCost());
					newExpense.setMyOwedShare(user.getOwed_share());
					newExpense.setMyPaidShare(user.getPaid_share());

					if (expense.getPayment() && (user.getPaid_share() > 0.0f)) {
						newExpense.setMyOwedShare(0.0f);
						payment = "done";
						log.debug("Payment done" + expense.getCost());
					} else if (expense.getPayment() && (user.getPaid_share() == 0.0f)) {
						payment = "received";
					}

					if (payment.equalsIgnoreCase("done") || payment.equalsIgnoreCase("expense")) {
						saveExpense(newExpense);
						break;
					}
				}
			}

		}
	}

	/*
	 * public void createExpenses(String expensesJsonString) throws ParseException {
	 * JsonElement userExpensesJson =
	 * parser.parse(expensesJsonString).getAsJsonObject().get("expenses"); Long myId
	 * = userService.getCurrentUserObject().getId(); SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"); for (JsonElement expenseJson :
	 * userExpensesJson.getAsJsonArray()) { JsonElement myShareEntry =
	 * findCurrentUserEntry(expenseJson, myId); String paymentStatus = "expense";
	 * boolean isDeleted =
	 * expenseJson.getAsJsonObject().get("deleted_at").isJsonNull() ? false : true;
	 * if (isDeleted) { long id =
	 * expenseJson.getAsJsonObject().get("id").getAsLong(); log.debug("Deleted : " +
	 * isDeleted + " | " + id); if (dao.hasEntry(id)) {
	 * log.debug("Deleted from DB: " + isDeleted + " | " + id); dao.deleteById(id);
	 * } } if (expenseJson.getAsJsonObject().get("payment").getAsBoolean())
	 * paymentStatus = findCurrentUserPaymentInfo(expenseJson, myId); if
	 * (myShareEntry != null && !isDeleted &&
	 * (paymentStatus.equalsIgnoreCase("done") ||
	 * paymentStatus.equalsIgnoreCase("expense"))) {
	 * saveExpense(createExpenseObject(expenseJson, myShareEntry, format,
	 * paymentStatus)); } } }
	 * 
	 * private Expense createExpenseObject(JsonElement expenseJson, JsonElement
	 * myShareEntry, SimpleDateFormat format, String paymentStatus) throws
	 * ParseException { Expense expense = new Expense();
	 * expense.setId(expenseJson.getAsJsonObject().get("id").getAsLong()); String
	 * dateString = expenseJson.getAsJsonObject().get("date").getAsString();
	 * dateString = dateString.substring(0, dateString.length() - 1); Date date =
	 * format.parse(dateString); expense.setDate(date);
	 * expense.setDescription(expenseJson.getAsJsonObject().get("description").
	 * getAsString());
	 * expense.setTotalCost(expenseJson.getAsJsonObject().get("cost").getAsFloat());
	 * expense.setMyPaidShare(myShareEntry.getAsJsonObject().get("paid_share").
	 * getAsFloat()); if (paymentStatus.equalsIgnoreCase("done")) {
	 * expense.setMyOwedShare(0.0f); } else {
	 * expense.setMyOwedShare(myShareEntry.getAsJsonObject().get("owed_share").
	 * getAsFloat()); } return expense; }
	 */
	private void saveExpense(Expense expense) {
		Boolean updated = false;
		if (dao.hasEntry(expense.getId())) {
			log.debug("Expense Already Exists :" + expense.getDescription());
			Expense savedExpense = dao.getExpenseById(expense.getId());
			if (savedExpense.getMyOwedShare() != expense.getMyOwedShare()) {
				updated = true;
				savedExpense.setMyOwedShare(expense.getMyOwedShare());
			}
			if (!savedExpense.getDescription().equalsIgnoreCase(expense.getDescription())) {
				updated = true;
				savedExpense.setDescription(expense.getDescription());
			}
			if (savedExpense.getMyPaidShare() != expense.getMyPaidShare()) {
				updated = true;
				savedExpense.setMyPaidShare(expense.getMyPaidShare());
			}
			if (savedExpense.getTotalCost() != expense.getTotalCost()) {
				updated = true;
				savedExpense.setTotalCost(expense.getTotalCost());
			}
			if (updated) {
				log.debug("Expense Updated :" + expense.getDescription());
				dao.save(savedExpense);
			}
		} else {
			expense = suggestExpenseCategory(expense);
			dao.save(expense);
			log.debug("Expense Added to Entry :" + expense.getDescription());
		}

	}

	public Expense suggestExpenseCategory(Expense expense) {
		int max = 0;
		String category = null;
		for (SubCategory subcat : subCategoryService.getAll()) {
			int count = 0;
			for (Tags tag : subcat.getTags()) {
				if (tag.getName().contains(expense.getDescription())) {
					count++;
				}
			}
			if (count > max) {
				max = count;
				category = subcat.getName();
			}
		}
		if (category != null) {
			SubCategory subCat = subCategoryService.getSubCatByName(category);
			expense.setSubCategory(category);
			expense.setCategory(subCat.getCategory().getName());
			expense.setSuggested(true);
			expense.setApproved(false);
		}
		return expense;
	}

	public void updateExpense(Expense expense) {
		dao.save(expense);
	}

	public void saveControllerExpense(Expense expense) {
		dao.save(expense);
	}

	public Expense getById(Long id) {
		return dao.getExpenseById(id);
	}

	public List<Expense> getAllExpense() {
		return dao.getAll();
	}

	public void updateCategory(Expense exp) throws Exception {
		log.debug("--- exp cat received : " + exp.getSubCategory());
		if (exp.getSubCategory() != null) {
			SubCategory subCat = subCategoryService.getSubCatByName(exp.getSubCategory());
			Expense savedExp = dao.getExpenseById(exp.getId());
			if (savedExp.getSubCategory() == null || savedExp.getSubCategory() == "") {
				savedExp.setSubCategory(subCat.getName());
				savedExp.setCategory(subCat.getCategory().getName());
				savedExp.setApproved(true);
				savedExp.setSuggested(false);
				Tags newTag = new Tags();
				newTag.setName(savedExp.getDescription());
				if (subCat.getTags() == null) {
					Set<Tags> tagList = new HashSet<>();
					tagList.add(newTag);
					subCat.setTags(tagList);
				} else {
					Set<Tags> subCatTags = subCat.getTags();
					boolean found = false;
					for (Tags tag : subCatTags) {
						if (tag.getName().equalsIgnoreCase(savedExp.getDescription()))
							found = true;
					}
					if (!found)
						subCat.getTags().add(newTag);
				}
			} else {
				SubCategory savedSubCat = subCategoryService.getSubCatByName(savedExp.getSubCategory());
				if (savedSubCat.getTags() != null) {
					Set<Tags> tagList = savedSubCat.getTags();
					Tags tagToRemove = null;
					for (Tags tag : tagList) {
						if (tag.getName().equalsIgnoreCase(savedExp.getDescription())) {
							tagToRemove = tag;
						}
					}
					if (tagToRemove != null)
						tagList.remove(tagToRemove);
					subCategoryService.save(savedSubCat);
				}
				if (!savedExp.getSubCategory().equalsIgnoreCase(subCat.getName())) {
					savedExp.setSuggested(false);
				}
				savedExp.setSubCategory(subCat.getName());
				savedExp.setCategory(subCat.getCategory().getName());
				Tags newTag = new Tags();
				savedExp.setApproved(true);
				newTag.setName(savedExp.getDescription());
				if (subCat.getTags() == null) {
					Set<Tags> tagListToSave = new HashSet<>();
					tagListToSave.add(newTag);
					subCat.setTags(tagListToSave);
				} else {
					Set<Tags> subCatTags = subCat.getTags();
					boolean found = false;
					for (Tags tag : subCatTags) {
						if (tag.getName().equalsIgnoreCase(savedExp.getDescription()))
							found = true;
					}
					if (!found)
						subCat.getTags().add(newTag);
				}
			}
			dao.save(savedExp);
		} else {
			throw new Exception("Cannot set empty category");
		}
	}

	public List<CalendarEvent> getExpensesByDay() {
		List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df = new DecimalFormat("0.00");

		List<Expense> allExpense = getAllExpense();
		Map<String, Float> groupedDateOwedExpense = new HashMap<>();
		for (Expense expense : allExpense) {
			String date = formatter.format(expense.getDate().getTime() + 19800000) + "T00:00:00";
			if (groupedDateOwedExpense.containsKey(date)) {
				Float newTotal = groupedDateOwedExpense.get(date) + expense.getMyOwedShare();
				groupedDateOwedExpense.put(date, newTotal);
			} else {
				groupedDateOwedExpense.put(date, expense.getMyOwedShare());
			}
		}

		for (Map.Entry<String, Float> expenses : groupedDateOwedExpense.entrySet()) {
			CalendarEvent event = new CalendarEvent();
			event.setAllDay(true);
			event.setDate(expenses.getKey());
			event.setTitle("Owed: ₹ " + df.format(expenses.getValue()));
			event.setBackgroundColor("#fff");
			event.setTextColor("#c62828");
			event.setBorderColor("#c62828");
			events.add(event);
		}

		Map<String, Float> groupedDatePaidExpense = new HashMap<>();
		for (Expense expense : allExpense) {
			String date = formatter.format(expense.getDate().getTime() + 19800000) + "T00:00:00";
			if (groupedDatePaidExpense.containsKey(date)) {
				Float newTotal = groupedDatePaidExpense.get(date) + expense.getMyPaidShare();
				groupedDatePaidExpense.put(date, newTotal);
			} else {
				groupedDatePaidExpense.put(date, expense.getMyPaidShare());
			}
		}

		for (Map.Entry<String, Float> expenses : groupedDatePaidExpense.entrySet()) {
			CalendarEvent event = new CalendarEvent();
			event.setDate(expenses.getKey());
			event.setAllDay(true);
			event.setTitle("Paid: ₹ " + df.format(expenses.getValue()));
			event.setBackgroundColor("#fff");
			event.setBorderColor("#2e7d32");
			event.setTextColor("#2e7d32");
			events.add(event);
		}

		Map<String, Float> groupedDateTotalExpense = new HashMap<>();
		for (Expense expense : allExpense) {
			String date = formatter.format(expense.getDate().getTime() + 19800000) + "T00:00:00";
			if (groupedDateTotalExpense.containsKey(date)) {
				Float newTotal = groupedDateTotalExpense.get(date) + expense.getTotalCost();
				groupedDateTotalExpense.put(date, newTotal);
			} else {
				groupedDateTotalExpense.put(date, expense.getTotalCost());
			}
		}

		for (Map.Entry<String, Float> expenses : groupedDateTotalExpense.entrySet()) {
			CalendarEvent event = new CalendarEvent();
			event.setAllDay(true);
			event.setDate(expenses.getKey());
			event.setTitle("Total: ₹ " + df.format(expenses.getValue()));
			event.setBorderColor("#283593");
			event.setBackgroundColor("#fff");
			event.setTextColor("#283593");
			events.add(event);
		}
		return events;
	}

	public List<ExpenseListVO> getAllExpenseByGivenDay(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));

		List<ExpenseListVO> expenseList = new ArrayList<ExpenseListVO>();
		List<Expense> allExpense = getAllExpense();
		for (Expense expense : allExpense) {
			log.debug("--- Expense date : " + formatter.format(expense.getDate()));
			if ((formatter.format(expense.getDate()).equalsIgnoreCase(formatter.format(date)))) {
				log.debug("--- Expense date found : " + formatter.format(expense.getDate()));
				ExpenseListVO expenseVo = gson.fromJson(gson.toJson(expense), ExpenseListVO.class);
				expenseList.add(expenseVo);
			}
		}
		return expenseList;
	}

	public List<Expense> getAllUnapprovedExpenses() {
		return dao.getAllUnApprovedExpenses();
	}
}
