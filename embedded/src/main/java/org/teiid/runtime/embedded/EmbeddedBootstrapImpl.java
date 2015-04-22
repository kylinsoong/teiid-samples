package org.teiid.runtime.embedded;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.github.fungal.api.Kernel;
import com.github.fungal.api.KernelFactory;
import com.github.fungal.api.classloading.ClassLoaderFactory;
import com.github.fungal.api.configuration.DeploymentOrder;
import com.github.fungal.api.configuration.KernelConfiguration;


public class EmbeddedBootstrapImpl implements EmbeddedBootstrap {
	
	
	/** Fungal Kernel */
	private Kernel kernel;
	
	private boolean started;
	
	EmbeddedBootstrapImpl() {
		this.started = false;
		SecurityActions.setSystemProperty("java.naming.factory.initial", "org.jnp.interfaces.LocalOnlyContextFactory");
		SecurityActions.setSystemProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
	}

	@Override
	public void startup() throws Throwable {

		if (started){
			throw new IllegalStateException("Container already started");
		}
		
		List<String> order = new ArrayList<String>(4);
	    order.add(".xml");
	    order.add(".rar");
	    order.add("-ra.xml");      
	    order.add("-ds.xml");
	    
	    boolean management =  Boolean.valueOf(SecurityActions.getSystemProperty("teiid.embedded.management", "false"));
		
	    KernelConfiguration kernelConfiguration = new KernelConfiguration();
	    kernelConfiguration = kernelConfiguration.name("iron.jacamar");
	    kernelConfiguration = kernelConfiguration.home(null);
	    kernelConfiguration = kernelConfiguration.classLoader(ClassLoaderFactory.TYPE_PARENT_FIRST);
	    kernelConfiguration = kernelConfiguration.management(management);
	    
	    if (management){
	    	kernelConfiguration = kernelConfiguration.usePlatformMBeanServer(true);
	    }
	    
	    kernelConfiguration = kernelConfiguration.parallelDeploy(false);
	    kernelConfiguration = kernelConfiguration.remoteAccess(false);
	    kernelConfiguration = kernelConfiguration.hotDeployment(false);
	    kernelConfiguration = kernelConfiguration.deploymentOrder(new DeploymentOrder(order));
	    
	    kernel = KernelFactory.create(kernelConfiguration);
	    kernel.startup();
	    
	    deploy(SecurityActions.getClassLoader(EmbeddedBootstrapImpl.class), "naming.xml");
	    deploy(SecurityActions.getClassLoader(EmbeddedBootstrapImpl.class), "transaction.xml");
	    
	    started = true;
	}
	
	private void deploy(ClassLoader cl, String name) throws Throwable {
		
		if (cl == null){
			throw new IllegalArgumentException("ClassLoader is null");
		}

	    if (name == null){
	    	throw new IllegalArgumentException("Name is null");
	    }
	    
	    URL url = cl.getResource(name);
	    
	    kernel.getMainDeployer().deploy(url);
	}
	
	private void undeploy(ClassLoader cl, String name) throws Throwable {
		
		if (cl == null){
			throw new IllegalArgumentException("ClassLoader is null");
		}

	    if (name == null){
	    	throw new IllegalArgumentException("Name is null");
	    }
	    
	    URL url = cl.getResource(name);
	    
	    kernel.getMainDeployer().undeploy(url);
	}

	@Override
	public void shutdown() throws Throwable {
		
		if (!started){
			throw new IllegalStateException("Container not started");
		}
		
		undeploy(SecurityActions.getClassLoader(EmbeddedBootstrapImpl.class), "transaction.xml");
		undeploy(SecurityActions.getClassLoader(EmbeddedBootstrapImpl.class), "naming.xml");
	}

	@Override
	public void deploy(URL url) throws Throwable {
		
		if (url == null){
			throw new IllegalArgumentException("Url is null");
		}
		
		if (!started){
			throw new IllegalStateException("Container not started");
		}
		
		kernel.getMainDeployer().deploy(url);
	}

}
