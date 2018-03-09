package bankobjects;

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

import com.revature.util.LoggingUtil;

public class BankUserInterface {
	
	private static String fileName = "serialize/serbankdata.ser";
	private static File file = new File(fileName);
	private static BankDatabase data;
	private static Scanner scanner;
	private static User currentUser = new User("", "");
	
	
    public static void start() {
		
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
				LoggingUtil.logError("Bank database not found, initiating new bank with and admin and employee.");
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
	
	private static void menu() {
		
		//default text that shows every time menu is called
		System.out.println("\n\n\n==============================");
		System.out.println("Main Menu                                           +---------------------------------------+");
		System.out.println("1.Login                                             |      ****SPECIAL PROMOTION****        |");
		System.out.println("2.Register                                          | Register and make a new account today |"); 
		System.out.println("3.Shut Down                                         | and we'll start your account off with |"); 
		System.out.println("==============================                      |          A WHOLE $0.01!!!!            |");
		System.out.println("Please type in an option(1-3)                       +---------------------------------------+");
		String selection = scanner.nextLine();
		
		//if input is not one of the valid options catches it and prompts a new input
		while(!selection.equals("1") && !selection.equals("2") && !selection.equals("3")) {
			System.out.println(selection);
			System.out.println("Sorry invalid option, please try again.");
			selection = scanner.nextLine();
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

	private static void login() {
		
		//prompts for a username and password or return to main menu option
		System.out.println("Please enter a username and its corresponding password to continue or type in \"menu\" as username and any not empty password to return to main menu.");
		System.out.print("Username: ");
		String username = scanner.nextLine();	
		while(username.length()==0) {
			username = scanner.nextLine();
		}
		
		System.out.print("Password: ");
		String password = scanner.nextLine();
		while(password.length()==0) {
			password = scanner.nextLine();
		}
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
				LoggingUtil.logInfo("Admin " + currentUser.getUsername() + " has logged in.");
				adminMenu();
			}else if(currentUser instanceof Employee) {
				LoggingUtil.logInfo("Employee " + currentUser.getUsername() + " has logged in.");
				employeeMenu();
			}else {
				LoggingUtil.logInfo("Customer " + currentUser.getUsername() + " has logged in.");
				customerMenu();
			}
			
		}else {//if invalid username or password, returns to login menu
			System.out.println("Sorry invalid username and/or password, please try again.\n");
			login();
		}
		
	}
	
	private static void register() {
		
		//prompts user to register with a user name and a password or return to main menu
		System.out.println("Please choose a username and a password or return to the main menu by typing in menu as your username");
		System.out.print("Username: ");
		String username = scanner.nextLine();
		while(username.length()==0) {
			username = scanner.nextLine();
		}
		
		System.out.print("Password: ");
		String password = scanner.nextLine();
		while(password.length()==0) {
			password = scanner.nextLine();
		}
		
		//if "menu" is entered as the user name, they are returned to the main menu
		if(username.equals("menu")) {
			
			System.out.println("\n\n\n");
			menu();
			
		}else if(data.getLoginInfo().containsKey(username)) {//if user name is already taken
			
			System.out.println("Sorry, the desired username is already taken.");
			register();
			
		}else {//if username not taken, registers user and returns to main menu
			
			Customer tempCustomer = new Customer(username, password);
			data.getUser().add(tempCustomer);
			data.getLoginInfo().put(username, password);
			LoggingUtil.logInfo("Customer " + tempCustomer.getUsername() + " has registered a new account.");
			System.out.println("Congratulations, you have sucessfully registered for an account! Returning to main menu.\n\n\n");
			menu();
			
		}
	}

	private static void shutDown() {
		
		//asks user if he/she is sure about exiting system
		System.out.println("Are you sure you want to shutdown the system? Type in 1 to return to the main menu and 2 to shut down.");		
		String valid = scanner.nextLine();
		
		//catches input if invalid and prompts for new input
		while(!valid.equals("1") && !valid.equals("2")) {
			System.out.println("Sorry invalid input, please try again.");
			valid = scanner.nextLine();
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
	
	private static void adminMenu() {
		
		System.out.println("\n\n\n==============================\nAdmin Menu");
		System.out.println("1.View Account Info\n2.View Account Application\n3.Withdraw Funds\n4.Deposit Funds\n5.Transfer Funds\n6.Delete Account\n7.List of All Customers\n8.List of All Active Accounts\n9.Logout\n==============================");
		System.out.println("Please select a option by typing in a valid number(1-9).");
		String choice = scanner.nextLine();
		
		//error trapping invalid inputs until valid input is entered
		while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5") 
				&& !choice.equals("6") && !choice.equals("7") && !choice.equals("8") && !choice.equals("9")) {
			System.out.println("Invalid option, please try again");
			choice = scanner.nextLine();
		}
		
		
		
		if(choice.equals("1")) {//View Account Info
			
			System.out.println("Please enter the ID of the account whose info you want to view or enter \"menu\" to return to admin menu.");
			String accountID = scanner.nextLine();
			while(accountID.length()==0) {
				accountID = scanner.nextLine();
			}
			boolean accountParse = true;
			
			for(int j=0 ; j<accountID.length() ; j++) {
				if(!Character.isDigit(accountID.charAt(j))) {//if current char is not a digit
					accountParse = false;
				}
			}
			
			int index = -1;
			if(accountParse==true && accountID.length()<10) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(accountID), null), new Account(0, null));
			}
			
			if(index<0) {
				System.out.println("Specified account does not exist, we are now returning to admin menu.");
				adminMenu();
			}else {
				data.getAccount().get(index).printInfo();
				System.out.println("Enter anything to continue to admin menu.");
				scanner.nextLine();
				System.out.println("Returning to admin menu.");
				adminMenu();
			}
			
		}else if(choice.equals("2")) {//View Account Application
			
			if(!data.getApplication().isEmpty()) {//if there is a application
				
				//prints out oldest application and prompts for an action
				data.getApplication().get(0).printInfo();
				String option = scanner.nextLine();
				
				//if input is invalid, prompts for input until it is valid
				while(!option.equals("1") && !option.equals("2") && !option.equals("3")) {
					System.out.println("Invalid option, please try again.");
					option = scanner.nextLine();
				}
				
				if(option.equals("1")) {//approve account
					
					if(!data.getAccount().isEmpty()) {//if account list is not empty
						
						int newID = data.getAccount().get(data.getAccount().size()-1).getId()+1;
						
						for(int i=0 ; i<data.getApplication().get(0).getUsers().size() ; i++) {//goes through list of users in application
							
							User tempUser = new User("","");
							
							//goes through list of users to find the one with correct user name
							for(int j=0 ; !tempUser.getUsername().equals(data.getApplication().get(0).getUsers().get(i)) ; j++) {
								if(data.getUser().get(j).getUsername().equals(data.getApplication().get(0).getUsers().get(i))) {//once found, sets that user to the temp user
									tempUser = data.getUser().get(j);
									((Customer)tempUser).getOwnedAccounts().add(newID);
								}
							}
							
						}
						
						//adds new account to account list with id 1 higher than account with highest id and application's specified users
						data.getAccount().add(new Account(newID, data.getApplication().get(0).getUsers()));
						LoggingUtil.logInfo("Account " + newID + " created with account balance of $0.01.");
						
					}else {//if account list is empty
						
						int newID = 1000;
						
						for(int i=0 ; i<data.getApplication().get(0).getUsers().size() ; i++) {//goes through list of users in application
							
							User tempUser = new User("","");
							
							//goes through list of users to find the one with correct user name
							for(int j=0 ; !tempUser.getUsername().equals(data.getApplication().get(0).getUsers().get(i)) ; j++) {
								if(data.getUser().get(j).getUsername().equals(data.getApplication().get(0).getUsers().get(i))) {//once found, sets that user to the temp user
									tempUser = data.getUser().get(j);
									((Customer)tempUser).getOwnedAccounts().add(newID);
								}
							}
							
						}
						//adds new account to account list with id of 1 and application's specified users
						data.getAccount().add(new Account(1000, data.getApplication().get(0).getUsers()));
						LoggingUtil.logInfo("Account " + newID + " created with account balance of $0.01.");
					}
					
					//removes approved application from application list
					data.getApplication().remove(0);
					System.out.println("Application approved, returning to admin menu.");
					adminMenu();
					
				}else if(option.equals("2")) { //deny account
					//removes application from list of open applications
					data.getApplication().remove(0);
					System.out.println("Application denied, returning to admin menu.");
					adminMenu();
				}else { //return to employee menu
					System.out.println("Returning to employee menu.");
					adminMenu();
				}
				
			}else {//no application currently
				System.out.println("There are no applications to be viewed, returning to admin menu.");
				adminMenu();
			}
			
		}else if(choice.equals("3")) {//Withdraw Funds
			//prompts for account number and amount of money they want to withdrawn or return to menu
			System.out.println("Please enter the account number of the account you want to withdrawn from.");
			String account = scanner.nextLine();
			while(account.length()==0) {
				account = scanner.nextLine();
			}
			
			System.out.println("Please enter an amount of cash to withdraw in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the admin menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
			
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
			
			if(accountParse==true && account.length()<10) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if admin menu is selected
				System.out.println("Returning to admin menu.");
				adminMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to admin menu.");
				adminMenu();
			}else if(index<0) {//if account id not found
				System.out.println("Sorry account id not found, returning to admin menu.");
				adminMenu();
			}else if(data.getAccount().get(index).negativeBalance(doubleAmount)) {//if not enough funds
				System.out.println("Sorry there is not have enough funds in this account, returning to admin menu.");
				adminMenu();
			}else if(amount.length() > 12){
				System.out.println("Sorry we can not authorize transactions of this scale, returning to admin menu.");
				adminMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).withdraw(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " withdrawn from account " + account + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " withdrawn from account " + account + ", returning to admin menu.");
				adminMenu();
			}
		}else if(choice.equals("4")) {//Deposit Funds
			
			//prompts for account number and amount of money they want to deposit or return to menu
			System.out.println("Please enter the account number of the account you want to deposit to.");
			String account = scanner.nextLine();
			while(account.length()==0) {
				account = scanner.nextLine();
			}
			
			System.out.println("Please enter an amount of cash to deposit in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the admin menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
			
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
			
			if(accountParse==true && account.length()<10) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if admin menu is selected
				System.out.println("Returning to admin menu.");
				adminMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to admin menu.");
				adminMenu();
			}else if(index<0) {//if account id not found
				System.out.println("Sorry account id not found, returning to admin menu.");
				adminMenu();
			}else if(amount.length() > 12){//value too high
				System.out.println("Sorry we can not authorize transactions of this scale, returning to admin menu.");
				adminMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).deposit(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " deposited to account " + account + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " deposited to account " + account + ", returning to admin menu.");
				adminMenu();
			}
			
		}else if(choice.equals("5")) {//Transfer Funds	
			
			System.out.println("Please enter the account id of the account you want to transfer funds from.");
			String account1 = scanner.nextLine();
			while(account1.length()==0) {
				account1 = scanner.nextLine();
			}
			
			System.out.println("Please enter the account id of the account you want to transfer funds to.");
			String account2 = scanner.nextLine();
			while(account2.length()==0) {
				account2 = scanner.nextLine();
			}
			
			System.out.println("Please enter the amount of funds you want to transfer in the correct format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter 0 to return to admin menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
			
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
			
			if(account1Parse==true && account1.length()<10) {//finds index of account 1 if parseable
				index1 = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account1), null), new Account(0, null));
			}
			if(account2Parse==true && account2.length()<10) {//finds index of account 2 if parseable
				index2 = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account2), null), new Account(0, null));
			}
			
			if(amount.equals("0")) {//if customer menu is selected
				System.out.println("Returning to admin menu.");
				adminMenu();
			}else if(amountParse==false) {//amount is not valid input
				System.out.println("Sorry invalid amount entered or format is not correct, returning to admin menu.");
				adminMenu();
			}else if(index1<0 || index2<0) {//if account id not found
				System.out.println("Sorry one or more account ids not found, returning to admin menu.");
				adminMenu();
			}else if(amount.length() > 12){//value too high
				System.out.println("Sorry we can not authorize transactions of this scale, returning to admin menu.");
				adminMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index1).withdraw(doubleAmount);
				data.getAccount().get(index2).deposit(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " transfered from account " + account1 + " to account " + account2 + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " transfered from account " + account1 + " to account " + account2 + ", returning to admin menu.");
				adminMenu();
			}
			
		}else if(choice.equals("6")) {//Delete account
			
			System.out.println("Please enter the ID of the account you want to delete or enter 0 to return to admin menu.");
			String account = scanner.nextLine();
			while(account.length()==0) {
				account = scanner.nextLine();
			}
			
			boolean parseable = true;
			
			for(int i=0 ; i<account.length() ; i++) {
				if(!Character.isDigit(account.charAt(i))) {
					parseable = false;
				}
			}
			
			int index = -1;
			
			if(parseable==true) {//if parseable, binary search for account index
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account), null), new Account(0, null));
			}
			
			if(account.equals("0")) {//if returning to admin menu was selected
				System.out.println("Returning to admin menu.");
				adminMenu();
			}else if(index<0){//if account not found
				System.out.println("Account not found, returning to admin menu.");
				adminMenu();
			}else {
				
				String ownerName = "";
				User ownerUser = new User("","");
				for(int i=0 ; i<data.getAccount().get(index).getOwners().size() ; i++) {//go through list of accounts owners
					
					//set target name to the current account owner's name
					ownerName = data.getAccount().get(index).getOwners().get(i);
					for(int j=0 ; !ownerUser.getUsername().equals(ownerName) ; j++) {//traverses list of users until finds the one with correct user name
						if(data.getUser().get(j).getUsername().equals(ownerName)) {//if current user has correct user name
							//remove account id from the owner's list of accounts owned and sets moves to next account owner
							Integer targetInt = new Integer(data.getAccount().get(index).getId());
							((Customer)data.getUser().get(j)).getOwnedAccounts().remove((Object)targetInt);
							ownerUser = data.getUser().get(j);
						}
					}
				}
				data.getAccount().remove(index);
				LoggingUtil.logInfo("Account " + account + " deleted.");
				System.out.println("Account ID " + account + " deleted, returning to admin menu.");
				adminMenu();
			}
			
		}else if(choice.equals("7")) {//all customers
			
			System.out.println("Registered Customers\n==============================");
			
			for(User u: data.getUser()) {
				if(u instanceof Customer) {
					System.out.println(u.getUsername());
				}
			}
			
			System.out.println("==============================\nEnter anything to continue to previous menu.");
			scanner.nextLine();
			adminMenu();
			
		}else if(choice.equals("8")) {//view all accounts
			
			System.out.println("Active Account IDs\n==============================");
			
			for(Account a: data.getAccount()) {
				System.out.println(a.getId());
			}
			
			System.out.println("==============================\nEnter anything to continue to previous menu.");
			scanner.nextLine();
			adminMenu();
			
		}
			else {//Logout
			//clears current user and return to menu screen
			System.out.println("Logging out and returning to main menu\n\n\n");
			currentUser = new User("","");
			menu();
		}	
	}
	
	private static void employeeMenu() {
		
		System.out.println("\n\n\n==============================\nEmployee Menu");
		System.out.println("1.View Customer Info\n2.View Account Info\n3.Approve/Deny Account Applcations\n4.Logout\n==============================");
		System.out.println("Please select a option by typing in a valid number(1-4).");
		String choice = scanner.nextLine();
		
		//error trapping invalid inputs until valid input is entered
		while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
			System.out.println("Invalid option, please try again");
			choice = scanner.nextLine();
		}
		
		if(choice.equals("1")) {//View Customer info
			
			//prompts for username of customer info that you want to view
			System.out.println("Please enter the username of the customer whose info you want to view or enter \"menu\" to return to employee menu.");
			String customerName = scanner.nextLine();
			while(customerName.length()==0) {
				customerName = scanner.nextLine();
			}
			
			if(data.getLoginInfo().containsKey(customerName)) {//if user name is a valid user
				
				User tempUser = new User("","");
				
				//goes through list of users to find the one with correct user name
				for(int i=0 ; !tempUser.getUsername().equals(customerName) ; i++) {
					if(data.getUser().get(i).getUsername().equals(customerName)) {//once found, sets that user to the temp user
						tempUser = data.getUser().get(i);
					}
				}
				
				if(tempUser instanceof Customer) {//if specified user is a customer, print info
					((Customer)tempUser).printInfo();
					System.out.println("Enter anything to continue to employee menu.");
					scanner.nextLine();
					System.out.println("Returning to employee menu.");
					employeeMenu();
				}else {//if specified user is not a customer, return to employee menu
					System.out.println("Sorry target user's information is not viewable, returning to employee menu.");
					employeeMenu();
				}
				
			}else {//if not valid user name
				System.out.println("User not found, returning to employee menu.");
				employeeMenu();
			}
			
		}else if(choice.equals("2")) {//View Account Info
			System.out.println("Please enter the ID of the account whose info you want to view or enter \"menu\" to return to employee menu.");
			String accountID = scanner.nextLine();
			
			while(accountID.length()==0) {
				accountID = scanner.nextLine();
			}
			
			boolean accountParse = true;
			
			for(int j=0 ; j<accountID.length() ; j++) {
				if(!Character.isDigit(accountID.charAt(j))) {//if current char is not a digit
					accountParse = false;
				}
			}
			
			int index = -1;
			if(accountParse==true && accountID.length()<10) {
				index = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(accountID), null), new Account(0, null));
			}
			
			if(index<0) {
				System.out.println("Specified account does not exist, we are now returning to employee menu.");
				employeeMenu();
			}else {
				data.getAccount().get(index).printInfo();
				System.out.println("Enter anything to continue to employee menu.");
				scanner.nextLine();
				System.out.println("Returning to employee menu.");
				employeeMenu();
			}
			
		}else if(choice.equals("3")) {//View Account Applications
			if(!data.getApplication().isEmpty()) {
				
				//prints out oldest application and prompts for an action
				data.getApplication().get(0).printInfo();
				String option = scanner.nextLine();
				
				//if input is invalid, prompts for input until it is valid
				while(!option.equals("1") && !option.equals("2") && !option.equals("3")) {
					System.out.println("Invalid option, please try again.");
					option = scanner.nextLine();
				}
				
				if(option.equals("1")) {//approve account
					
					if(!data.getAccount().isEmpty()) {//if account list is not empty
						int newID = data.getAccount().get(data.getAccount().size()-1).getId()+1;
						
						for(int i=0 ; i<data.getApplication().get(0).getUsers().size() ; i++) {//goes through list of users in application
							
							User tempUser = new User("","");
							
							//goes through list of users to find the one with correct user name
							for(int j=0 ; !tempUser.getUsername().equals(data.getApplication().get(0).getUsers().get(i)) ; j++) {
								if(data.getUser().get(j).getUsername().equals(data.getApplication().get(0).getUsers().get(i))) {//once found, sets that user to the temp user
									tempUser = data.getUser().get(j);
									((Customer)tempUser).getOwnedAccounts().add(newID);
								}
							}
							
						}
						//adds new account to account list with id 1 higher than account with highest id and application's specified users
						data.getAccount().add(new Account(newID, data.getApplication().get(0).getUsers()));
						LoggingUtil.logInfo("Account " + newID + " created with account balance of $0.01.");
					}else {//if account list is empty
						
						//adds new account to account list with id of 1 and application's specified users
						int newID = 1000;
						
						for(int i=0 ; i<data.getApplication().get(0).getUsers().size() ; i++) {//goes through list of users in application
							
							User tempUser = new User("","");
							
							//goes through list of users to find the one with correct user name
							for(int j=0 ; !tempUser.getUsername().equals(data.getApplication().get(0).getUsers().get(i)) ; j++) {
								if(data.getUser().get(j).getUsername().equals(data.getApplication().get(0).getUsers().get(i))) {//once found, sets that user to the temp user
									tempUser = data.getUser().get(j);
									((Customer)tempUser).getOwnedAccounts().add(newID);
								}
							}
							
						}
						data.getAccount().add(new Account(1000, data.getApplication().get(0).getUsers()));
						LoggingUtil.logInfo("Account " + newID + " created with account balance of $0.01.");
					}
					
					//removes approved application from application list
					data.getApplication().remove(0);
					System.out.println("Application approved, returning to employee menu.");
					employeeMenu();
					
				}else if(option.equals("2")) { //deny account
					//removes application from list of open applications
					data.getApplication().remove(0);
					System.out.println("Application denied, returning to employee menu.");
					employeeMenu();
				}else { //return to employee menu
					System.out.println("Returning to employee menu.");
					employeeMenu();
				}
				
			}else {//no application currently
				System.out.println("There are no applications to be viewed, returning to employee menu.");
				employeeMenu();
			}
		}else {//logout
			//clears current user and return to menu screen
			System.out.println("Logging out and returning to main menu\n\n\n");
			currentUser = new User("","");
			menu();
		}
		
	}
	
	private static void customerMenu() {
		
		//prompts customer for an action
		System.out.println("\n\n\n==============================\nCustomer Menu");
		System.out.println("1.Apply for an account \n2.Withdraw Funds \n3.Deposit Funds \n4.Transfer Funds \n5.View Accounts\n6.Log out\n==============================");
		System.out.println("Please select a option by typing in a valid number(1-6).");
		String choice = scanner.nextLine();
		
		//error trapping invalid inputs until valid input is entered
		while(!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4") && !choice.equals("5") && !choice.equals("6")) {
			System.out.println("Invalid option, please try again");
			choice = scanner.nextLine();
		}
		
		
		if(choice.equals("1")) {//if applying for account is selected
			
			//prompts for type of account that user wants to apply for or to return to main menu
			System.out.println("Applying for account");
			System.out.println("1.Individual Account\n2.Joint Account\n3.Customer Menu");
			System.out.println("Please enter a valid choice");
			String accountType = scanner.nextLine();
			
			while(!accountType.equals("1") && !accountType.equals("2") && !accountType.equals("3")) {
				System.out.println("Invalid option, please try again");
				accountType = scanner.nextLine();
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
					jointOwners = scanner.nextLine();
					
					while(jointOwners.length()==0) {
						jointOwners = scanner.nextLine();
					}
					
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
						}else {//valid customer's user name
							otherOwners.add(jointOwners);
							System.out.println("You have added " + jointOwners + " to the joint application.");
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
			String account = scanner.nextLine();
			while(account.length()==0) {
				account = scanner.nextLine();
			}
			
			System.out.println("Please enter an amount of cash to withdraw in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the customer menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
			
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
			
			if(accountParse==true && account.length()<10) {
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
			}else if(amount.length() > 12){
				System.out.println("Sorry we can not authorize transactions of this scale, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).withdraw(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " withdrawn from account " + account + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " withdrawn from account " + account + ", returning to customer menu.");
				customerMenu();
			}
			
		}else if(choice.equals("3")) {//if depositing funds is selected
			
			//prompts for account number and amount of money they want to deposit or return to menu
			System.out.println("Please enter the account number of the account you want to deposit to.");
			String account = scanner.nextLine();
			while(account.length()==0) {
				account = scanner.nextLine();
			}
			
			System.out.println("Please enter an amount of cash to deposit in the following format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter \"0\" to go back to the customer menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
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
			
			if(accountParse==true && account.length()<10) {
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
			}else if(amount.length() > 12){
				System.out.println("Sorry we can not authorize transactions of this scale, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index).deposit(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " deposited to account " + account + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " deposited to account " + account + ", returning to customer menu.");
				customerMenu();
			}
			
		}else if(choice.equals("4")) {//if transferring funds is selected
			
			System.out.println("Please enter the account id of the account you want to transfer funds from.");
			String account1 = scanner.nextLine();
			while(account1.length()==0) {
				account1 = scanner.nextLine();
			}
			
			System.out.println("Please enter the account id of the account you want to transfer funds to.");
			String account2 = scanner.nextLine();
			while(account2.length()==0) {
				account2 = scanner.nextLine();
			}
			
			System.out.println("Please enter the amount of funds you want to transfer in the correct format(d.cc, dd.cc, etc... or d, dd, ddd, etc...) or enter 0 to return to main menu.");
			String amount = scanner.nextLine();
			while(amount.length()==0) {
				amount = scanner.nextLine();
			}
			
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
			
			if(account1Parse==true && account1.length()<10) {//finds index of account 1 if parseable
				index1 = Collections.binarySearch(data.getAccount(), new Account(Integer.parseInt(account1), null), new Account(0, null));
			}
			if(account2Parse==true && account2.length()<10) {//finds index of account 2 if parseable
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
			}else if(amount.length() > 12){
				System.out.println("Sorry we can not authorize transactions of this scale, returning to customer menu.");
				customerMenu();
			}else {//permitted access with sufficient funds
				data.getAccount().get(index1).withdraw(doubleAmount);
				data.getAccount().get(index2).deposit(doubleAmount);
				LoggingUtil.logInfo("$" + amount + " transfered from account " + account1 + " to account " + account2 + " by " + currentUser.getUsername());
				System.out.println("$" + amount + " transfered from account " + account1 + " to account " + account2 + ", returning to customer menu.");
				customerMenu();
			}
			
		}else if(choice.equals("5")){//view account info
			((Customer)currentUser).printAccounts();
			System.out.println("Enter anything to return to continue to previous menu.");
			scanner.nextLine();
			customerMenu();
		}else {//if log out is selected1
			
			//clears current user and return to menu screen
			System.out.println("Logging out and returning to main menu\n\n\n");
			currentUser = new User("","");
			menu();
			
		}
		
	}
}
