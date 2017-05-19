package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
public class QuestionRate extends Model {
	
	@Id
	public Long id;
	
	@ManyToOne
	public Interview interview;

	//Unidirectional mapping
//	@Constraints.Required
	@ManyToOne
	public Question question;
	
	public Integer rate;
	
	public String comments;
	
	public QuestionRate(){
		
	}

	
	public QuestionRate(Long id, Question question, Integer rate, String comments){
		this.id = id;
		this.question = question;
		this.rate = rate;
		this.comments = comments;
	}
	
	public static Finder<Long,QuestionRate> find = new Finder<Long,QuestionRate>(Long.class, QuestionRate.class);



	//Find all Candidates in the database
	public static List<QuestionRate> findAll() {
		return QuestionRate.find.all();
	}
}