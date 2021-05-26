package com.jdbc;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

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
	@Test
	public void givenemployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount()
	{
		EmployeePayroll[] arrayOfEmps = getEmployeeList();
		EmployeeRepo employeeRepo;
		employeeRepo = new EmployeeRepo(Arrays.asList(arrayOfEmps));
		long entries = employeeRepo.countEntries();
		Assert.assertEquals(2, entries);
	}
}
