package com.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeeDBService employeeService;

	public EmployeePayroll addEmployeeToPayrollUC7(String name, String gender, double salary, LocalDate startDate) {
		int employeeId = -1;
		EmployeePayroll employeeList = null;
		String sql = String.format("Insert into employee_payroll(name, gender, basic_pay, start)" +
									"values( '%s', '%', '%s', '%s')", name, gender,salary, Date.valueOf(startDate));
		try(Connection conn = this.getConnection())
		{
			Statement statement = conn.createStatement();
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1)
			{
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeId = resultSet.getInt(1);
			}
			employeeList = new EmployeePayroll(employeeId, name, salary, startDate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeeList;
	}
	
	public EmployeePayroll addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) 
	{
		int employeeId = -1;
		Connection conn = null;
		EmployeePayroll empPayrollData = null;
			
		conn = this.getConnection();	
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try(Statement statement = conn.createStatement())
		{
			String sql = String.format("Insert into employeePayroll(name, gender, salary, start) values( '%s', '%s', '%s', '%s')", name, gender,salary, Date.valueOf(startDate));
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1)
			{
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next()) employeeId = resultSet.getInt(1);
			}
			//empPayrollData = new EmployeePayroll(employeeId, name, salary, gender, startDate);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		try(Statement statement = conn.createStatement())
		{
			double deductions = salary *0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql1 = String.format("Insert into payroll_details(employee_id, basic_pay, deductions, taxable_pay, tax, net_pay) values ( %s, %s,%s, %s, %s, %s )", employeeId, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql1);
			if(rowAffected == 1)
			{
				empPayrollData = new EmployeePayroll(employeeId, name, salary, startDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally
		{
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return empPayrollData;
	}
	
	public List<EmployeePayroll> readData() 
	{
		String sql = "Select * from employee_payroll";
		return this.getEmployeePayrollDataUsingDB(sql);
	}

	public int updateEmployeeData(String name, double salary) 
	{
		return this.updateEmployeeDataUsingStatement(name, salary);
	}
	
	private int updateEmployeeDataUsingStatement(String name, double salary) 
	{
		String sql = String.format("update employee_payroll set basic_pay = %.2f where name = '%s';",salary, name);
		try(Connection conn = this.getConnection()) {
			Statement statement = conn.createStatement();
			return statement.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<EmployeePayroll> getEmployeeWithInDateRange(LocalDate startDate, LocalDate endDate)
	{
		String sql = String.format("select * from employee_payroll where start between '%s' and '%s';",
									Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getEmployeePayrollDataUsingDB(sql);
	}
	
	public Map<String, Double> getAverageSalaryByGender() {
		String sql = "select gender, avg(basic_pay) as avg_salary from employee_payroll group by gender;";
		Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
		try(Connection conn = this.getConnection())
		{
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next())
			{
				String gender = resultSet.getString("gender");
				double salary = resultSet.getDouble("avg_salary");
				genderToAverageSalaryMap.put(gender, salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genderToAverageSalaryMap;
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
		
	public List<EmployeePayroll> getEmployeePayrollData(String name) 
	{
		List<EmployeePayroll> empList = null;
		if(this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			empList = this.getEmployeePayrollData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return empList;
	}
	
	private void prepareStatementForEmployeeData() 
	{
		try(Connection conn = this.getConnection();)
		{
			String sql = "select * from employee_payroll where name = ?";
			employeePayrollDataStatement = conn.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
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