package org.teiid.quickstart.eclipselink.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.teiid.quickstart.eclipselink.model.ProductInfo;

public class ProductInfoService {
	
	public static void main(String[] args) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.teiid.quickstart");
		
		EntityManager em = factory.createEntityManager(); 
		
//		insert(em);

		List list = em.createQuery("SELECT product FROM ProductInfo product ").getResultList();		
		
//		List list = em.createNativeQuery("SELECT COUNT(*) FROM ProductInfo").getResultList();
		
		System.out.println("Query Result:");
		for(Object obj : list) {
			System.out.println("    " + obj);
		}
		
		em.close();
		factory.close();
	}

	private static void insert(EntityManager em) {
		em.getTransaction().begin();
		for(int i = 0 ; i < 10 ; i ++) {
			em.persist(new ProductInfo(3000 + i, "KS", "ksoong.org"));
		}
		em.getTransaction().commit();
	}

}
