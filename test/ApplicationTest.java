package controllers;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.db.*;
import play.twirl.api.*;

import java.util.*;
import java.sql.*;
import javax.persistence.*;

import play.mvc.Http.RequestBuilder;
import play.mvc.Http;
import static org.mockito.Mockito.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.databind.JsonNode;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

// Import models
import models.*;
import controllers.*;
import views.html.*;


/**
*
* Simple (JUnit) tests that can call parts of a play app.
* 
*
*/
public class ApplicationTest {
/* 	static Database database;
	static Connection connection; */
	public static FakeApplication app;
    private final Http.Request request = mock(Http.Request.class);

	public static Interviewer i1 = new Interviewer(Long.valueOf(0), "Test Interviewer2", "interviewer", "pass");
	



	@BeforeClass	
	public static void startApplication(){
		System.out.println("Starting unit tests");

		//**
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
		i1.save();
		System.out.println("test interviewer inserted to db");

	}
	
	//http context
	@Before
    public void setUp() throws Exception {	
		System.out.println("Setting up http context");
		
		Map<String, String> flashData = Collections.emptyMap();
		Map<String, Object> argData = Collections.emptyMap();
		Long id = 2L;
		play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
		Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
		Http.Context.current.set(context);
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
		i1.delete();

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

	
        Content html = views.html.index.render("Index page working.",i1);
        assertEquals("text/html", contentType(html));

        assertTrue(contentAsString(html).contains("page"));
		
		System.out.println("Finished index page test");

    }
	
	@Test
	public void testBadRoute() {
		System.out.println("Testing bad route");

		RequestBuilder request1 = new RequestBuilder()
			.method(GET)
			.uri("/xx");

		Result result = route(request1);
		System.out.println("Status: "+result.status());

		assertEquals(NOT_FOUND, result.status());
	}
	
	@Test
	public void testWorkingRoutes() {
		System.out.println("Testing bad route");

		RequestBuilder request2 = new RequestBuilder()
			.method(GET)
			.uri("/");

		Result result = route(request2);
		System.out.println("Status: "+result.status());

		assertEquals(ACCEPTED, result.status());
	}

 	
	@Test
	public void callListQuestions() {
		System.out.println("Testing List Questions Controller");


        Content html = views.html.listQuestions.render(Category.findAllCategories(),"Test Category",i1);

		Set<Category> categories = Category.findAllCategories();
		assertEquals("text/html", contentType(html));
		//System.out.println(contentAsString(html));
        assertTrue(contentAsString(html).contains("A test Question"));

		

		System.out.println("finished test for List Questions controller");

	} 
	
	@Test
	public void generateQuestions() {
		System.out.println("Testing Questions Generator Controller");

		Result result = Helpers.route(Helpers.fakeRequest(controllers.routes.QuestionCtrl.getCategories("all")));
        

		System.out.println("finished test for List Questions controller");

	} 
/* 	
	@Test
	public void testAddCandidate() {
		System.out.println("Testing AddCandidate controller");

		Candidate inputData = DataSimulator.createCandidate();
		Call action = controllers.routes.CandidateCtrl.addCandidate();
		Result result = route(Helpers.fakeRequest(action).bodyJson(Json.toJson(inputData)));
        

		System.out.println("finished test for List Questions controller");

	
	} */

}
