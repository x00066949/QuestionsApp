package models;

import java.util.*;
import java.time.LocalDate;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
public class Interview extends Model {
	
	@Id
	public Long id;
	
	@OneToOne
//	@Constraints.Required
	public Candidate candidate;
	
	@ManyToOne
	public Category role;

	@OneToMany(mappedBy = "interview")
	public List<QuestionRate> questions;

	public int numQuestions;
	
	public LocalDate interviewDate;
	
	public Integer interviewRate;
	
	public Interview(){
		this.interviewDate = LocalDate.now();
	}
	
	public Interview(Candidate candidate, List<QuestionRate> questions){

		this.candidate = candidate;
		this.questions = questions;

	}
	
	public Interview(Long id, Candidate candidate, List<QuestionRate> questions, Integer interviewRate, LocalDate interviewDate, Category role){
		this.id = id;
		this.candidate = candidate;
		this.questions = questions;
		this.interviewRate = interviewRate;
		this.interviewDate = interviewDate;
		this.role = role;
	}
	
	public String getInterviewDateString(){
		return interviewDate.toString();
	}
	
	public static Finder<Long,Interview> find = new Finder<Long,Interview>(Long.class, Interview.class);



		//Find all Candidates in the database
		public static List<Interview> findAll() {
			return Interview.find.all();
		}
}