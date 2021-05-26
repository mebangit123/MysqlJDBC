package com.jdbc;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayroll {
	private int id;
	private String name;
	private double salary;
	private String gender;
	private LocalDate startDate;
	
	public EmployeePayroll(int id, String name, double salary) {
		this.name = name;
		this.salary = salary;
		this.id = id;
	}
	
	public EmployeePayroll(int id, String name, double salary, LocalDate startDate) {
		this(id, name, salary);
		this.setStartDate(startDate);
	}
	
	public EmployeePayroll(int id, String name, String gender, double salary, LocalDate startDate) {
		this(id, name, salary, startDate);
		this.gender = gender;
	}
	
	public EmployeePayroll() {}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	@Override
	public String toString() {
		return "EmployeePayroll [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EmployeePayroll)) {
			return false;
		}
		EmployeePayroll other = (EmployeePayroll) obj;
		return id == other.id && Objects.equals(name, other.name)
				&& Double.doubleToLongBits(salary) == Double.doubleToLongBits(other.salary);
	}
}
