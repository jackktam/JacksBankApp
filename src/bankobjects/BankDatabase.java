package bankobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BankDatabase implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6040893903896195516L;
	
	private List<User> user ;
	private List<Account> account ;

	public BankDatabase() {
		user = new LinkedList<User>();
		account = new ArrayList<Account>();
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public List<Account> getAccount() {
		return account;
	}

	public void setAccount(List<Account> account) {
		this.account = account;
	}
	
}