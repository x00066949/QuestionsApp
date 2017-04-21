package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

import com.avaje.ebean.Ebean;

// Import required classes
import java.util.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.*;


// Import models
import models.*;
//import controllers.security.*;

//@Security.Authenticated(Secured.class)
public class InterviewCtrl extends Controller{
	//method to get 5 random questions from a specified category
	public Result getRandomQuestions(Long canId){
		
		//find category and candidate with same id as passed in the parameter
		//Category category = Category.find.byId(catId);
		Candidate candidate = Candidate.find.byId(canId);
		 
		//check candidate and category exists. 
		if (candidate != null /* && category != null */){
			
			//check candidate's role will match the questions generated
			//if(candidate.role.id == catId){
				
				//add all questions in candidate
					List<Question> qList = candidate.role.questions;
				
				
			
				Collections.shuffle(qList);
				
				List<Question> random5Questions = new ArrayList<Question>();
				//List<Interview> randomQuestions = new ArrayList<Interview>();

				for (int i = 0; i <= 4; i++){

					random5Questions.add(qList.get(i));
				}
				
				//create interview instance with detaails gathered so far
				Interview cq = new Interview();
				cq.candidate = candidate;
				cq.questions = random5Questions;
				
//				Ebean.save(randomQuestions);
				
				Form<Interview> editQuestionRateForm = Form.form(Interview.class).fill(cq);
				
				//render page with pre filled form
				return ok(candidateQuestions.render(editQuestionRateForm.fill(new Interview(candidate, random5Questions)), cq, Category.findAllCategories()));
			//}else{
			//	flash("Error Questions requested does not match candidate's role.");
			//	return badRequest(index.render(Category.findAllCategories(), "Error Questions requested belong in "+category.name+" category. While Candidate is interviewing for "+candidate.role.name+" role. "));
			//}
			
		}
		else{
			flash("Error either category or candidate does not exist");
            return badRequest(index.render(Category.findAllCategories(), "Error: candidate does not exist"));
		}
	}
	
	/* public Result editQuestionRate(Long id){
		Form<CandidateQuestion> editQuestionRateForm = Form.form(CandidateQuestion.class).fill(CandidateQuestion.find.byId(id));
		return ok (candidateQuestions.render(id, editQuestionRateForm, Category.findAllCategories()));
	}	 */

	public Result interviewSubmit() {
		Form<Interview> interviewForm = Form.form(Interview.class).bindFromRequest();
		
		if(interviewForm.hasErrors()) {
			return badRequest(candidateQuestions.render(interviewForm, interviewForm.get(), Category.findAllCategories()));
		}
		
		Interview c= interviewForm.get();

		c.save();
		flash("success", "Interview " + interviewForm.get() + " has been completed");
		return redirect("/");
	}
}