package com.aidyn.expense.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarEvent {
	private String title;
	private String date;
	boolean allDay;
	private String backgroundColor;
	private String textColor;
	private String borderColor;

}
