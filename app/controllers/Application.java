package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	@Security.Authenticated(Secured.class)
    public Result index() {
		System.out.println("Index page");

        return ok(index.render("Home Page", Interviewer.getLoggedIn(session().get("username"))));
    }
	
	@Security.Authenticated(Secured.class)
	public Result showGraph() {
		System.out.println("Graph page");

        return ok(graph.render(Category.findAll(), Interviewer.getLoggedIn(session().get("username"))));
    }

}
