package com.revature.jacksbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
	
	public static void main(String[] args) {
		start();
	}
	
	static void start() {
		//if file not found or file empty, populates database with one admin and one employee before serializing to file and closing
		if(!file.exists() || file.length()==0) {
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
				data = new BankDatabase();
				Admin tempAdmin = new Admin("admin", "admin");
				data.getUser().add(tempAdmin);
				Employee tempEmployee = new Employee("employee","employee");
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
	         return;
	      } catch (ClassNotFoundException c) {
	         System.out.println("BankDatabase class not found");
	         c.printStackTrace();
	         return;
	      }
		

			scanner = new Scanner(System.in);
		
		//enters menu on completion of setup
		menu();
		
	}
	
	public static void menu() {
		
		System.out.println("Welcome to the main menu, please type in an option(1, 2, 3).");
		System.out.println("1.Login \n2.Register \n3.Shut Down");
		int selection = scanner.nextInt();
		
		while(selection!=1 && selection!=2 && selection!=3) {
			System.out.println("Sorry invalid option, please try again.");
			selection = scanner.nextInt();
		}
		
		if(selection==1) {
			login();
		}else if(selection==2) {
			register();
		}else{
			shutDown();
		}
		
	}

	public static void login() {
		
	}
	
	public static void register() {
		
	}

	public static void shutDown() {
		
		System.out.println("Are you sure you want to shutdown the system? Type in 1 to return to the main menu and 2 to shut down.");
		int valid = scanner.nextInt();
		
		while(valid!=1 && valid!=2) {
			System.out.println("Sorry invalid input, please try again.");
			valid = scanner.nextInt();
		}
		
		if(valid==1) {
			System.out.println("Returning to main menu.");
			menu();
		}else {
			System.out.println("Thank you for using Jack's Bank, system exiting.");
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
				out.writeObject(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}

	}
	
}
