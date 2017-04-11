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
public class Candidate extends Model {

	@Id
	public Long id;
	
	@Constraints.Required
	public String name;
	
	public Category department;
	
	public int rate;
	
	
	public  Candidate(Long id, String name, Category department, int rate){
        this.id = id;
        this.name = name;
        this.department = department;
        this.rate = rate;
    }
	
	public static Finder<Long,Candidate> find = new Finder<Long,Candidate>(Long.class, Candidate.class);



		//Find all Candidates in the database
		public static List<Candidate> findAll() {
			return Candidate.find.all();
		}
}