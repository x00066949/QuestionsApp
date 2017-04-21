package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


@Entity
public class Candidate extends Model {

	@Id
	public Long id;
	
	@Constraints.Required
	public String name;
	

	@ManyToOne
	public Category role;
	
	public int rate;

	public Interview interview;
	
	public Candidate(){
		
	}
	
	public  Candidate(Long id, String name, Category role, int rate){
        this.id = id;
        this.name = name;
        this.role = role;
        this.rate = rate;
    }
	
	/* public String getRoleName(){
		return role.name;
		
	} */
	
	public static Finder<Long,Candidate> find = new Finder<Long,Candidate>(Long.class, Candidate.class);

	//Find all Candidates in the database
	public static List<Candidate> findAll() {
		return Candidate.find.all();
	}
}