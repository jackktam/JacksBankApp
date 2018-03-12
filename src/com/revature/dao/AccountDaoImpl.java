package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.revature.util.ConnectionFactory;
import com.revature.util.LoggingUtil;

import bankobjects.Account;
import bankobjects.Application;

public class AccountDaoImpl implements AccountDao {
	
	@Override
	public void createAccount(Application app) {
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String sql1 = "SELECT A_ID_SEQ.NEXTVAL FROM DUAL";
		String sql2;
		
		int aId=0;
		
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){	
				aId = rs.getInt(1);
			}
			
			for(String s: app.getUsers()) {
				sql2 = "INSERT INTO CUSTOMERACCOUNT (USERNAME, ACCOUNTID) VALUES("+s+ ", '" +String.valueOf(aId)+"')";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.executeUpdate();
			}
		
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Account retrieveAccountById(int id) {
		Account account = new Account();
		
		String sql = "SELECT * FROM ACCOUNTS WHERE ACCOUNTID = ?";
		String sq2 = "SELECT USERNAME FROM USER WHERE USERID IN (SELECT USERID FROM CUSTOMERACCOUNT WHERE ACCOUNTID = ?";
		
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				
				account.setId(rs.getInt(1));
				account.setBalance(Double.parseDouble(rs.getString(2)));
				
			}
			
			PreparedStatement ps2 = ConnectionFactory.getInstance().getConnection().prepareStatement(sq2);
			ps2.setInt(1, id);
			
			ResultSet rs2 = ps2.executeQuery();
			List<String> owners = new LinkedList<>();
			
			while(rs2.next()){
				
				owners.add(rs2.getString(1));
				
			}
			
			account.setOwners(owners);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return account;
	}

	@Override
	public List<Integer> retrieveAllAccount() {
		
		String sql = "SELECT ACCOUNTID FROM ACCOUNTS";
		List<Integer> accounts = new LinkedList<>();
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			
			while(rs.next()){
				
				accounts.add(rs.getInt(1));
				
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accounts;
	}

	@Override
	public void updateAccount(int id, double balance) {
		// TODO Auto-generated method stub
		 
		Connection conn = ConnectionFactory.getInstance().getConnection();
		try {
			Statement statement = conn.createStatement();
			
			String sql = "UPDATE ACCOUNTS SET BALANCE = " + String.valueOf(balance) + " WHERE ACCOUNTID =" + String.valueOf(id);
			
			statement.executeQuery(sql);
			
			LoggingUtil.logInfo("Account " + id + " balance updated to $" + balance);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAccount(int id) {
		// TODO Auto-generated method stub
		Connection conn = ConnectionFactory.getInstance().getConnection();
		try {
			Statement statement = conn.createStatement();
			
			String sql = "DELETE FROM ACCOUNTS WHERE ACCOUNTID = " + String.valueOf(id);
			
			statement.executeQuery(sql);
			
			LoggingUtil.logInfo("Account " + id + " deleted");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createAccountPreparedStmt(Account account) {
		// TODO Auto-generated method stub
		
	}

}
