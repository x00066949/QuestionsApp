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
	
	
	private List<QuestionRate> randomQuestions = new ArrayList<QuestionRate>();
	private Integer questionIndex = 1;
	private Interview interview = new Interview();

	private Integer interviewRate = 0;
	
	//method to get 5 random questions from a candidate's category
	public Result getRandomQuestions(Long canId){
		
		//find candidate with same id as passed in the parameter
		Candidate candidate = Candidate.find.byId(canId);
		 
		//check candidate and category exists. 
		if (candidate != null /* && category != null */){

				
			//add all questions in candidate
			List<Question> qList = candidate.role.questions;
			
			//jumble all questions
			Collections.shuffle(qList);

			
			for(int i = 0; i <= 4; i++){
				QuestionRate cq = new QuestionRate();
				cq.question = qList.get(i);
				randomQuestions.add(cq);
			}
			//create interview instance with detaails gathered so far
			//add list of questions to the interview object
			interview.candidate = candidate;
			interview.questions = randomQuestions;
			
			
			
			Form<QuestionRate> editQuestionRateForm = Form.form(QuestionRate.class);

			//render page with pre filled form
			return ok(candidateQuestions.render(editQuestionRateForm, interview.questions.get(0),interview.candidate, Category.findAllCategories()));

		}
		else{
			flash("Error either category or candidate does not exist");
            return badRequest(index.render(Category.findAllCategories(), "Error: candidate does not exist"));
		}
	}
	


	public Result interviewSubmit() {
		Form<QuestionRate> interviewForm = Form.form(QuestionRate.class).bindFromRequest();
		QuestionRate c= interviewForm.get();
		
		
		int current = questionIndex;
		
		c.save();
		
		if(questionIndex < randomQuestions.size()){
			interviewRate = interviewRate+c.rate;
			questionIndex++;
			
			return ok(candidateQuestions.render(interviewForm, interview.questions.get(current), interview.candidate, Category.findAllCategories()));
		}else{
			
			interview.interviewRate = interviewRate;
			interview.save();
			//flash("success", "Interview " + interviewForm.get() + " has been completed");
			return ok(index.render(Category.findAllCategories(), "Interview Completed, "+interview.candidate.name+"'s score is "+interview.interviewRate.toString()));
		}
		
	}
}