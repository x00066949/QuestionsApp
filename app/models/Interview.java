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
	
	@ManyToOne
//	@Constraints.Required
	public Candidate candidate;
	

	//Unidirectional mapping
	@ManyToOne
	public List<QuestionRate> questions;
	
	@ManyToOne
	public InterviewGroup group;
	
	public Integer interviewRate;
	
	public Interview(){
		
	}
	public Interview(Candidate candidate, List<QuestionRate> questions){

		this.candidate = candidate;
		this.questions = questions;

	}
	
	public Interview(Long id, Candidate candidate, List<QuestionRate> questions, Integer interviewRate){
		this.id = id;
		this.candidate = candidate;
		this.questions = questions;
		this.interviewRate = interviewRate;
	}
	
	public static Finder<Long,Interview> find = new Finder<Long,Interview>(Long.class, Interview.class);



		//Find all Candidates in the database
		public static List<Interview> findAll() {
			return Interview.find.all();
		}
}