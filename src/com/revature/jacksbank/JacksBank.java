package com.revature.jacksbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bankobjects.Account;
import bankobjects.Admin;
import bankobjects.Application;
import bankobjects.BankDatabase;
import bankobjects.Customer;
import bankobjects.Employee;
import bankobjects.User;



public class JacksBank {
	private static String fileName = "serialize/serbankdata.ser";
	static File file = new File(fileName);
	static BankDatabase data;
	static Scanner scanner;
	static User currentUser = new User("", "");
	
	public static void main(String[] args) {
		
		start();
		
	}
	
	static void start() {
		
		data = new BankDatabase();
		
		//if file not found or file empty, populates database with one admin and one employee before serializing to file and closing
		if(!file.exists() || file.length()==0) {
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {		
				Admin tempAdmin = new Admin("admin", "admin");
				data.getLoginInfo().put("admin", "admin");
				data.getUser().add(tempAdmin);
				Employee tempEmployee = new Employee("employee","employee");
				data.getLoginInfo().put("employee","employee");
				data.getUser().add(tempEmployee);
				out.writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		
		//deserialize database from file and stores it
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {   
	        data = (BankDatabase) in.readObject();
	     } catch (IOException i) {
	    	 i.printStackTrace();
	     } catch (ClassNotFoundException c) {
	    	 System.out.println("BankDatabase class not found");
	    	 c.printStackTrace();
	     }
		scanner = new Scanner(System.in);
		
		//enters menu on completion of setup
		menu();
		
	}
	
	public static void menu() {
		
		//default text that shows every time menu is called
		System.out.println("Main Menu");  
		System.out.println("1.Login \n2.Register \n3.Shut Down");
		System.out.println("Please type in an option(\"1\",\" 2\", \"3\").");
		String selection = scanner.next();
		
		//if input is not one of the valid options catches it and prompts a new input
		while(!selection.equals("1") && !selection.equals("2") && !selection.equals("3")) {
			System.out.println(selection);
			System.out.println("Sorry invalid option, please try again.");
			selection = scanner.next();
		}
		
		//routes to selected menu selection
		if(selection.equals("1")) {
			System.out.println("Welcome to the login menu.");
			login();	
		}else if(selection.equals("2")) {
			register();
		}else{
			shutDown();
		}
		
	}

	public static void login() {
		
		//prompts for a username and password or return to main menu option
		System.out.println("Please enter a username and its corresponding password to continue or type in \"menu\" as username and any not empty password to reutrn to main menu.");
		System.out.print("Username: ");
		String username = scanner.next();	
		System.out.print("Password: ");
		String password = scanner.next();
		//if "menu" is entered as the username, they are returned to the main menu
		if(username.equals("menu")) {
			
			menu();
			
		}else if(data.getLoginInfo().containsKey(username) && 
				data.getLoginInfo().get(username).equals(password)) {//checks to see if the username is valid and if the password entered is correct
			
			//goes through list of users to find the one with correct user name
			for(int i=0 ; !currentUser.getUsername().equals(username) ; i++) {
				if(data.getUser().get(i).getUsername().equals(username)) {//once found, sets that user to the current user
					currentUser = data.getUser().get(i);
				}
			}
			
			if(currentUser instanceof Admin) {
				adminMenu();
			}else if(currentUser instanceof Employee) {
				employeeMenu();
			}else {
				customerMenu();
			}
			
		}else {//if invalid username or password, returns to login menu
			System.out.println("Sorry invalid username and/or password, please try again.\n");
			login();
		}
		
	}
	
	public static void register() {
		
		//prompts user to register with a user name and a password or return to main menu
		System.out.println("Please choose a username and a password or return to the main menu by typing in menu as your username");
		System.out.print("Username: ");
		String username = scanner.next();	
		System.out.print("Password: ");
		String password = scanner.next();
		
		//if "menu" is entered as the user name, they are returned to the main menu
		if(username.equals("menu")) {
			
			menu();
			
		}else if(data.getLoginInfo().containsKey(username)) {//if user name is already taken
			
			System.out.println("Sorry, the desired username is already taken.");
			register();
			
		}else {//if username not taken, registers user and returns to main menu
			
			Customer tempCustomer = new Customer(username, password);
			data.getUser().add(tempCustomer);
			data.getLoginInfo().put(username, password);
			System.out.println("Congratulations, you have sucessfully registered for an account! Returning to main menu.");
			menu();
			
		}
	}

	public static void shutDown() {
		
		//asks user if he/she is sure about exiting system
		System.out.println("Are you sure you want to shutdown the system? Type in 1 to return to the main menu and 2 to shut down.");		
		String valid = scanner.next();
		
		//catches input if invalid and prompts for new input
		while(!valid.equals("1") && !valid.equals("2")) {
			System.out.println("Sorry invalid input, please try again.");
			valid = scanner.next();
		}
		
		//returns to main menu if 1 is selected
		if(valid.equals("1")) {
			System.out.println("Returning to main menu.");
			menu();
		}else {
			//if 2 is selected, serializes database to specified file path
			System.out.println("Thank you for using Jack's Bank, system exiting.");
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
				out.writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}

	}
	
	public static void adminMenu() {
		
		System.out.println("Admin Menu");
		
	}
	
	public static void employeeMenu() {
		
		System.out.println("Employee Menu");
		System.out.println("1.View Customer Info\n2.View Account Info\n3.Approve/Deny Account Applcations\n4.Logout");
		System.out.println("Please select a option by typing in a valid number(1-4).");
		String choice = scanner.next();
		
		//error trapping invalid inputs until valid input is entered
		while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
			System.out.println("Invalid option, please try again");
			choice = scanner.next();
		}
		
		if(choice.equals("1")) {//View Customer info
			System.out.println("Please enter the username of the customer whose info you want to view.");
			String customerName = scanner.next();
		}else if(choice.equals("2")) {//View Account Info
			
		}else if(choice.equals("3")) {//View Account Applications
			
		}else {//logout
			//clears current user and return to menu screen
			System.out.println("Logging out and returning to main menu");
			currentUser = null;
			menu();
		}
		
	}
	
	public static void customerMenu() {
		
		//prompts customer for an action
		System.out.println("Customer Menu");
		System.out.println("1.Apply for an account \n2.Withdraw Funds \n3.Deposit Funds \n4.Transfer Funds \n5.Log out");
		System.out.println("Please select a option by typing in a valid number(1-5).");
		String choice = scanner.next();
		
		//error trapping invalid inputs until valid input is entered
		while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5")) {
			System.out.println("Invalid option, please try again");
			choice = scanner.next();
		}
		
		
		if(choice.equals("1")) {//if applying for account is selected
			
			//prompts for type of account that user wants to apply for or to return to main menu
			System.out.println("Applying for account");
			System.out.println("1.Individual Account\n2.Joint Account\n3.Customer Menu");
			System.out.println("Please enter a valid choice");
			String accountType = scanner.next();
			
			while(!accountType.equals("1") && !accountType.equals("2") && !accountType.equals("3")) {
				System.out.println("Invalid option, please try again");
				accountType = scanner.next();
			}
			
			if(accountType.equals("1")) {//if applied for individual account
				
				Application tempApp = new Application(currentUser.getUsername());
				data.getApplication().add(tempApp);
				System.out.println("You have sucessfully applied for an account, we are now returning to customer menu.");
				customerMenu();
				
			}else if(accountType.equals("2")) {//applying for joint account
				
				List<String> otherOwners = new LinkedList<>();
				otherOwners.add(currentUser.getUsername());
				String jointOwners = "";
				
				while(!jointOwners.equals("0")) {
					
					System.out.println("Creating joint account, please enter a valid username with whom you want to share an account or enter 0 if you have listed all users.");
					jointOwners = scanner.next();
					
					if(data.getLoginInfo().containsKey(jointOwners)) {//if exiting user name is entered
						
						//goes through list of users to find the one with correct user name
						User tempUser = new User("","");
						for(int i=0 ; !tempUser.getUsername().equals(jointOwners) ; i++) {
							if(data.getUser().get(i).getUsername().equals(jointOwners)) {//once found, sets that user to the temp user
								tempUser = data.getUser().get(i);
							}
						}						
						
						if(otherOwners.contains(jointOwners)) {//if entered user name has previously been added already							
							System.out.println("The user you have entered has already been added previously.");
						}else if(tempUser instanceof Admin || tempUser instanceof Employee){//if entered user is an admin or employee
							System.out.println("Sorry target user cannot own an account.");
						}

					}else {//if user name doesn't exit			
						System.out.println("Sorry target user does not exist.");						
					}
				}
					
				//creates application with current user's user name and all other previously entered user names
				Application tempApp = new Application(otherOwners);
				data.getApplication().add(tempApp);
				System.out.println("You have sucessfully applied for an account, we are now returning to customer menu.");
				customerMenu();
				
			}else {//return to customer menu
				customerMenu();
			}
			
		}else if(choice.equals("2")) {//if withdrawing funds is selected
			
			//prompts for account number and amount of money they want to withdrawn or return to menu
			System.out.println("Please enter the account number of the account you want to withdrawn from.");
			String account = scanner.next();
			System.out.println("Please enter an amount of cash to withdraw in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the customer menu.");
			String amount = scanner.next();
			boolean amountParse = true;
			boolean accountParse = true;
			
			double doubleAmount = 0;
			
			for(int i=0 ; i<amount.length() ; i++) {//check if amount can be converted to correctly formated double
				if(!Character.isDigit(amount.charAt(i))) {//if current char is not a digit
					if(amount.charAt(i)=='.' && i==amount.length()-3) {//if it is a '.' and in correct spot nothing happens
						
					}else {//else marked as not parseable
						amountParse = false;
					}
				}
			}
			
			if(amountParse) {
				doubleAmount = Double.parseDouble(amount);
			}
			
			for(int j=0 ; j<account.length() ; j++) {
				if(!Character.isDigit(account.charAt(j))) {//if current char is not a digit
					accountParse = false;
				}
			}
			
			int index = -1 ;
			
			if(accountParse==true) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if customer menu is selected
				System.out.println("Returning to customer menu.");
				customerMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to customer menu.");
				customerMenu();
			}else if(index<0) {//if account id not found
				System.out.println("Sorry account id not found, returning to customer menu.");
				customerMenu();
			}else if(data.getAccount().get(index).allowedAccess(currentUser.getUsername())==false) {//if account does not belong to user
				System.out.println("Sorry you do not have access to this account, returning to customer menu.");
				customerMenu();
			}else if(data.getAccount().get(index).negativeBalance(doubleAmount)) {//if not enough funds
				System.out.println("Sorry you do not have enough funds in this account, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).withdraw(doubleAmount);
				System.out.println(amount + "withdrawn from account " + account + ", returning to customer menu.");
				customerMenu();
			}
			
		}else if(choice.equals("3")) {//if depositing funds is selected
			
			//prompts for account number and amount of money they want to deposit or return to menu
			System.out.println("Please enter the account number of the account you want to deposit to.");
			String account = scanner.next();
			System.out.println("Please enter an amount of cash to deposit in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the customer menu.");
			String amount = scanner.next();
			boolean amountParse = true;
			boolean accountParse = true;
			
			double doubleAmount = 0;
			
			for(int i=0 ; i<amount.length() ; i++) {//check if amount can be converted to correctly formated double
				if(!Character.isDigit(amount.charAt(i))) {//if current char is not a digit
					if(amount.charAt(i)=='.' && i==amount.length()-3) {//if it is a '.' and in correct spot nothing happens
						
					}else {//else marked as not parseable
						amountParse = false;
					}
				}
			}
			
			if(amountParse) {
				doubleAmount = Double.parseDouble(amount);
			}
			
			for(int j=0 ; j<account.length() ; j++) {
				if(!Character.isDigit(account.charAt(j))) {//if current char is not a digit
					accountParse = false;
				}
			}
			
			int index = -1 ;
			
			if(accountParse==true) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if customer menu is selected
				System.out.println("Returning to customer menu.");
				customerMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to customer menu.");
				customerMenu();
			}else if(index<0) {//if account id not found
				System.out.println("Sorry account id not found, returning to customer menu.");
				customerMenu();
			}else if(data.getAccount().get(index).allowedAccess(currentUser.getUsername())==false) {//if account does not belong to user
				System.out.println("Sorry you do not have access to this account, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).deposit(doubleAmount);
				System.out.println(amount + "deposited to account " + account + ", returning to customer menu.");
				customerMenu();
			}
			
		}else if(choice.equals("4")) {//if transferring funds is selected
			
			System.out.println("Please enter the account id of the account you want to transfer funds from.");
			String account1 = scanner.next();
			System.out.println("Please enter the account id of the account you want to transfer funds to.");
			String account2 = scanner.next();
			System.out.println("Please enter the amount of funds you want to transfer in the correct format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter 0 to retun to main menu.");
			String amount = scanner.next();
			
			boolean amountParse = true;
			boolean account1Parse = true;
			boolean account2Parse = true;
			
			double doubleAmount = 0;
			
			for(int i=0 ; i<amount.length() ; i++) {//check if amount can be converted to correctly formated double
				if(!Character.isDigit(amount.charAt(i))) {//if current char is not a digit
					if(amount.charAt(i)=='.' && i==amount.length()-3) {//if it is a '.' and in correct spot nothing happens
						
					}else {//else marked as not parseable
						amountParse = false;
					}
				}
			}
			
			if(amountParse) {
				doubleAmount = Double.parseDouble(amount);
			}
			
			for(int j=0 ; j<account1.length() ; j++) {
				if(!Character.isDigit(account1.charAt(j))) {//if current char is not a digit
					account1Parse = false;
				}
			}
			
			for(int j=0 ; j<account2.length() ; j++) {
				if(!Character.isDigit(account2.charAt(j))) {//if current char is not a digit
					account2Parse = false;
				}
			}
			
			int index1 = -1;
			int index2 = -1;
			
			if(account1Parse==true) {//finds index of account 1 if parseable
				index1 = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account1), null), new Account(0, null));
			}
			if(account2Parse==true) {//finds index of account 2 if parseable
				index2 = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account2), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if customer menu is selected
				System.out.println("Returning to customer menu.");
				customerMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to customer menu.");
				customerMenu();
			}else if(index1<0 || index2<0) {//if account id not found
				System.out.println("Sorry one or more account ids not found, returning to customer menu.");
				customerMenu();
			}else if(data.getAccount().get(index1).allowedAccess(currentUser.getUsername())==false 
					|| data.getAccount().get(index2).allowedAccess(currentUser.getUsername())==false) {//if account does not belong to user
				System.out.println("Sorry you do not have access to one of the accounts, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index1).withdraw(doubleAmount);
				data.getAccount().get(index2).deposit(doubleAmount);
				System.out.println(amount + "transfered from account " + account1 + " to account " + account2 + ", returning to customer menu.");
				customerMenu();
			}
			
		}else {//if log out is selected1
			
			//clears current user and return to menu screen
			System.out.println("Logging out and returning to main menu");
			currentUser = null;
			menu();
			
		}
		
	}
	
}