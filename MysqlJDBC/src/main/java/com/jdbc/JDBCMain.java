package com.jdbc;

public class JDBCMain {
	public static void main(String[] args) {
		EmployeeRepo repo = new EmployeeRepo();
		repo.checkDataBaseConnection();
	}
}
