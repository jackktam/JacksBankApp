package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.revature.util.ConnectionFactory;

import bankobjects.Admin;
import bankobjects.Customer;
import bankobjects.Employee;
import bankobjects.User;

public class UserDaoImpl implements UserDao {

	@Override
	public void createUser(User user) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO USERS (USERID, USERNAME, PASS, TYPEID) VALUES(C_ID_SEQ.NEXTVAL, '"+user.getUsername()+"', '"+user.getPassword()+"', 1) ";
		
		try {
			
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			
			ps.executeUpdate(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public User retrieveUserByName(String username) {
		// TODO Auto-generated method stub
		User user = new User("","");
		
		String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
		
		try {
			PreparedStatement ps = 
					ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				
				if(rs.getInt(4)==1) {
					user = new Customer(rs.getString(2), rs.getString(3));
				}else if(rs.getInt(4)==2) {
					user = new Employee(rs.getString(2), rs.getString(3));
				}else {
					user = new Admin(rs.getString(2), rs.getString(3));
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public List<String> retrieveAllUser() {
		// TODO Auto-generated method stub
		String sql = "SELECT USERNAME FROM USERS";
		List<String> users = new LinkedList<>();
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery();
			
			
			while(rs.next()){
				
				users.add(rs.getString(1));
				
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return users;
	}

}
