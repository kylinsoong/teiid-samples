/*
 * ${license}
 */
package org.teiid.resource.adapter;


import javax.resource.ResourceException;
import javax.resource.cci.Connection;

import org.teiid.resource.spi.BasicConnection;

/**
 * Connection to the resource. You must define myTypeConnection interface, that 
 * extends the "javax.resource.cci.Connection"
 */
public class myTypeConnectionImpl extends BasicConnection implements Connection {

    private MyManagedConnectionFactory config;

    public myTypeConnectionImpl(MyManagedConnectionFactory env) {
        this.config = env;
        // todo: connect to your source here
    }
    
    @Override
    public void close() {
    	
    }
}
