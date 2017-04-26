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
	public Long id;
	
    @Constraints.Required
    public String name;

	// Category contains many questions
	
	@OneToMany(mappedBy = "category")
	public List<Question> questions;
	
	@OneToMany(mappedBy = "role")
	public List<Candidate> candidates;

    // Default constructor
    public  Category() {
    }

    public  Category( Long id, String name, List<Question> questions) {
			this.id = id;
			this.name = name;
			this.questions = questions;
    }

		//Generic query helper for category entity with name
    public static Finder<Long,Category> find = new Finder<Long,Category>(Long.class, Category.class);

		// Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Category c: Category.find.orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
		options.put("all", "ALL QUESTIONS");
        return options;
    }
	

		//Find all categories in the database
	public static Set<Category> findAllCategories() {
		Set<Category> categories = new HashSet<Category>();
		for (Category c : Category.find.all()){
			categories.add(c);
		}

		return categories;
	}
	
	public static List<Category> findAll() {
		return Category.find.all();
	}
	
/* 	public static Category getCategory(String name) {
        if (id == null)
                return null;
        else
            return find.byId(name);
    } */
	
}

