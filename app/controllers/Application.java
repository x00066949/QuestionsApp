package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public Result index() {
		System.out.println("Index page");

        return ok(index.render(Category.findAllCategories(), "Home Page"));
    }
	
	public Result showGraph() {
		System.out.println("Graph page");

        return ok(graph.render(Category.findAll()));
    }

}
