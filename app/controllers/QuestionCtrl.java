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
    public Result listQuestions(Long cat) {
			// Get list of categories
			List<Category> categories = Category.find.where().orderBy("name asc").findList();
			// Instansiate questions, an Arraylist of questions			
			List<Question> questions = new ArrayList<Question>();
		
			if (cat == 0) {
				// Get the list of ALL questions
				questions = Question.findAll();
			}
			else {
				// Get questions for the selected category
				// Each category object contains a list of questions
				for (int i = 0; i < categories.size(); i++) {
					if (categories.get(i).id == cat) {
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
						
				}
				cat.questions = questions;
				categories.add(cat);
				
				
			}
			
			/* for (String value : mappedQuestions.values()){
				Question que = new Question();
				que.question = (List) (mappedQuestions.get(key));
				que.category = key.toUpperCase();
				
			} */

			
/* 			for (Category temp : categories) {
				if ((temp.name).equals(catIn)){
					questions = temp.questions;
					break;
				}else {
					for (String value : mappedQuestions.values()){
					questions = (List)(mappedQuestions.get(value));
					}
				}
			} */
			
//			Ebean.save(categories);
		
		}catch (Exception f){
			f.printStackTrace();
		}
		
		return ok(index.render(categories, catIn));

	}
}
