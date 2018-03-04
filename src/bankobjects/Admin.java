package bankobjects;

import java.io.Serializable;

public class Admin extends Staff implements EditAccount, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8153816157435037627L;

	public Admin(String username, String password) {
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

}
