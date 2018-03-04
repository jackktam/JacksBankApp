package bankobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BankDatabase implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6040893903896195516L;
	
	private List<User> user;
	private List<Account> account;
	private HashMap<String, String> loginInfo;

	public BankDatabase() {
		user = new LinkedList<User>();
		account = new ArrayList<Account>();
		loginInfo = new HashMap<>();
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

	public HashMap<String, String> getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(HashMap<String, String> loginInfo) {
		this.loginInfo = loginInfo;
	}
	
}