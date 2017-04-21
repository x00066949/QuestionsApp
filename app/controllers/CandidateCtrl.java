package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;

import views.html.*;

// Import required classes

import java.util.*;

import models.*;
//import controllers.security.*;

//@Security.Authenticated(Secured.class)
public class CandidateCtrl extends Controller{


	public Result addCandidate(){
		Form<Candidate> addCandidateForm = Form.form(Candidate.class);

		return ok(newCandidate.render(addCandidateForm, Category.findAllCategories() /* , User.getLoggedIn(session().get("email")) */));
	}
	
	
	public Result addCandidateSubmit(){
		Form<Candidate> newCandidateForm = Form.form(Candidate.class).bindFromRequest();
			if (newCandidateForm.hasErrors()){
				return badRequest(newCandidate.render(newCandidateForm, Category.findAllCategories()/* , User.getLoggedIn(session().get("email")) */));
			}

		newCandidateForm.get().save();
		Candidate can = newCandidateForm.get();
		flash("success", "Candidate " + newCandidateForm.get() + " has been created");
		Candidate candidate = Candidate.find
						.fetch("role","name")
						.where()
						.like("name",can.name)
						.eq("id",can.id)
						.findUnique();
						
		//String role = role1.getRoleName();
		return redirect(routes.InterviewCtrl.getRandomQuestions(candidate.id));
	}

	//@With(CheckManager.class)
	public Result deleteCandidate(Long id) {
		Candidate.find.ref(id).delete();	
		flash("success", "Candidate has been deleted");
		return redirect("/listCandidates");
		}
		
	public Result editCandidate(Long id){
		Form<Candidate> editCandidateForm = Form.form(Candidate.class).fill(Candidate.find.byId(id));
		return ok (editCandidate.render(id, editCandidateForm, Category.findAllCategories()/* , User.getLoggedIn(session().get("email")) */));
	}	

	public Result editCandidateSubmit(Long id) {
		Form<Candidate> editCandidateForm = Form.form(Candidate.class).bindFromRequest();
		
		if(editCandidateForm.hasErrors()) {
			return badRequest(editCandidate.render(id, editCandidateForm, Category.findAllCategories()/* , User.getLoggedIn(session().get("email")) */));
		}
		Candidate c= editCandidateForm.get();
		c.id = id;
		c.update();
		flash("success", "Candidate " + editCandidateForm.get() + " has been updated");
	return redirect("/listCandidates");
	}
	
	

	

	public Result listCandidate() {
		
		List<Candidate> candidates = Candidate.findAll();
		return ok(listCandidates.render(candidates, Category.findAllCategories()/* , User.getLoggedIn(session().get("email")) */));
		
    }


}
