package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
public class InterviewGroup extends Model {
	
	@Id
	public Long id;


	@OneToMany(mappedBy = "group")
	public List<Interview> interviews;
	
	
	public InterviewGroup(){
		
	}
	
	public InterviewGroup(Long id, List<Interview> interviews){
		this.id = id;
		this.interviews = interviews;
	}
	
	public static Finder<Long,InterviewGroup> find = new Finder<Long,InterviewGroup>(Long.class, InterviewGroup.class);

	//Find all Candidates in the database
	public static List<InterviewGroup> findAll() {
		return InterviewGroup.find.all();
	}
}