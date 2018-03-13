package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.revature.util.ConnectionFactory;

import bankobjects.Application;

public class ApplicationDaoImpl implements ApplicationDao {

	@Override
	public void createApplication(Application application) {
		// TODO Auto-generated method stub
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String sql1 = "SELECT APP_ID_SEQ.NEXTVAL FROM DUAL";
		String sql2;
		
		int aId=0;
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){	
				aId = rs.getInt(1);
			}
			
			rs.close();
			
			for(String s: application.getUsers()) {
				sql2 = "INSERT INTO APP (APPID, USERNAME) VALUES("+String.valueOf(aId)+ ", '" +s+"')";
				ps = conn.prepareStatement(sql2);
				ps.executeUpdate();
				ps.close();
			}
			
			ps.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Application retrieveNextApp() {
		// TODO Auto-generated method stub
		
		List<String> owners = new LinkedList<>();
		String sql = "SELECT USERNAME FROM APP WHERE APPID = (SELECT MIN(APPID) FROM APP)";
		
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
		
			while(rs.next()){	
				owners.add(rs.getString(1));
			}
			
			ps.close();
			rs.close();
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Application app = new Application(owners);
		
		return app;
	}


	@Override
	public void removeNextApplication() {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM APP WHERE APPID = (SELECT MIN(APPID) FROM APP)";
		
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();	
			
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
