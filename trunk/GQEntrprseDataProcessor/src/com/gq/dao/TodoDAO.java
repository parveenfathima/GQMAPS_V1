package com.gq.dao;


import java.util.HashMap;
import java.util.Map;

import com.gq.model.Todo;


public enum TodoDAO {
	instance;
	
	private Map<String, Todo> contentProvider = new HashMap<String, Todo>();
	
	private TodoDAO() {
		
		Todo todo = new Todo("1", "Learn REST");
		contentProvider.put("1", todo);
		todo = new Todo("2", "Do something");
		contentProvider.put("2", todo);
		
	}
	public Map<String, Todo> getModel(){
		return contentProvider;
	}
	
}

