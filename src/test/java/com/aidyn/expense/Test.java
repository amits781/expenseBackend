package com.aidyn.expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String args[]) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		Date date = format.parse("2019-08-29T18:32:46");
		
		System.out.println(date);
	}
}
