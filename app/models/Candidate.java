package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("candidate")
public class Candidate extends User {


	public Interview interview;
	
	public Candidate(){
		
	}
	
	public  Candidate(Long id, String name, Interview interview){

		super(id,name);
        this.interview = interview;

    }

	
	/* public static Finder<Long,Candidate> find = new Finder<Long,Candidate>(Long.class, Candidate.class);

	//Find all Candidates in the database
	public static List<Candidate> findAll() {
		return Candidate.find.all();
	}
	 */
	
}