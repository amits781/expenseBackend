package com.aidyn.expense.model;

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
public class Person {

	@Id
	long id;
	String first_name;
	String last_name;
	String email;
	String country_code;
	String date_format;
	String default_currency;
	boolean isMe;
	
	@Override
	public String toString() {
		String result = "id : "+id+"\n"+
				"first_name : "+first_name+"\n"+
				"last_name : "+last_name+"\n"+
				"email : "+email+"\n"+
				"country_code : "+country_code+"\n"+
				"date_format : "+date_format+"\n"+
				"default_currency : "+default_currency;
		return result;
	}
}
