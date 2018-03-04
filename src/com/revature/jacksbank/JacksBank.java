package com.revature.jacksbank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		
		//enters menu on completion of setup
		menu();
		
	}
	
	public static void menu() {
		
		shutDown();
		
	}

	public static void register() {
		
	}
	
	public static void login() {
		
	}

	public static void shutDown() {
		
		boolean finished = false;
		
		while(finished!=true) {
			String valid = "";
			System.out.println("Are you sure you want to shutdown the system? \nType in \"yes\" to exit and \"no\" to return to main menu:");
			Scanner scanner = new Scanner(System.in);
			valid = scanner.nextLine();
			scanner.close();
			switch(valid) {
			case "yes": 
				System.out.println("Thank you for using Jack's Bank, system exiting.");
				try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
					out.writeObject(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				finished = true;
				break;
			case "no":
				System.out.println("Returning to main menu.");
				menu();
				break;
			default:
				System.out.println("Sorry invalid input, please try again.");
			}
		}
	}
	
}
