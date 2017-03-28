package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.Form.*;
import play.mvc.Http.Context;

import views.html.*;

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
			

			return redirect("/");
		  //return (listQuestions.render(categories, questions));
    }
	
	public Result getCategories(){
		
		Set<Category> categories = new HashSet<Category>();
		List<Question> questions = new ArrayList<Question>();

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

				Category cat = new Category();
				if (mappedQuestions.get(key) instanceof List){
				 cat.questions	= (List)(mappedQuestions.get(key));
				}else{
					cat.questions = new ArrayList(mappedQuestions.get(key));
				}
				String upperCat = key.toUpperCase();
				cat.name = upperCat;
				categories.add(cat);
				
			}
		
		}catch (Exception f){
			f.printStackTrace();
		}
		
		return ok(index.render(categories));

	}
}
