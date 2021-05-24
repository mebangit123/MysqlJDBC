package com.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeeDBService employeeService;

	public List<EmployeePayroll> readData() 
	{
		String sql = "Select * from employee_payroll";
		return this.getEmployeePayrollDataUsingDB(sql);
	}

	private List<EmployeePayroll> getEmployeePayrollData(ResultSet resultSet) 
	{
		List<EmployeePayroll> empList = new ArrayList<>();
		try {
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("basic_pay");	
				empList.add(new EmployeePayroll(id, name, salary));	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return empList;
	}
	
	private List<EmployeePayroll> getEmployeePayrollDataUsingDB(String sql) {
		List<EmployeePayroll> empList = new ArrayList<>();
		try(Connection conn = this.getConnection())
		{
			Statement statement = conn.createStatement();
			ResultSet resutlSet = statement.executeQuery(sql);
			empList = this.getEmployeePayrollData(resutlSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empList;
	}
	
	public static EmployeeDBService getInstance() 
	{
		if(employeeService == null)
			employeeService = new EmployeeDBService();
		return employeeService;
	}
	
	public Connection getConnection() 
	{
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
		String userName = "root";
		String password = "root";
		Connection conn = null; 
		
		try {
			conn = DriverManager.getConnection(jdbcURL,userName,password);
	 	} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
