package com.gq.restsvcs;



import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.gq.dao.TodoDAO;
import com.gq.model.Todo;


// Will map the resource to the URL todos
@Path("/todos")
public class TodoMasterSvc {

	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;


	// Return the list of todos to the user in the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Todo> getTodosBrowser() {
		System.out.println("calling text xml");
		List<Todo> todos = new ArrayList<Todo>();
		todos.addAll( TodoDAO.instance.getModel().values() );
		return todos; 
	}
	
	// Return the list of todos for applications
	@GET
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Todo> getTodos() {
		System.out.println("calling application xml");

		List<Todo> todos = new ArrayList<Todo>();
		todos.addAll( TodoDAO.instance.getModel().values() );
		return todos; 
	}
	
	
	// returns the number of todos
	// Use http://localhost:8080/de.vogella.jersey.todo/rest/todos/count
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		int count = TodoDAO.instance.getModel().size();
		return String.valueOf(count);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newTodo(
			@FormParam("id") String id,
			@FormParam("summary") String summary,
			@Context HttpServletResponse servletResponse
	) throws IOException {
		System.out.println("calling text html");

		Todo todo = new Todo(id,summary);
		TodoDAO.instance.getModel().put(id, todo);
		
		URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
		Response.created(uri).build();
		
		servletResponse.sendRedirect("../html/create_todo.html");
	}
	
	
	// Defines that the next path parameter after todos is
	// treated as a parameter and passed to the TodoSvcs
	// Allows to type http://localhost:8080/de.vogella.jersey.todo/rest/todos/1
	// 1 will be treaded as parameter todo and passed to TodoSvc
	@Path("{todo}")
	public TodoSvc getTodo( @PathParam("todo") String id) {
		return new TodoSvc(uriInfo, request, id);
	}

	@Path("{todoss},{jjj}")
	public TodoSvc getTodoDesc( @PathParam("todoss") String id , @PathParam("jjj") String jj) {
		System.out.println("calling getTodoDesc with {todoss} = " + id + " ,{jjj} = "+ jj);

		return new TodoSvc(uriInfo, request, id);
	}

}

			