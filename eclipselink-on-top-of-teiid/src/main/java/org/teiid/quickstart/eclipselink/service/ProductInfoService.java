package org.teiid.quickstart.eclipselink.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProductInfoService {
	
	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.teiid.quickstart");
		
		EntityManager em = factory.createEntityManager(); 
		
		List list = em.createQuery("SELECT product FROM ProductInfo product ").getResultList();
		
		System.out.println("Query Result:");
		for(Object obj : list) {
			System.out.println("    " + obj);
		}
	
		em.close();
		factory.close();
	}

}
