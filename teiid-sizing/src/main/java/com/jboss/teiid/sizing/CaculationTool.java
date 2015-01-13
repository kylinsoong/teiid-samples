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

public class CaculationTool {

	private CaculationEntity caculationEntity ;

	public CaculationTool(CaculationEntity caculationEntity) {
		super();
		this.caculationEntity = caculationEntity;
	}

	public CaculationEntity getCaculationEntity() {
		return caculationEntity;
	}

	public void setCaculationEntity(CaculationEntity caculationEntity) {
		this.caculationEntity = caculationEntity;
	}
	
	/**
	 * #concurrency * (5mb)  * #source queries + 300mb
	 * 
	 * @return heap size in GB
	 */
	public int heapCaculation() {
		int sources = caculationEntity.getSource_count();
		int concurrent = caculationEntity.getQueries_concurrent();
		
		int total_in_mb = concurrent * 5 * sources + 300 ;
		int heap = total_in_mb/1024 + 1;
		
		if(heap < 16) {
			heap = 16 ;
		}
		
		return heap;
	}
	
	public int coreCaculation() {
		
		int sources = caculationEntity.getSource_count();
		int row_count_each = caculationEntity.getRow_count_each();
		int row_size_each = caculationEntity.getRow_size_each();
		int source_latency = caculationEntity.getAvg_time_each();
		int row_count_federdated = caculationEntity.getRow_count_federated();
		int row_size_federdated = caculationEntity.getRow_size_federated();
		int walltime = caculationEntity.getAvg_time_sample();
		boolean isAggregation = caculationEntity.isAggregation();
		int queries_per_sec = caculationEntity.getQueries_per_sec();
		
		long source_processing = getSourceProcessing(row_count_each, row_size_each, sources);
		long initial_latency = getInitialLatency(row_count_each, row_size_each, sources, source_latency);
		long additional_latency = getAdditionalLatency(sources, source_latency);
		long client_processing = getClientProcessing(row_count_federdated, row_size_federdated);
		long engine_time = getEngineTime(isAggregation, sources, row_count_each, row_size_each, row_size_federdated, row_count_federdated, walltime, source_latency);

		long cores = getcorenumbers(source_latency, sources, source_processing, initial_latency, additional_latency, engine_time, client_processing, queries_per_sec);
	
		
		
		return (int) cores;
	}
	
	/*
	 * How much time took to deserialize rows coming back from source.
	 */
	private long getSourceProcessing(int row_count_each, int row_size_each, int sources) {

		double total_byte = row_count_each * row_size_each * sources;
		double source_processing = 0;
		
		if (total_byte > 1000000) {
			double size_in_mb = total_byte / 1000000;
			source_processing = 100 + 4.6 * size_in_mb;
		} else if (total_byte > 100000 && total_byte <= 1000000) {
			double percentage = 80.0 / 900000.0 ;
			source_processing = percentage * (total_byte - 100000) + 20;
		} else if (total_byte > 10000 && total_byte <= 100000) {
			double percentage = 18.0 / 90000.0 ;
			source_processing = percentage * (total_byte - 10000) + 2;
		} else {
			source_processing = 0;
		}
		
		return Math.round(source_processing);
	}
	
	/*
	 * this also variation "source latency", as to first source to return results, where processing starts. We can say this is "low(source_latency)".
	 * 
	 * 'source_latency' is average source latency for each data source, so the formula used to estimate source latency like below method
	 */
	private long getInitialLatency(int row_count_each, int row_size_each, int sources, int source_latency) {
		
		long total_byte = row_count_each * row_size_each;
		double initial_latency = 0 ;
		
		if (sources == 1) {
			initial_latency = source_latency;
		} else if (total_byte > 100000000) {
			initial_latency = source_latency * 0.8;
		} else {
			initial_latency = source_latency * 0.6;
		}
		 
		return Math.round(initial_latency);
	}
	
	/*
	 * Even after the first row of results came(lowest of source_latency), how much more *additional* time spent on waiting for results. Consider a guess of half (0.5) when we parallelize,
	 * 
	 * in serialized situations (XA) this will be 1. So, typically this should be 0.5(high(source_latency) - low(source_latency)) or in XA it should be 1 * sum(source_latency).
	 */
	private long getAdditionalLatency(int sources, int source_latency) {
		
		double additional_latency = 0 ;

        if(sources == 1){
                additional_latency = source_latency * 0.5;
        } else {
                additional_latency = source_latency * 0.4;
        }

        return Math.round(additional_latency);
	}
	
	/*
	 * How much time took for serializing the results and put on the socket.
	 */
	private long getClientProcessing(int row_count_federdated, int row_size_federdated) {
		
		double total_byte = row_count_federdated * row_size_federdated;
		double client_procesing = 0;
		
		if(total_byte > 10000000){
			double size_in_mb = total_byte/1000000 ;
            client_procesing = 210 + 4.6 * size_in_mb;
		} else if (total_byte > 1000000 && total_byte <= 10000000) {
			double percentage = 125.0 / 9000000.0 ;
			client_procesing = percentage * (total_byte - 1000000) + 85;
		} else if (total_byte > 100000 && total_byte <= 1000000) {
			double percentage = 75.0 / 900000.0 ;
			client_procesing = percentage * (total_byte - 100000) + 10;
		} else if (total_byte > 10000 && total_byte <= 100000) {
			double percentage = 5.0 / 90000.0 ;
			client_procesing = percentage * (total_byte - 10000) + 5;
		} else if (total_byte > 1000 && total_byte <= 10000) {
			double percentage = 2.0 / 9000.0 ;
			client_procesing = percentage * (total_byte - 1000) + 3;
		} else if (total_byte > 0 && total_byte <= 1000) {
			double percentage = 2.0 / 1000.0 ;
			client_procesing = percentage * total_byte;
		} else if (total_byte == 0) {
			client_procesing = 0;
		}
		
		return  Math.round(client_procesing);
	}
	
	/*
	 * If there are lot of sorting, aggregations this can be high, if not can be very low as in pass through scenarios.
	 * 
	 * "how much time they took in their sample runs" will get a time for running a sample query, then remove all source and deserialization/Serialization latencies then we roughly have the engine time
	 * 
	 * based on that time, and sorting and aggregation, we can say low, medium or high processing (< 25%, 60%, > 90%) of times
	 */
	private long getEngineTime( boolean isAggregation, 
								int sources,
								int row_count_each, 
								int row_size_each, 
								int row_size_federdated,
								int row_count_federdated, 
								int walltime, 
								int source_latency) {

		long serializing_time = getSourceProcessing(row_count_each, row_size_each, sources);
		long deserializing_time = getClientProcessing(row_count_federdated, row_size_federdated);
		long initial_latency = getInitialLatency(row_count_each, row_size_each, sources, source_latency);
		long additional_latency = getAdditionalLatency(sources, source_latency);
		
		double engine_time = 0;
		
		//sampleruntime should large than latencies + serializing_time + deserializing_time 
		double engine_time_rough = walltime - serializing_time - deserializing_time - initial_latency - additional_latency ;
		
		long total_fer_size = row_size_federdated * row_count_federdated ;
		
		if(engine_time_rough <= 10) {
            engine_time = 10 ;
		} else if (isAggregation && total_fer_size > 100000000) {
			engine_time = engine_time_rough * 0.9;
		} else if (isAggregation) {
			engine_time = engine_time_rough * 0.6;
		} else {
			engine_time = engine_time_rough * 0.3;
		}

		return Math.round(engine_time);
	}
	
	/*
	 * CPU calculation logic & Formula:
	 *   cpu_time = sum(source_processing) + engine_time + client_processing
	 *   wall_time = low(source_latency) + cpu_time + additional_latency
	 *   cpu_utilization_per_query = cpu_time/wall_time
	 *   total_cpu_time_available = cpu_core_count * 2 * 1000ms
	 *   queries/sec = total_cpu_time_available / (threads_used_per_query * cpu_utilization_per_query * cpu_time)
	 *   
	 */
	private long getcorenumbers(int source_latency, 
								int sources,
								long source_processing, 
								long initial_latency,
								long additional_latency, 
								long engine_time, 
								long client_processing,
								int queries_per_sec) {

		double cpu_time = source_processing + engine_time + client_processing;
		double wall_time = cpu_time + initial_latency + additional_latency;
		double cpu_utilization_per_query = cpu_time / wall_time ;
		int threads_used_per_query = sources + 1 ;
		
		double cores = (cpu_time * queries_per_sec * cpu_utilization_per_query * threads_used_per_query)/(1000 * 2);
		
		if (cores < 16) {
			cores = 16;
		}
		
		if(cores > 128) {
			cores = 128 ;
		}

		
		return Math.round(cores);
	}
}
