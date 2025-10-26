package com.gamingroom.gameauth.auth;

import io.dropwizard.auth.Authorizer;

public class GameAuthorizer implements Authorizer<GameUser> 
{
    @Override
    public boolean authorize(GameUser user, String role) {
    	
        // FIXME: Finish the authorize method based on BasicAuth Security Example
    	//check if the user actually has the requested role
        if (user != null && user.getRoles().contains(role)) {
        	return true; // access granted
        }
        
    	return false; // deny access
    }
}