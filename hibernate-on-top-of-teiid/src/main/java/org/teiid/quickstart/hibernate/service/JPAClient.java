package org.teiid.quickstart.hibernate.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAClient {

	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.teiid.quickstart");
		
		EntityManager em = factory.createEntityManager(); 
		
		System.out.println(em);
	
		em.close();
		factory.close();

	}

}
