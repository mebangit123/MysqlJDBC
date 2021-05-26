package com.jdbc;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestAssuredTest {
	//@org.junit.jupiter.api.BeforeEach
	@Before
	public void setup()
	{
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}
	public EmployeePayroll[] getEmployeeList()
	{
		Response response = RestAssured.get("/employees");
		System.out.println("Employee Payroll Entries In JSONServer:\n" + response.asString());	
		EmployeePayroll[] arrayOfEmps = new Gson().fromJson(response.asString(), EmployeePayroll[].class);
		return arrayOfEmps;
	}
	
	private Response addEmployeeToJSONServer(EmployeePayroll employeePayroll) {
		String empJson = new Gson().toJson(employeePayroll);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}
	@Test
	public void givenemployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount()
	{
		EmployeePayroll[] arrayOfEmps = getEmployeeList();
		EmployeeRepo employeeRepo;
		employeeRepo = new EmployeeRepo(Arrays.asList(arrayOfEmps));
		long entries = employeeRepo.countEntries();
		Assert.assertEquals(2, entries);
	}
	@Test
	public void givenNewEmployee_whenAdded_ShouldMatch201ResponseAndCount()
	{
		EmployeePayroll[] arrayOfEmps = getEmployeeList();
		EmployeeRepo employeeRepo;
		employeeRepo = new EmployeeRepo(Arrays.asList(arrayOfEmps));
		EmployeePayroll employeePayroll = null;
		employeePayroll = new EmployeePayroll(0, "Mark Zuckerberg", 300000.0);
		
		Response response = addEmployeeToJSONServer(employeePayroll);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
	
		employeePayroll = new Gson().fromJson(response.asString(), EmployeePayroll.class);
		employeeRepo.addEmployeeToPayroll(employeePayroll);
		long entries = employeeRepo.countEntries();
		Assert.assertEquals(3, entries);
	}
	@Test
	public void givenListOfNewEmployee_WhenAdded_ShouldMatch201ResponseAndCount()
	{
		EmployeePayroll[] arrayOfEmps = getEmployeeList();
		EmployeeRepo employeeRepo;
		employeeRepo = new EmployeeRepo(Arrays.asList(arrayOfEmps));
		
		EmployeePayroll[] arrayOfEmpPayrolls = {
			new EmployeePayroll(0, "Sunder", 600000.00),
			new EmployeePayroll(0, "Anil", 1000000.00),
			new EmployeePayroll(0, "Mukesh", 200000.00)
		};
		for(EmployeePayroll employeePayroll : arrayOfEmpPayrolls)
		{
			Response response = addEmployeeToJSONServer(employeePayroll);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			
			employeePayroll = new Gson().fromJson(response.asString(), EmployeePayroll.class);
			employeeRepo.addEmployeeToPayroll(employeePayroll);
		}
		long entries = employeeRepo.countEntries();
		Assert.assertEquals(6, entries);
	}
}
