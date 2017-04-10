package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

// Product entity managed by Ebean
@Entity
public class Category extends Model {

    // Fields - note that these are defined as public and not private
    // During compile time, The Play Framework
    // automatically generates getters and setters


    @Id
    public String name;

	// Category contains many questions
	@OneToMany(mappedBy = "category")
	public List<Question> questions;

    // Default constructor
    public  Category() {
    }

    public  Category( String name, List<Question> questions) {
	
			this.name = name;
			this.questions = questions;
    }

		//Generic query helper for category entity with name
    public static Finder<String,Category> find = new Finder<String,Category>(String.class, Category.class);

/* 		// Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Category c: Category.find.orderBy("name").findList()) {
            options.put(c.name);
        }
        return options;
    } */

		//Find all categories in the database
		public static List<Category> findAll() {
			return Category.find.all();
		}
	
}

