package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;

import com.avaje.ebean.Ebean;

import views.html.*;

// Import required classes

import java.util.*;

import models.*;
//import controllers.security.*;

@Security.Authenticated(Secured.class)
public class CandidateCtrl extends Controller{


	public Result addCandidate(){
		System.out.println(session().get("username")+ " Creating candidate");

		Form<Interview> addInterviewCandidateForm = Form.form(Interview.class);

		return ok(newCandidate.render(addInterviewCandidateForm, Interviewer.getLoggedIn(session().get("username"))));
	}
	
	
	public Result addCandidateSubmit(){
		Form<Interview> newInterviewCandidateForm = Form.form(Interview.class).bindFromRequest();

		if (newInterviewCandidateForm.hasErrors()){
			return badRequest(newCandidate.render(newInterviewCandidateForm, Interviewer.getLoggedIn(session().get("username"))));
		}
		
		Candidate candidate = new Candidate();
		candidate = newInterviewCandidateForm.get().candidate;
		candidate.interview = newInterviewCandidateForm.get();
		candidate.save();

		System.out.println(session().get("username")+candidate.id+" saved");

		
		
		newInterviewCandidateForm.get().save();
		
		flash("success", "Candidate " + newInterviewCandidateForm.get() + " has been created");
	

		return redirect(routes.InterviewCtrl.getRandomQuestions(newInterviewCandidateForm.get().id));
	}
	
	public Result deleteCandidate(Long id) {
		
		//Delete interview associated with candidate
		Interview intDelete = Interview.find
		.where()
		.eq("candidate_id",id)
		.findUnique();
	

		//find all questions associated with the interview
		List <QuestionRate> qr = QuestionRate.find
		.where()
		.eq("interview_id",intDelete.id)
		.findList();
		
		Ebean.delete(qr);
		
		//delete the interview instance for the candidate before deleteing candidate
		intDelete.delete();
		
		//then delete candidate
		User.find.ref(id).delete();

		flash("success", "Candidate has been deleted");
		return redirect("/");
	}

	


}
