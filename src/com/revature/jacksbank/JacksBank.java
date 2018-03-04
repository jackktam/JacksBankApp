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
		
		menu();
		
	}
	
	public static void menu() {
	
	}

	public static void register() {
		
	}
	
	public static void login() {
		
	}

	public static boolean shutDown() {
		
		return false;
	}
}
