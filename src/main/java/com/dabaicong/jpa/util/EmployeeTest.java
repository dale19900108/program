package com.dabaicong.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.dabaicong.jpa.entry.Employee;
import com.dabaicong.jpa.service.EmployeeService;

public class EmployeeTest {
	
	
	
	public static void main(String[] args) throws InterruptedException {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("EmployeeService");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EmployeeService serivce = new EmployeeService(entityManager);
		Employee employee = null ;
		
		/**
		 * test for insert 
		 */
		entityManager.getTransaction().begin();
		Employee employee1 = serivce.create(102, "dale", 10000);
		
		System.out.println("插入一条记录id = "+employee1.getId()+" ,name = "+employee1.getName());
		
//		
		entityManager.getTransaction().commit();	
		entityManager.close();
		entityManagerFactory.close();
		
	}
}
