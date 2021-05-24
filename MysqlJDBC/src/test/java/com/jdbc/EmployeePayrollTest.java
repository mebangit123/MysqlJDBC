package com.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollTest {
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeeRepo repo = new EmployeeRepo(); 
		List<EmployeePayroll> empData = repo.readEmployeePayrollData();
		Assert.assertEquals(6, empData.size());
	}
}