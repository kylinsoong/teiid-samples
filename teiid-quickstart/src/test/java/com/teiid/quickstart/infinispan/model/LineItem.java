package com.teiid.quickstart.infinispan.model;

import java.io.Serializable;

public class LineItem implements Serializable {

	private static final long serialVersionUID = 775854192010074314L;

	private int id;

	private Product product;

	private int quantity;

	public LineItem() {
	}

	public LineItem(int id) {
		this.id = id;
	}

	public LineItem(int id, Product product, int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String toString() {
		return "Product (id) " + getId() + " (product) " + getProduct() + " (quantity) " + getQuantity(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
