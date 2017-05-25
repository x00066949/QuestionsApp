package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

// Import required classes
import java.util.ArrayList;
import java.util.List;

// Import models
import models.*;

public class LoginCtrl extends Controller {

    public Result login() {
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
			// If errors, show the form again
			return badRequest(login.render(loginForm, Interviewer.getLoggedIn(session().get("username"))));
		} 
		else {
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
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.LoginCtrl.login());
	}

}
