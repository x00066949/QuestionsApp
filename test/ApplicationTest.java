package controllers;

//

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
//import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
//

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.avaje.ebean.*;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import java.sql.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.*;
import java.util.*;
import javax.persistence.*;


import play.mvc.*;
import play.test.*;
//import play.test.Helpers.callAction;
import play.data.DynamicForm;
import static play.mvc.Http.Status.*;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


// Import models
import models.*;
import controllers.*;
import views.html.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {
/* 	static Database database;
	static Connection connection; */
	public static FakeApplication app;



	@BeforeClass	
	public static void startApplication(){
		System.out.println("Starting unit tests");

		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);
		
		System.out.println("Application Started");
		
		Category c = new Category(Long.valueOf(1),"Test Category");
		Ebean.save(c);
		System.out.println("test category inserted to db");
		
		Question q = new Question(Long.valueOf(2),c,"A test Question");
		Ebean.save(q);
		Question q1 = new Question(Long.valueOf(3),c,"Another Question");
		Ebean.save(q1);
		System.out.println("test questions inserted to db");
		

	}
	
	@AfterClass
	public static void shutdownDatabase() {
		System.out.println("Finishing unit tests");
		
		if (Category.findAll() != null){
			Ebean.delete(QuestionRate.findAll());
			Ebean.delete(Question.findAll());
			Ebean.delete(Interview.findAll());
			Ebean.delete(Candidate.findAll());
			
			Ebean.delete(Category.findAll());
			
			System.out.println("Emptying database");

			
		}

        Helpers.stop(app);
		
		System.out.println("Application Stopped");

	}

	
    @Test
    public void simpleCheck() {
		System.out.println("example unit test");

        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderIndex() {
		System.out.println("Testing index page works");

	
        Content html = views.html.index.render(Category.findAllCategories(),"Index page working.");
        assertEquals("text/html", contentType(html));

        assertTrue(contentAsString(html).contains("page"));
		
		System.out.println("Finished index page test");

    }
	
	
 	
	@Test
	public void callListQuestions() {
		System.out.println("Testing List Questions Controller");


        Content html = views.html.listQuestions.render(Category.findAllCategories(),"Test Category");

		Set<Category> categories = Category.findAllCategories();
		assertEquals("text/html", contentType(html));
		System.out.println(contentAsString(html));
        assertTrue(contentAsString(html).contains("A test Question"));

		

		System.out.println("finished test for List Questions controller");

	} 
	
	@Test
	public void generateQuestions() {
		System.out.println("Testing Questions Generator Controller");

		Result result = Helpers.route(Helpers.fakeRequest(controllers.routes.QuestionCtrl.getCategories("all")));
        

		System.out.println("finished test for List Questions controller");

	} 
	
	@Test
	public void testAddCandidate() {
		System.out.println("Testing AddCandidate controller");

		Candidate inputData = DataSimulator.createCandidate();
		Call action = controllers.routes.CandidateCtrl.addCandidate();
		Result result = route(Helpers.fakeRequest(action).bodyJson(Json.toJson(inputData)));
        

		System.out.println("finished test for List Questions controller");

	
	}

}
