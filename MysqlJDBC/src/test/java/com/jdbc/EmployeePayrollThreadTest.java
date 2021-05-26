package com.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollThreadTest {

	@Test
	public void given6Employees_ShouldMatchEmployeeEntries() 
	{
		EmployeePayroll[] arrayOfEmps = {
				new EmployeePayroll(0, "Jeff Bezos", "M", 100000.0, LocalDate.now()),
				new EmployeePayroll(0, "Bill Gates", "M", 200000.0, LocalDate.now()),
				new EmployeePayroll(0, "Mark Zuckerberg", "M", 300000.0, LocalDate.now()),
				new EmployeePayroll(0, "Sunder", "M", 600000.0, LocalDate.now()),
				new EmployeePayroll(0, "Mukesh", "M", 100000.0, LocalDate.now()),
				new EmployeePayroll(0, "Anil", "M", 200000.0, LocalDate.now())
		};
		EmployeeRepo employeeRepo = new EmployeeRepo();
		employeeRepo.readEmployeePayrollData();
		Instant start = Instant.now();
		employeeRepo.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without Thread: "+ Duration.between(start, end));
		Assert.assertEquals(6, employeeRepo.countEntries());
	}
	
	@Test
	public void givenEmployees_WhenAddedToDB_ShouldMatchEmployeeEntries() 
	{
		EmployeePayroll[] arrayOfEmps = {
				new EmployeePayroll(0, "Jeff Bezos", "M", 100000.0, LocalDate.now()),
				new EmployeePayroll(0, "Bill Gates", "M", 200000.0, LocalDate.now()),
				new EmployeePayroll(0, "Mark Zuckerberg", "M", 300000.0, LocalDate.now()),
				new EmployeePayroll(0, "Sunder", "M", 600000.0, LocalDate.now()),
				new EmployeePayroll(0, "Mukesh", "M", 100000.0, LocalDate.now()),
				new EmployeePayroll(0, "Anil", "M", 200000.0, LocalDate.now())
		};
		
		EmployeeRepo employeeRepo = new EmployeeRepo();
		employeeRepo.readEmployeePayrollData();
		Instant start = Instant.now();
		employeeRepo.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant end = Instant.now();
		System.out.println("Duration without Thread: "+ Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeeRepo.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread: "+Duration.between(threadStart, threadEnd));
		Assert.assertEquals(6, employeeRepo.countEntries());
	}
}
