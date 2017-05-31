package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

// Import required classes
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.*;


// Import models
import models.*;
//import controllers.security.*;

@Security.Authenticated(Secured.class)
public class InterviewCtrl extends Controller{
	
	
	private Integer questionIndex = 0;
	private Interview interview = new Interview();
	private Integer interviewRate = 0;


	
	//method to get 5 random questions from a interview's category
	public Result getRandomQuestions(Long interviewId){
		System.out.println(session().get("username")+" Getting Random Questions");
		
		List<QuestionRate> randomQuestions = new ArrayList<QuestionRate>();
		//find interview with same id as passed in the parameter
		interview = Interview.find.byId(interviewId);
		
		//make sure we're starting with zero values
		questionIndex = 0;
		interviewRate = 0;
		
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
				
				System.out.println(session().get("username")+" Displaying first Question");

				return ok(candidateQuestions.render(editQuestionRateForm, interview.questions.get(questionIndex),interview, questionIndex+1, Interviewer.getLoggedIn(session().get("username"))));
			}else {
				Ebean.delete(interview);
				return badRequest(index.render("Error: You have requested "+interview.numQuestions
									+" questions, please try again, making sure to pick a number between 1 and  "+qList.size()+" for the role of  "+interview.role.name, Interviewer.getLoggedIn(session().get("username"))));

			}


		}
		else{
			flash("Error interview does not exist");
            return badRequest(index.render("Error: interview does not exist", Interviewer.getLoggedIn(session().get("username"))));
		}
	}
	

	public Result interviewSubmit() {


		Form<QuestionRate> interviewForm = Form.form(QuestionRate.class).bindFromRequest();
		
		
		//int current = questionIndex;
		if(interviewForm.get().rate != null){


			//save interview question
			interviewForm.get().save();
			
			QuestionRate c = Ebean.find(QuestionRate.class,interviewForm.get().id);
			
	
			
			setDifficulty(c);
			
			c.question.save();
			System.out.println(session().get("username")+" saved question "+questionIndex);

			
			interviewRate = interviewRate+c.rate;
			
			
			
			
			questionIndex++;
		}
		if(questionIndex < interview.numQuestions){
			
			System.out.println(session().get("username")+ " Displaying Question "+ questionIndex+1);

			return ok(candidateQuestions.render(interviewForm, interview.questions.get(questionIndex), interview, questionIndex+1, Interviewer.getLoggedIn(session().get("username"))));
		}else{
			
			interview.interviewRate = interviewRate;
			//interview.interviewDate = new Date();
			interview.save();
			
			System.out.println(session().get("username")+" Interview saved");

			return ok(index.render("Interview Completed, "+interview.candidate.name+"'s score is "+interview.interviewRate.toString(), Interviewer.getLoggedIn(session().get("username"))));
		} 
		
	}
	
	//Check if Question difficulty needs to be adjusted
	public void setDifficulty(QuestionRate c){
	
		//Add statistics infor to question
		//Category c = category;
		//Question q = c.question;
		int maxPoints = (c.question.category.countQuestions()*5); //multiply by 5 because that is the max points you can get per question
		
		//the 1owest 1/3 points are the most difficult in category
		int levelMarker = (maxPoints/3);
	
		//increase the repeats
		c.question.repeats++;
		
		//increase the points for the question
		//this is relative to how many times the question has been asked
		//c.question.points = (c.question.points + c.rate)/c.question.repeats;
		
		if (c.rate == 1 || c.rate == 2){
			c.question.points = levelMarker - (c.rate/c.question.repeats);
		}
		else if (c.rate >=3 && c.rate <=5){
			c.question.points = levelMarker + (c.rate/c.question.repeats);
		}else{
			return;
		}
		

		//set difficulty
		if (c.question.points > maxPoints){
			c.question.points = maxPoints;
			c.question.isDifficult = false;

		}		
		if (c.question.points > 0 && c.question.points <= levelMarker ){
			//difficult level questions
			c.question.isDifficult = true;
		}

		if (c.question.points > levelMarker){
			c.question.isDifficult = false;

		}
		
		
		
	}
	
	public Result groupInterviews(String date, Long cat){
		
		System.out.println(session().get("username")+" List interviews");

		
		Ebean.delete(Interview.find.where().eq("candidate_id",null).findList());
		//Ebean.delete(Interview.find.where().eq("interviewrate",null).findList());

		List<Interview> interviewList = Interview.findAll();
		Set<String> dates = new TreeSet<>();
		String role="all";
		
		if (cat != 0){
			Category matchingCategory = Category.find.where().eq("id",cat).findUnique();
			role = matchingCategory.name;

		}
		for (Interview i : interviewList){
			dates.add(i.interviewDate.toString());
		}

		if (!date.equals("0")){
			
			//convert from String to date
			try{
				LocalDate dt = LocalDate.parse(date);
				if(cat== 0){
					interviewList = Interview.find
							.where()
							.eq("INTERVIEW_DATE",dt)
							.orderBy("INTERVIEW_RATE desc")
							.findList(); 
					
				}else{
					interviewList = Interview.find
							.where()
							.eq("INTERVIEW_DATE",dt)
							.eq("ROLE_id",cat)
							.orderBy("INTERVIEW_RATE desc")
							.findList();
					

				}
			}catch(DateTimeParseException dp){
				dp.printStackTrace();
				date = "all dates";
			}
			
			
			
		}
		if (date.equals("0") && cat != 0){
			interviewList = Interview.find
							.where()
							.eq("ROLE_id",cat)
							.orderBy("INTERVIEW_RATE desc")
							.findList();
							
			date = "all dates";
		}
		
		
		
		return ok(interviews.render(Category.findAllCategories(), interviewList, dates,date,role, Interviewer.getLoggedIn(session().get("username"))));
	}

	
}