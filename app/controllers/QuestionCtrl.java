
package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.*;
import play.mvc.Http.Context;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;

import com.avaje.ebean.Ebean;

// Import required classes
import java.util.*;

import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;

import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import javax.activation.MimetypesFileTypeMap;



// Import models
import models.*;

@Security.Authenticated(Secured.class)
public class QuestionCtrl extends Controller{

	public Result uploadForm(){
		System.out.println(session().get("username")+" Visit upload page");

        return ok(upload.render(Interviewer.getLoggedIn(session().get("username"))));

	}
	
	public Result upload(){
		
		//check to see if anything questions exist in DB before adding new questions...
		//without this check, all new questions uploaded will be added to DB along with old Questions, so evey question ever uploaded will be generated when all questions link is clicked
		System.out.println(session().get("username")+" Uploaded a file ");
		

		if (Category.findAll() != null){
			Ebean.delete(QuestionRate.findAll());
			Ebean.delete(Question.findAll());
			Ebean.delete(Interview.findAll());
			Ebean.delete(Candidate.findAll());
			
			Ebean.delete(Category.findAll());
			
		}

		
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart questionsFile = body.getFile("questionsFile");
		
		if (questionsFile != null) {
			String fileName = questionsFile.getFilename();
			String contentType = questionsFile.getContentType();
			File file = questionsFile.getFile();
			
			System.out.println(session().get("username")+ " File name"+fileName);
			System.out.println(session().get("username")+" File format"+contentType);

			if ((contentType.equalsIgnoreCase("text/plain")) || (contentType.equalsIgnoreCase("text/csv")) || (contentType.equalsIgnoreCase("application/vnd.ms-excel")) || (contentType.equalsIgnoreCase("vnd.ms-excel"))){
				
				
				
				
				try{
					Multimap<String, String> mappedQuestions = ArrayListMultimap.create();
					BufferedReader br = new BufferedReader(new FileReader(file));
					
					String line = " ";

					int lineNum=0;
					
					while ((line = br.readLine()) != null){
						lineNum++;
						
						String [] parts = line.split(":",2);

						String key = parts[0];

						String value = parts[1];

						if (key.trim().length() > 0){

							mappedQuestions.put(key,value);
						}else{
							mappedQuestions.put("NO CATEGORY DEFINED",value);
						}
					}
					for (String key : mappedQuestions.keys()){
						List<String> questionList = new ArrayList<String>();
						List<Question> questions = new ArrayList<Question>();
						
						
						questionList = (List) (mappedQuestions.get(key));
						Category cat = new Category();
						cat.name = key.toUpperCase();
						
						for (String question : questionList){

							Question que = new Question();
							que.question = question;
							
							que.category = cat;
							
							
							
							int questionRows = Question.find
								.where()
								.like("category.name",cat.name)
								.like("question",question)
								.findRowCount();	

							if (questionRows == 0){
								questions.add(que);
							}
						
							
						}
						cat.questions = questions;							
						int categoryRows = Category.find.where()
							.like("name",cat.name)
							.findRowCount();
							
						if (categoryRows == 0){
							Ebean.save(cat);
						}
						
						
						Ebean.save(questions);
					
					}
					

			
					}catch (Exception f){
						f.printStackTrace();
					}
				
					return ok(index.render("File uploaded", Interviewer.getLoggedIn(session().get("username"))));
				} else {
					System.out.println(session().get("username")+ " wrong File format");

					return badRequest(upload.render(Interviewer.getLoggedIn(session().get("username"))));
				}
		}else{
			System.out.println(session().get("username")+" Missing file");

			//flash("error", "Missing file");
			return badRequest(upload.render(Interviewer.getLoggedIn(session().get("username"))));
			
			
		}
		

	}


	// Show a list of all questions
    public Result listQuestions(String cat) {
			System.out.println(session().get("username")+" List Questions");

			// Get list of categories
			List<Category> categories = Category.find.where().orderBy("name asc").findList();
			// Instansiate questions, an Arraylist of questions			
			List<Question> questions = new ArrayList<Question>();
		
			if (cat.equalsIgnoreCase("all")) {
				// Get the list of ALL questions
				questions = Question.findAll();
			}
			else {
				// Get questions for the selected category
				// Each category object contains a list of questions
				for (int i = 0; i < categories.size(); i++) {
					if (categories.get(i).name == cat) {
						questions = categories.get(i).questions;
						break;
					}
				}
			}
			

			return redirect("/all");
		  //return (listQuestions.render(categories, questions));
    }
	
	public Result getCategories(String catIn){
		
		
		return ok(listQuestions.render(Category.findAllCategories(), catIn, Interviewer.getLoggedIn(session().get("username"))));

	}
	
	
}