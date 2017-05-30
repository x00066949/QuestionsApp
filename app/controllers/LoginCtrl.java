package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;


import views.html.*;

// Import required classes
import java.util.*;
import java.util.List;
import java.io.*;




// Import models
import models.*;

public class LoginCtrl extends Controller {

    public Result login() {
		System.out.println(Interviewer.getLoggedIn(session().get("username")) + " login page ");
		// Pass a login form to the login view and render
		return ok(login.render(Form.form(Login.class), Interviewer.getLoggedIn(session().get("username"))));
    }

	// Process the user login form
	public Result authenticate() {
		// Bind form instance to the values submitted from the form  
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

		// Check for errors
		// Uses the validate method defined in the Login class
		if (loginForm.hasErrors()) {
			System.out.println(session().get("username")+" wrong password");

			
			// If errors, show the form again
			return badRequest(login.render(loginForm, Interviewer.getLoggedIn(session().get("username"))));
		} 
		else {
			System.out.println(session().get("username")+" Logged in suceessfully");
			// User Logged in sucessfully
			// Clear the existing session
			session().clear();
			
			// Store the logged in username in the session
			session("username", loginForm.get().username);
			
			// Return to home page
			return redirect(routes.Application.index());
		}
	}	

	public Result logout() {
		System.out.println(session().get("username")+" Logged out");
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.LoginCtrl.login());
	}
	
	public Result addInterviewer(){
		System.out.println(session().get("username") + "Creating interviewer");

		Form<Interviewer> addInterviewerForm = Form.form(Interviewer.class);

		return ok(register.render(addInterviewerForm, Interviewer.getLoggedIn(session().get("username"))));
	}
	
	public Result addInterviewerSubmit(){
		Form<Interviewer> newInterviewerForm = Form.form(Interviewer.class).bindFromRequest();

		if (newInterviewerForm.hasErrors()){
			return badRequest(register.render(newInterviewerForm, Interviewer.getLoggedIn(session().get("username"))));
		}
		
		if (checkPassword(newInterviewerForm.get().password)){
			Interviewer anInterviewer = new Interviewer();
			anInterviewer.name = newInterviewerForm.get().name;
			anInterviewer.username = newInterviewerForm.get().username;
			anInterviewer.password = Interviewer.encryptPassword(newInterviewerForm.get().password);
			
			

			anInterviewer.save();

			System.out.println(session().get("username")+" Interviewer "+newInterviewerForm.get().name+" saved");

		
			flash("success", "Interviewer " + newInterviewerForm.get() + " has been created");
		

			return redirect(routes.Application.index());
		}else {
			flash("error", "Password must meet the following requirements: at least 8 characters, 1 uppercaser letter, 1 lowercase letter, 1 number, 1 special character " );

			System.out.println(session().get("username")+" password requirements not met ");
			return badRequest(register.render(newInterviewerForm, Interviewer.getLoggedIn(session().get("username"))));
		}
       
		
	}
	
	public boolean checkPassword(String testPassword){
		System.out.println(session().get("username")+" checking password requirements ");

		boolean hasUppercase = !testPassword.equals(testPassword.toLowerCase());//checks for an uppecase
		boolean hasLowercase = !testPassword.equals(testPassword.toUpperCase());//checks for a lowercase
		boolean isAtLeast8 = testPassword.length() >= 8;//Checks for at least 8 characters
		boolean hasSpecial = !testPassword.matches("[A-Za-z0-9 ]*");//Checks at least one char is not alpha numeric
		boolean hasNumber = testPassword.matches(".*\\d+.*"); //checks that it contains a number 
		
		if (hasUppercase == true && hasLowercase == true && isAtLeast8 == true && hasSpecial == true && hasNumber == true){
			return true;
		}else{
			return false;
		}
		
		
		
	}

}
