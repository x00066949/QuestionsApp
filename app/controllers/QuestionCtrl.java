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

public class QuestionCtrl extends Controller{


		// Show a list of all questions
    public Result listQuestions(String cat) {
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
		
		
		Set<Category> categories = new HashSet<Category>();

		
		try{
			Multimap<String, String> mappedQuestions = ArrayListMultimap.create();
			BufferedReader br = new BufferedReader(new FileReader("listQuestions.txt"));
			
			String line = " ";

						
			while ((line = br.readLine()) != null){
				
				String [] parts = line.split(":");
				String key = parts[0];
				String value = parts[1];
				
				mappedQuestions.put(key,value);
				
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
						
						questions.add(que);
						
						
						
					int categoryRows = Category.find.where()
						.like("name",cat.name)
						.findRowCount();
						
					if (categoryRows == 0){
						Ebean.save(cat);
					}
					
					
					int questionRows = Question.find
						.where()
						.like("category.name",cat.name)
						.like("question",question)
						.findRowCount();
						
						
						
					if (questionRows == 0){
						Ebean.save(que);
					}
					
				
					
				}
				
				cat.questions = questions;
				
				
				
				
				categories.add(cat);
				
			}
			

		
		}catch (Exception f){
			f.printStackTrace();
		}
		
		return ok(listQuestions.render(categories, catIn));

	}
	
	//method to get 5 random questions from a specified category
	public Result getRandomQuestions(Long id){
		Category category = Category.find.byId(id);
		if (category != null){
		List<Question> qList = category.questions;
		
		Collections.shuffle(qList);
		
		List<Question> randomQuestions = new ArrayList<Question>();
		
		for (int i = 0; i <= 4; i++){
			randomQuestions.add(qList.get(i));
		}
		
		return ok(candidateQuestions.render(randomQuestions, Category.findAllCategories()));
		}
		else{
			flash("Error category does not exist");
            return badRequest(index.render(Category.findAllCategories()));
		}
	}
}
