package com.dabaicong.jpa.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.dabaicong.jpa.entry.Employee;

public class EmployeeService {

	private EntityManager entityManager ;
	
	public EmployeeService(EntityManager entityManager ){
		this.entityManager = entityManager ;
	}
	
	public Employee create(int id,String name ,long sal){
		Employee employee = new  Employee(id, name, sal);
		entityManager.persist(employee);
		return employee;
	}
	
	public void remove(int id){
		Employee employee = entityManager.find(Employee.class, id);
		if(employee!= null){
			entityManager.remove(employee);
		}
	}
	public Employee addSal (int id,long addsal){
		Employee employee = entityManager.find(Employee.class, id);
		if(employee!= null){
			employee.setSalary(employee.getSalary()+addsal);
		}
		return employee;
	}
	public List<Employee> findAll(){
		List<Employee> list = new ArrayList<Employee>(); 
		TypedQuery<Employee> query = entityManager.createQuery("select e from employee e",Employee.class);
		list = query.getResultList();
		return list;
	}
	public Employee findById(int id){
		Employee employee = entityManager.find(Employee.class, id);
		if(employee!= null){
			return employee;
		}
		return null ;
	}
}
