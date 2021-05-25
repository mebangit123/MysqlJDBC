package com.jdbc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollTest {
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeeRepo repo = new EmployeeRepo(); 
		List<EmployeePayroll> empData = repo.readEmployeePayrollData();
		Assert.assertEquals(6, empData.size());
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdate_ShouldSyncWithDB() 
	{
		EmployeeRepo repo = new EmployeeRepo();
		List<EmployeePayroll> employeeList = repo.readEmployeePayrollData();
		repo.updateEmployeeSalary("Terisa", 3500000.00);
		boolean result = repo.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() 
	{
		EmployeeRepo repo = new EmployeeRepo();
		repo.readEmployeePayrollData();
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayroll> employeePayrollData = repo.retrieveAllEmployeeJoinedWithInGivenDateRange(startDate, endDate);
		Assert.assertEquals(3, employeePayrollData.size());
	}
	
	@Test
	public void givenPayrollData_WhenAverageSalaryRetrieved_ShouldReturnProperValue()
	{
		EmployeeRepo repo = new EmployeeRepo();
		repo.readEmployeePayrollData();
		Map<String, Double> averageSalaryByGender = repo.readAverageSalaryByGender();
		Assert.assertTrue(averageSalaryByGender.get("M").equals(1500000.00) &&
						  averageSalaryByGender.get("F").equals(3500000.00));
	}
	
	@Test
	public void givenNewEmployee_WhenAdded_ShouldSyncWithDB()
	{
			
		EmployeeRepo repo = new EmployeeRepo();
		repo.readEmployeePayrollData();
		repo.addEmployeeToPayroll("Mark", "M", 5000000.00, LocalDate.now());
		boolean result = repo.checkEmployeePayrollInSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
}