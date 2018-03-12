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
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){	
				aId = rs.getInt(1);
			}
			
			for(String s: application.getUsers()) {
				sql2 = "INSERT INTO APP (APPID, USERID) VALUES("+String.valueOf(aId)+ ", '" +s+"')";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.executeUpdate();
			}
		
			
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
		
		PreparedStatement ps;
		try {
			ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
		
			while(rs.next()){	
				owners.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Application app = new Application(owners);
		
		return app;
	}


	@Override
	public void denyNextApplication() {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM APPLICATION WHERE APPID = (SELECT MIN(APPID) FROM APPLICATION)";
		
		try {
			PreparedStatement ps = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
			ps.executeUpdate();		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
