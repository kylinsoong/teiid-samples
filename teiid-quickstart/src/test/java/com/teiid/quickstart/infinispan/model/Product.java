package com.teiid.quickstart.infinispan.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

	private static final long serialVersionUID = -8474134932528375758L;

	private int id;

	private String name;

	private BigDecimal price;

	public Product() {
	}

	public Product(int id) {
		this.id = id;
	}

	public Product(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = new BigDecimal(price);
	}

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public String toString() {
		return "ProductInfo: (id) " + getId() + " (name) " + getName() + " (price) " + getPrice(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
