package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

@Entity
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "userType")
public abstract class User extends Model {

	@Id
	public Long id;
	
	public String name;

	
	public User(){
		
	}
	public  User(Long id, String name){
        this.id = id;
        this.name = name;

    }
	
/* 
	public  User(Long id, String name, String userType){
        this.id = id;
        this.name = name;
        this.userType = userType;

    } */

	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class);

	//Find all Candidates in the database
	public static List<User> findAll() {
		return User.find.all();
	}
	
	
}