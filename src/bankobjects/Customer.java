package bankobjects;

import java.io.Serializable;
import java.util.Scanner;

public class Customer extends User implements EditAccount, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2412725884484869873L;

	public Customer(String username, String password) {
		super(username, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean withdraw(String targetAccount, double value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deposit(String targetAccount, double value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transfer(String currentAccount, String targetAccount, double value) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean apply(Scanner scanner, BankDatabase base) {
		
		return false;
		
	}

}
