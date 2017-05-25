package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {

	// Get the username of the logged in user
	// null returned if no user logged in - onUnauthorized
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

	// If not logged in then open the login page
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginCtrl.login());
    }
}
