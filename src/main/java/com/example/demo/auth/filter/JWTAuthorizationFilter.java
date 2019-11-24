package com.example.demo.auth.filter;

public class JWTAuthorizationFilter {

	
	
	protected boolean requiresAuthentication(String header) {
		
		if(header == null || !header.startsWith("bearer ")) {
			return false;
		}
		return true;
	}
}
