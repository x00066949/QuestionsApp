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
	
	
	private Integer questionIndex = 0;
	private Interview interview = new Interview();
	private Integer interviewRate = 0;


	
	//method to get 5 random questions from a interview's category
	public Result getRandomQuestions(Long interviewId){
		List<QuestionRate> randomQuestions = new ArrayList<QuestionRate>();
		//find interview with same id as passed in the parameter
		interview = Interview.find.byId(interviewId);
		 
		//check interview and category exists. 
		if (interview != null /* && category != null */){

			//add all questions in interview
			List<Question> qList = interview.role.questions;
			
			//jumble all questions
			Collections.shuffle(qList);

			if (interview.numQuestions > 0 && interview.numQuestions <= qList.size()){
				for(int i = 0; i < interview.numQuestions; i++){
					QuestionRate cq = new QuestionRate();
					cq.question = qList.get(i);
					randomQuestions.add(cq);
				}
				
				//add list of questions to the interview object
				interview.questions = randomQuestions;
				
				
				
				Form<QuestionRate> editQuestionRateForm = Form.form(QuestionRate.class);

				//render page with pre filled form
				return ok(candidateQuestions.render(editQuestionRateForm, interview.questions.get(questionIndex),interview, Category.findAllCategories()));
			}else {
				Ebean.delete(interview);
				return badRequest(index.render(Category.findAllCategories(), "Error: You have requested "+interview.numQuestions
									+" questions, please try again, making sure to pick a number between 1 and  "+qList.size()+" for the role of  "+interview.role.name));

			}


		}
		else{
			flash("Error interview does not exist");
            return badRequest(index.render(Category.findAllCategories(), "Error: interview does not exist"));
		}
	}
	

	public Result interviewSubmit() {
		
			
		Form<QuestionRate> interviewForm = Form.form(QuestionRate.class).bindFromRequest();
		QuestionRate c= interviewForm.get();
		
		
		//int current = questionIndex;
		
		c.save();

		interviewRate = interviewRate+c.rate;
		questionIndex++;
		if(questionIndex < interview.numQuestions){
			
			
			return ok(candidateQuestions.render(interviewForm, interview.questions.get(questionIndex), interview, Category.findAllCategories()));
		}else{
			
			interview.interviewRate = interviewRate;
			interview.interviewDate = new Date();
			interview.save();
			
			//reset interview rate back to zero so a fresh rate is created for a new interview
			interviewRate = 0;
			questionIndex = 0;
			return ok(index.render(Category.findAllCategories(), "Interview Completed, "+interview.candidate.name+"'s score is "+interview.interviewRate.toString()));
		}
		
	}
}