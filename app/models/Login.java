package models;

public class Login {

  public String username;
  public String password;

	// Validate method - invoked during error checking
	// after form based on a Login object has been submitted
	public String validate() {
		
		// Call the static authenticate method in User
		if (Interviewer.authenticate(username, password) == null) {
		  return "Invalid user or password";
		}
		return null;
	}
}