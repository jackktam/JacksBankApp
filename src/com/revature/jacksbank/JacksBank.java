package com.revature.jacksbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bankobjects.Account;
import bankobjects.Admin;
import bankobjects.BankDatabase;
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
		System.out.println("Welcome to the main menu, please type in an option(\"1\",\" 2\", \"3\").");
		System.out.println("1.Login \n2.Register \n3.Shut Down");
		String selection = scanner.next();
		
		//if input is not one of the valid options catches it and prompts a new input
		while(!selection.equals("1") && !selection.equals("2") && selection.equals("3")) {
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
		System.out.println("Please enter a username and its corresponding password to continue or type in \"menu\" as username to reutrn to main menu.");
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
	}
	
	public static void customerMenu() {
		System.out.println("Customer Menu");
	}
	
}
