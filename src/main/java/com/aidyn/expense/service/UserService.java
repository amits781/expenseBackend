package com.aidyn.expense.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aidyn.expense.dao.UserDao;
import com.aidyn.expense.model.Person;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	UserDao dao;
	
	private static JsonParser parser = new JsonParser();
	private static Gson gson = new Gson();
	
	
	public Person createUser(String userJsonString) {
		JsonElement userJson = parser.parse(userJsonString).getAsJsonObject().get("user");
		return saveUser(getPersonObject(userJson,true));
	}
	
	public void createUserFriend(String userJsonString) {
		JsonElement userFriendJson = parser.parse(userJsonString).getAsJsonObject().get("friends");
		for (JsonElement userJson : userFriendJson.getAsJsonArray()) {
			saveUser(getPersonObject(userJson, false));
		}
	}
	
	private Person saveUser(Person person) {
		if(dao.hasEntry(person.getId())) {
			log.debug("Person Already Exists :" + person.getFirst_name());
		} else {
			dao.save(person);
			log.debug("Person Added to DB :" + person.toString());
		}
		return person;	
	}
	
	private Person getPersonObject(JsonElement json, boolean isMe) {
		Person person = gson.fromJson(json, Person.class);
		person.setMe(isMe);
		if(person.getFirst_name()!=null)
			return person;
		log.debug("Error in person JSON");
		return null;
	}
	
	public List<Person> getAll(){
		return dao.getAll();
	}

	public Person getCurrentUserObject() {
		return dao.getMyObject();
	}
}
