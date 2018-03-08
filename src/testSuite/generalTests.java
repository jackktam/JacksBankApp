package testSuite;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bankobjects.Account;

public class generalTests {	
	
	/*******************************************************************
	 * Account Methods
	 ******************************************************************/
	
	@Test
	public void testLargeNumberWithdrawal() {
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.setBalance(100);
		assertEquals(a.withdraw(1000000000),false);
	}
	
	@Test
	public void testNegativeNumberWithdrawal() {
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.setBalance(100);
		assertEquals(a.withdraw(-100),false);
	}
	
	@Test
	public void testTotalBalanceNumberWithdrawal() {
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.setBalance(100);
		assertEquals(a.withdraw(100),true);
	}

}
