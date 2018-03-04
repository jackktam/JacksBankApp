package bankobjects;

public interface EditAccount {

	boolean withdraw(String targetAccount, double value);
	
	boolean deposit(String targetAccount, double value);
	
	boolean transfer(String currentAccount, String targetAccount, double value);
	
}
