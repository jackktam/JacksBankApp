package com.revature.dao;

import bankobjects.Application;

public interface ApplicationDao {
	
	public void createApplication(Application application);
	
	public Application retrieveNextApp();
	
	public void denyNextApplication();
}
