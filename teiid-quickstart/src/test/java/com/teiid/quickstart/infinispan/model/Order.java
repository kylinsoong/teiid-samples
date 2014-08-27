package com.teiid.quickstart.infinispan.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = 4677490141470160833L;

	private static DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");

	private int id;

	private Date orderDate;

	private String person;

	private List<LineItem> lineItems;

	public Order() {
	}

	public Order(int id) {
		this.id = id;
	}

	public Order(int id, Date date, String person, List<LineItem> items) {
		this.id = id;
		this.orderDate = date;
		this.person = person;
		this.lineItems = items;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getOrderDateString() {
		return dateFormat.format(orderDate);
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public int getNumberOfItems() {
		return getLineItems().size();
	}
	
	public String toString() {
		return "OrderID: (id) " + getId() + " (Person) " + getPerson() + " (OrderDate) " + getOrderDate() + " (#LineItems) " + (getLineItems() != null ? getLineItems().size() : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

}
