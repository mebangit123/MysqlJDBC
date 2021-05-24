package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeeRepo {
	
	Scanner sc = new Scanner(System.in);
	private Connection conn;
	
	private List<EmployeePayroll> employeePayrollList = new ArrayList<>();
	private EmployeeDBService employeeService;
	
	public EmployeeRepo() {
		employeeService = EmployeeDBService.getInstance();
	}
	
	public EmployeeRepo(List<EmployeePayroll> empList) {
		this();
		this.employeePayrollList = empList;
	}
	
	
	public void checkDataBaseConnection() {
		try 
		{
			//Loading mysql driver.
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
			
			//Establish Connection
			conn = employeeService.getConnection();
			System.out.println("Connection is successfull...");
		
		}catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!", e);
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
	
	public List<EmployeePayroll> readEmployeePayrollData() {
			this.employeePayrollList = employeeService.readData();
		return this.employeePayrollList;
	}
}
