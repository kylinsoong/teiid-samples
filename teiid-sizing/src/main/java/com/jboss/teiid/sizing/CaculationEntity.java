/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package com.jboss.teiid.sizing;

public class CaculationEntity {
	
	private int source_count;
	
	private int queries_concurrent;
	
	private int queries_per_sec;
	
	private int row_count_each;
	
	private int row_size_each;
	
	private int avg_time_each;
	
	private int row_count_federated;
	
	private int row_size_federated;
	
	private int avg_time_sample;
	
	private boolean isAggregation;
	
	public CaculationEntity() {
		
	}

	public CaculationEntity(int source_count, int queries_concurrent,
			int queries_per_sec, int row_count_each, int row_size_each,
			int avg_time_each, int row_count_federated, int row_size_federated,
			int avg_time_sample, boolean isAggregation) {
		super();
		this.source_count = source_count;
		this.queries_concurrent = queries_concurrent;
		this.queries_per_sec = queries_per_sec;
		this.row_count_each = row_count_each;
		this.row_size_each = row_size_each;
		this.avg_time_each = avg_time_each;
		this.row_count_federated = row_count_federated;
		this.row_size_federated = row_size_federated;
		this.avg_time_sample = avg_time_sample;
		this.isAggregation = isAggregation;
	}

	public int getSource_count() {
		return source_count;
	}

	public void setSource_count(int source_count) {
		this.source_count = source_count;
	}

	public int getQueries_concurrent() {
		return queries_concurrent;
	}

	public void setQueries_concurrent(int queries_concurrent) {
		this.queries_concurrent = queries_concurrent;
	}

	public int getQueries_per_sec() {
		return queries_per_sec;
	}

	public void setQueries_per_sec(int queries_per_sec) {
		this.queries_per_sec = queries_per_sec;
	}

	public int getRow_count_each() {
		return row_count_each;
	}

	public void setRow_count_each(int row_count_each) {
		this.row_count_each = row_count_each;
	}

	public int getRow_size_each() {
		return row_size_each;
	}

	public void setRow_size_each(int row_size_each) {
		this.row_size_each = row_size_each;
	}

	public int getAvg_time_each() {
		return avg_time_each;
	}

	public void setAvg_time_each(int avg_time_each) {
		this.avg_time_each = avg_time_each;
	}

	public int getRow_count_federated() {
		return row_count_federated;
	}

	public void setRow_count_federated(int row_count_federated) {
		this.row_count_federated = row_count_federated;
	}

	public int getRow_size_federated() {
		return row_size_federated;
	}

	public void setRow_size_federated(int row_size_federated) {
		this.row_size_federated = row_size_federated;
	}

	public int getAvg_time_sample() {
		return avg_time_sample;
	}

	public void setAvg_time_sample(int avg_time_sample) {
		this.avg_time_sample = avg_time_sample;
	}

	public boolean isAggregation() {
		return isAggregation;
	}

	public void setAggregation(boolean isAggregation) {
		this.isAggregation = isAggregation;
	}

}
