package com.gq.restsvcs;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.gq.dao.TodoDAO;
import com.gq.model.Todo;


public class TodoSvc {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;
	public TodoSvc(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	//Application integration 		
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Todo getTodo() {
		System.out.println("todo-calling  getTodo");
		Todo todo = TodoDAO.instance.getModel().get(id);
		if(todo==null)
			throw new RuntimeException("Get: Todo with " + id +  " not found");
		return todo;
	}
	
	// For the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Todo getTodoHTML() {
		System.out.println("todo-calling  getTodoHTML");

		Todo todo = TodoDAO.instance.getModel().get(id);
		if(todo==null)
			throw new RuntimeException("Get: Todo with " + id +  " not found");
		return todo;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putTodo(JAXBElement<Todo> todo) {
		System.out.println("todo-calling  putTodo");

		Todo c = todo.getValue();
		return putAndGetResponse(c);
	}
	
	@DELETE
	public void deleteTodo() {		
		System.out.println("todo-calling  deleteTodo");

		Todo c = TodoDAO.instance.getModel().remove(id);
		if(c==null)
			throw new RuntimeException("Delete: Todo with " + id +  " not found");
	}
	
	private Response putAndGetResponse(Todo todo) {
		Response res;
		if(TodoDAO.instance.getModel().containsKey(todo.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		TodoDAO.instance.getModel().put(todo.getId(), todo);
		return res;
	}
	
	

}