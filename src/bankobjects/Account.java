package bankobjects;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Account implements Serializable, Comparator<Account> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7109839765253977195L;
	
	private double balance;
	private int id;
	private List<String> owners ;
	
	public Account(int id, List<String> owners) {
		super();
		this.balance = 0;
		this.id = id;
		this.owners = owners;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<String> getOwners() {
		return owners;
	}
	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	@Override
	public int compare(Account arg0, Account arg1) {
		
		return arg0.id - arg1.id;
	}
	
	public boolean allowedAccess(String user) {

		for(String s: owners) {
			if(s.equals(user))
				return true;
		}
		
		return false;
		
	}
	
	public boolean negativeBalance(double amount) {
		
		if(this.getBalance()-amount < 0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public void withdraw(double amount) {
		
		this.balance = this.balance-amount;
		
	}
	
	public void deposit(double amount) {
		
		this.balance = this.balance+amount;
		
	}
	
}
