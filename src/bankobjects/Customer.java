package bankobjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Customer extends User implements EditAccount, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2412725884484869873L;
	private List<Integer> ownedAccounts;
	
	public List<Integer> getOwnedAccounts() {
		return ownedAccounts;
	}

	public void setOwnedAccounts(List<Integer> ownedAccounts) {
		this.ownedAccounts = ownedAccounts;
	}

	public Customer(String username, String password) {
		super(username, password);
		ownedAccounts = new LinkedList<>();
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
	
	public void printInfo() {
		System.out.println("Customer username: " + this.username + "\nCustomer password: " + this.password);
		System.out.println(this.username + "'s Accounts\n===============");
		for(Integer i: ownedAccounts) {
			System.out.println(i);
		}
		System.out.println("===============");
	}
	
	public void printAccounts() {
		
		System.out.println(this.username + "'s Accounts\n===============");
		for(Integer i: ownedAccounts) {
			System.out.println(i);
		}
		System.out.println("===============");
		
	}

}
