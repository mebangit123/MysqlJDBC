package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
	public String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
	public String userName = "root";
	public String password = "root";
	public Connection conn;
	
	public void checkDataBaseConnection() {
		try {
			//Loading mysql driver.
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
			
			//Establish Connection
			conn = DriverManager.getConnection(jdbcURL,userName,password);
			System.out.println("Connection is successfull...");
		
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
