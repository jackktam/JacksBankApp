package com.revature.dao;

import java.util.List;

import bankobjects.User;

public interface UserDao {
	
	public void createUser(User user);
	
	public User retrieveUserByName(String username);
	
	public List<String> retrieveAllUser();
	
}
