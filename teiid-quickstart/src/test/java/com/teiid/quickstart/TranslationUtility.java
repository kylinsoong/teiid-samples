package com.teiid.quickstart;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.teiid.cdk.CommandBuilder;
import org.teiid.dqp.internal.datamgr.RuntimeMetadataImpl;
import org.teiid.language.Command;
import org.teiid.metadata.FunctionMethod;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.query.function.FunctionLibrary;
import org.teiid.query.function.FunctionTree;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.function.UDFSource;
import org.teiid.query.metadata.BasicQueryMetadataWrapper;
import org.teiid.query.metadata.QueryMetadataInterface;

public class TranslationUtility {
	
	private QueryMetadataInterface metadata;
	private FunctionLibrary functionLibrary;
	
	private List<FunctionTree> functions = new ArrayList<FunctionTree>();
	
	public TranslationUtility(String vdbFile) {
		initWrapper(VDBMetadataFactory.getVDBMetadata(vdbFile));
	}
	
	public TranslationUtility(String vdbName, URL url) {
		try {
			initWrapper(VDBMetadataFactory.getVDBMetadata(vdbName, url, null));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public TranslationUtility(QueryMetadataInterface metadata) {
		initWrapper(metadata);
	}
	
	private void initWrapper(QueryMetadataInterface acutalMetadata) {
		this.functionLibrary = acutalMetadata.getFunctionLibrary();
		this.functions.addAll(Arrays.asList(this.functionLibrary.getUserFunctions()));
		this.metadata = new BasicQueryMetadataWrapper(acutalMetadata){

			public FunctionLibrary getFunctionLibrary() {
				return functionLibrary;
			}};
	}
	
	public Command parseCommand(String sql, boolean generateAliases, boolean supportsGroupAliases) {
		CommandBuilder commandBuilder = new CommandBuilder(metadata);
		return commandBuilder.getCommand(sql, generateAliases, supportsGroupAliases);
	}
	
	public Command parseCommand(String sql) {
		CommandBuilder commandBuilder = new CommandBuilder(metadata);
		return commandBuilder.getCommand(sql);
	}
	
	 public RuntimeMetadata createRuntimeMetadata() {
		 return new RuntimeMetadataImpl(metadata);
	}
	 
	 public void addUDF(String schema, Collection<FunctionMethod> methods){
		if (methods == null || methods.isEmpty()) {
			return;
		}
		this.functions.add(new FunctionTree(schema, new UDFSource(methods)));
		SystemFunctionManager sfm = new SystemFunctionManager();
		functionLibrary = new FunctionLibrary(sfm.getSystemFunctions(), this.functions.toArray(new FunctionTree[this.functions.size()]));
	 }

}
