package com.revature.dao;

import java.sql.CallableStatement;
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
			
			String sql = "INSERT INTO ACCOUNTS(ACCOUNTID, BALANCE) VALUES(?, 0.01)";
			ps = conn.prepareStatement(sql);
		    ps.setInt(1, aId);
		    ps.executeUpdate();
		    
			
			for(String s: app.getUsers()) {
				sql2 = "INSERT INTO CUSTOMERACCOUNT(USERNAME, ACCOUNTID) VALUES(?, ?) ";
				ps = conn.prepareStatement(sql2);
				ps.setString(1, s);
				ps.setInt(2, aId);
				ps.executeUpdate();
			}
			
			ps.close();
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Account retrieveAccountById(int id) {
		Account account = new Account();
		
		String sql = "SELECT * FROM ACCOUNTS WHERE ACCOUNTID = ?";
		String sq2 = "SELECT USERNAME FROM CUSTOMERACCOUNT WHERE ACCOUNTID = ? ";
		
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				account.setId(rs.getInt(1));
				account.setBalance(Double.parseDouble(rs.getString(2)));
			}
			
			ps.close();
			rs.close();
			
			PreparedStatement ps2 = conn.prepareStatement(sq2);
			ps2.setInt(1, id);
			
			ResultSet rs2 = ps2.executeQuery();
			List<String> owners = new LinkedList<>();
			
			while(rs2.next()){
				
				owners.add(rs2.getString(1));
				
			}
			
			ps2.close();
			rs2.close();
			conn.close();
			
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
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				accounts.add(rs.getInt(1));
			}
			
			ps.close();
			rs.close();
			conn.close();
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return accounts;
	}

	@Override
	public void updateAccount(int id, double balance) {
		// TODO Auto-generated method stub
		 
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			Statement statement = conn.createStatement();
			
			String sql = "UPDATE ACCOUNTS SET BALANCE = " + String.valueOf(balance) + " WHERE ACCOUNTID =" + String.valueOf(id);
			
			statement.executeQuery(sql);
			
			LoggingUtil.logInfo("Account " + id + " balance updated to $" + balance);
			
			statement.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteAccount(int id) {
		// TODO Auto-generated method stub
		String sql = "CALL DELETEACCOUNT(?) ";
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, id);
			
			call.execute();
			
			LoggingUtil.logInfo("Account " + id + " deleted");
			
			call.close();
			conn.close();
			
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
