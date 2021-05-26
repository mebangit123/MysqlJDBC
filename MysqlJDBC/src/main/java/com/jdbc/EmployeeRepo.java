package com.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
		this.employeePayrollList = new ArrayList<>(empList);
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
	
	public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) {
//			employeePayrollList.add(employeeService.addEmployeeToPayroll(name, gender, salary, startDate));
			employeeService.addEmployeeToPayroll(name, gender, salary, startDate);
	}
	
	public void addEmployeeToPayroll(List<EmployeePayroll> employeeList) {
		employeeList.forEach(employeeData ->
		{
			this.addEmployeeToPayroll(employeeData.getName(), employeeData.getGender(), employeeData.getSalary(), employeeData.getStartDate());
		});
	}
	
	public void addEmployeeToPayroll(EmployeePayroll employeePayroll) {
		employeePayrollList.add(employeePayroll);
	}
	
	public void addEmployeeToPayrollWithThreads(List<EmployeePayroll> employeeList)
	{
		Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
		employeeList.forEach(employeeData ->
		{
			Runnable task = () ->
			{
				employeeAdditionStatus.put(employeeData.hashCode(), false);
				System.out.println("Employee being Added: "+Thread.currentThread().getName());
				this.addEmployeeToPayroll(employeeData.getName(), employeeData.getGender(), employeeData.getSalary(), employeeData.getStartDate());
				employeeAdditionStatus.put(employeeData.hashCode(), true);
				System.out.println("Employee Added: "+Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, employeeData.getName());
			thread.start();
		});
		while(employeeAdditionStatus.containsValue(false))
		{
			try 
			{ 
				Thread.sleep(10);
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public List<EmployeePayroll> readEmployeePayrollData() {
			this.employeePayrollList = employeeService.readData();
		return this.employeePayrollList;
	}
	
	public Map<String, Double> readAverageSalaryByGender() {
		return employeeService.getAverageSalaryByGender();
	}
	
	public void updateEmployeeSalary(String name, double salary) 
	{
		int result = employeeService.updateEmployeeData(name, salary);
		if(result == 0) return;
		EmployeePayroll emp = this.getEmployeePayrollData(name);
		if(emp != null) emp.setSalary(salary);
	}

	public List<EmployeePayroll> retrieveAllEmployeeJoinedWithInGivenDateRange(LocalDate startDate, LocalDate endDate)
	{
		return employeeService.getEmployeeWithInDateRange(startDate, endDate);

	}
	
	private EmployeePayroll getEmployeePayrollData(String name) 
	{
		return this.employeePayrollList.stream()
					.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name))
					.findFirst()
					.orElse(null);
	}
	
	public boolean checkEmployeePayrollInSyncWithDB(String name) 
	{
		List<EmployeePayroll> emp = employeeService.getEmployeePayrollData(name);
		return emp.get(0).equals(getEmployeePayrollData(name));
	}

	public long countEntries() {
		return employeePayrollList.size();
	} 
}