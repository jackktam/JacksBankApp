package com.revature.dao;

import java.util.List;

import bankobjects.Account;
import bankobjects.Application;

public interface AccountDao {
	
	public void createAccount(Application app);
	
	public Account retrieveAccountById(int id);
	
	public List<Integer> retrieveAllAccount();
	
	public void updateAccount(int id, double balance);
	
	public void deleteAccount(int id);
	
	public void createAccountPreparedStmt(Account account);
	
}
