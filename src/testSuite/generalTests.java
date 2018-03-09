package testSuite;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import bankobjects.Account;
import bankobjects.Application;

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
	
	@Test
	public void testAllowedAccessBeginning(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		ll.add("sam");
		ll.add("kim");
		ll.add("lol");
		ll.add("sob");
		Account a = new Account(1, ll);
		assertEquals(a.allowedAccess("bob"),true);
	}
	
	@Test
	public void testAllowedAccessEnd(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		ll.add("sam");
		ll.add("kim");
		ll.add("lol");
		ll.add("sob");
		Account a = new Account(1, ll);
		assertEquals(a.allowedAccess("sob"),true);
	}
	
	@Test
	public void testSpaceRecognition(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		ll.add("sam");
		ll.add("kim");
		ll.add("lol");
		ll.add("sob");
		Account a = new Account(1, ll);
		a.printInfo();
		assertEquals(a.allowedAccess(" sob "),false);
	}
	
	@Test
	public void testNegativeBalance(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.setBalance(100);
		assertEquals(a.negativeBalance(1),false);
	}
	
	@Test
	public void testNegativeBalanceClose(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.setBalance(100);
		assertEquals(a.negativeBalance(99.9999999999999999999999999999),false);
	}
	
	@Test
	public void testNegativeBalanceOver(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		Account a = new Account(1, ll);
		a.deposit(100);
		assertEquals(a.negativeBalance(101),true);
	}
	
	@Test
	public void testOneApplicant(){
		Application a = new Application("bob");
		assertEquals(a.printInfo(),1);
	}
	
	@Test
	public void testFiveApplicant(){
		List<String> ll = new LinkedList<>();
		ll.add("bob");
		ll.add("sam");
		ll.add("kim");
		ll.add("lol");
		ll.add("sob");
		Application a = new Application(ll);
		assertEquals(a.printInfo(),5);
	}
/*	
	@Test
	public void test(){
		
	}
	
	@Test
	public void test(){
		
	}
*/
}
