package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("interviewer")
public class Interviewer extends User {

	public String username;
	
	public String password;
		
	public Interviewer(){
		
	}
	
	public  Interviewer(Long id, String name, String username, String password){
        super(id,name);
        this.username = username;
		this.password = password;

    }


	
	public	static User authenticate(String username, String password) {
			// If found return the user object with matching username and password
			return User.find.where().eq("username", username).eq("password", password).findUnique();
		}

		// Check if a user is logged in (by username)
		public static User getLoggedIn(String username) {
			if (username == null)
					return null;
			else
				// Find user by username and return object
				return find.where().eq("username",username).findUnique();
		}
}