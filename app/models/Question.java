package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
@Entity
 /**
 * Question class to hold a question object
 */
public class Question extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters


    @Id
    public Long id;

	//What category the question applies to
	@Constraints.Required
    @ManyToOne
    public Category category;

	//variable containing the question
    @Constraints.Required
    public String question;

	//for questions statistics
	
    public int points=0;
	
	//how many times the question has been asked
	public int repeats=0; 
	
	//difficulty level
	public boolean isDifficult = false;
	
	
	

    // Default constructor
    public  Question() {
    }

    // Constructor to initialise object
    public  Question(Long id, Category category, String question){
        this.id = id;
        this.category = category;
        this.question = question;
        
    }
	
	//Generic query helper for Question entity id Long
    public static Finder<Long,Question> find = new Finder<Long,Question>(Long.class, Question.class);

	//Find all Questions in the database
	public static List<Question> findAll() {
		return Question.find.all();
	}
	

}
