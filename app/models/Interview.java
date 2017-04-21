package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
public class Interview extends Model {
	
	@Id
	public Long id;
	
	
	
	public Candidate candidate;
	

	//Unidirectional mapping
	@ManyToOne
	public List<Question> questions;
	
	@ManyToOne
	public InterviewGroup group;
	
	public List<Integer> rates;
	
	public Interview(){
		
	}
	public Interview(Candidate candidate, List<Question> questions){

		this.candidate = candidate;
		this.questions = questions;

	}
	
	public Interview(Long id, Candidate candidate, List<Question> questions, List<Integer> rates){
		this.id = id;
		this.candidate = candidate;
		this.questions = questions;
		this.rates = rates;
	}
	
	public static Finder<Long,Interview> find = new Finder<Long,Interview>(Long.class, Interview.class);



		//Find all Candidates in the database
		public static List<Interview> findAll() {
			return Interview.find.all();
		}
}