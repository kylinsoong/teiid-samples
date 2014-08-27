package com.teiid.quickstart.dqp.internal.datamgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.SessionMetadata;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.client.metadata.ParameterInfo;
import org.teiid.core.types.DataTypeManager;
import org.teiid.dqp.internal.process.DQPWorkContext;
import org.teiid.metadata.BaseColumn.NullType;
import org.teiid.metadata.Column;
import org.teiid.metadata.Column.SearchType;
import org.teiid.metadata.ColumnSet;
import org.teiid.metadata.FunctionMethod;
import org.teiid.metadata.FunctionParameter;
import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Procedure;
import org.teiid.metadata.ProcedureParameter;
import org.teiid.metadata.ProcedureParameter.Type;
import org.teiid.metadata.Schema;
import org.teiid.metadata.Table;
import org.teiid.query.function.FunctionTree;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.function.UDFSource;
import org.teiid.query.mapping.relational.QueryNode;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.sql.lang.SPParameter;

public class RealMetadataFactory {
	
	public static final SystemFunctionManager SFM = new SystemFunctionManager();
	
	private static TransformationMetadata CACHED_BQT = exampleBQT();
	
	public static DQPWorkContext buildWorkContext(TransformationMetadata metadata) {
		return buildWorkContext(metadata, metadata.getVdbMetaData());
	}
	
	public static DQPWorkContext buildWorkContext(QueryMetadataInterface metadata, VDBMetaData vdb) {
		DQPWorkContext workContext = new DQPWorkContext();
		SessionMetadata session = new SessionMetadata();
		workContext.setSession(session);
		session.setVDBName(vdb.getName()); 
		session.setVDBVersion(vdb.getVersion()); 
		session.setSessionId(String.valueOf(1));
		session.setUserName("foo"); //$NON-NLS-1$
		session.setVdb(vdb);
	    workContext.getVDB().addAttchment(QueryMetadataInterface.class, metadata);
	    if (metadata instanceof TransformationMetadata) {
	    	workContext.getVDB().addAttchment(TransformationMetadata.class, (TransformationMetadata)metadata);
	    }
	    DQPWorkContext.setWorkContext(workContext);
		return workContext;
	}
	
	public static TransformationMetadata exampleBQTCached() {
        return CACHED_BQT;
    }
	
	public static TransformationMetadata exampleBQT() {
		return createTransformationMetadata(exampleBQTStore(), "bqt");	
	}
	
	public static TransformationMetadata createTransformationMetadata(MetadataStore metadataStore, String vdbName, FunctionTree... functionModels) {
		CompositeMetadataStore cms = null;
		if (metadataStore instanceof CompositeMetadataStore) {
			cms = (CompositeMetadataStore)metadataStore;
		} else {
			cms = new CompositeMetadataStore(metadataStore);
		}
		return createTransformationMetadata(cms, vdbName, null, functionModels);
	}
	
	public static Schema createPhysicalModel(String name, MetadataStore metadataStore) {
		Schema schema = new Schema();
		schema.setName(name);
		metadataStore.addSchema(schema);
		return schema;	
	}
	
	public static Schema createVirtualModel(String name, MetadataStore metadataStore) {
		Schema schema = new Schema();
		schema.setName(name);
		schema.setPhysical(false);
		metadataStore.addSchema(schema);
		return schema;	
	}
	
	public static Table createPhysicalGroup(String name, Schema model) {
    	return createPhysicalGroup(name, model, false);
    }
	
	public static Table createPhysicalGroup(String name, Schema model, boolean fullyQualify) {
		Table table = new Table();
		table.setName(name);
		model.addTable(table);
		table.setSupportsUpdate(true);
		table.setNameInSource((fullyQualify || name.lastIndexOf(".") == -1)? name : name.substring(name.lastIndexOf(".") + 1));  //$NON-NLS-1$ //$NON-NLS-2$
		table.setTableType(org.teiid.metadata.Table.Type.Table);
		return table;
	}
	
	public static ColumnSet<Procedure> createResultSet(String name, String[] colNames, String[] colTypes) {
    	ColumnSet<Procedure> rs = new ColumnSet<Procedure>();
    	int index = name.indexOf('.');
    	if (index > 0) {
    		name = name.substring(index + 1);
    	}
    	rs.setName(name);
        for(Column column : createElements(rs, colNames, colTypes)) {
        	column.setParent(rs);
        }
        return rs;
    }
	
	public static List<Column> createElements(ColumnSet<?> group, String[] names, String[] types) { 
		return createElementsWithDefaults(group, names, types, new String[names.length]);
	}
	
	public static List<Column> createElementsWithDefaults(ColumnSet<?> group, String[] names, String[] types, String[] defaults) {
		List<Column> elements = new ArrayList<Column>();
		
		for(int i=0; i<names.length; i++) { 
            Column element = createElement(names[i], group, types[i]);
            element.setDefaultValue(defaults[i]);
			elements.add(element);		
		}
		
		return elements;
	}
	
	public static Column createElement(String name, ColumnSet<?> group, String type) { 
        Column column = new Column();
        column.setName(name);
        group.addColumn(column);
        column.setRuntimeType(type);
        if(type.equals(DataTypeManager.DefaultDataTypes.STRING)) {  
            column.setSearchType(SearchType.Searchable);        
        } else if (DataTypeManager.isNonComparable(type)){
        	column.setSearchType(SearchType.Unsearchable);
        } else {        	
        	column.setSearchType(SearchType.All_Except_Like);
        }   
        column.setNullType(NullType.Nullable);
        column.setPosition(group.getColumns().size()); //1 based indexing
        column.setUpdatable(true);
        column.setLength(100);
        column.setNameInSource(name);
        column.setDatatype(SystemMetadata.getInstance().getRuntimeTypeMap().get(type));
        return column; 
    }
	
	public static ProcedureParameter createParameter(String name, int direction, String type) {
        ProcedureParameter param = new ProcedureParameter();
        param.setName(name);
        switch (direction) {
        case SPParameter.IN:
        	param.setType(Type.In);
        	break;
        case SPParameter.INOUT:
        	param.setType(Type.InOut);
        	break;
        case SPParameter.OUT:
        	param.setType(Type.Out);
        	break;
        case SPParameter.RESULT_SET:
        	throw new AssertionError("should not directly create a resultset param"); //$NON-NLS-1$
        case SPParameter.RETURN_VALUE:
        	param.setType(Type.ReturnValue);
        	break;
        }
        param.setRuntimeType(type);
        return param;
    }
	
	public static Procedure createStoredProcedure(String name, Schema model, List<ProcedureParameter> params) {
    	Procedure proc = new Procedure();
    	proc.setName(name);
    	proc.setNameInSource(name);
    	if (params != null) {
    		int index = 1;
	    	for (ProcedureParameter procedureParameter : params) {
				procedureParameter.setProcedure(proc);
				procedureParameter.setPosition(index++);
			}
	    	proc.setParameters(params);
    	}
    	model.addProcedure(proc);
        return proc;    
    }
	
	public static Table createUpdatableVirtualGroup(String name, Schema model, QueryNode plan) {
		return createUpdatableVirtualGroup(name, model, plan, null);
	}
	
	public static Table createUpdatableVirtualGroup(String name, Schema model, QueryNode plan, String updatePlan) {
        Table table = createVirtualGroup(name, model, plan);
        table.setUpdatePlan(updatePlan);
        table.setSupportsUpdate(true);
        return table;
    }
	
	public static Table createVirtualGroup(String name, Schema model, QueryNode plan) {
        Table table = new Table();
        table.setName(name);
        model.addTable(table);
        table.setVirtual(true);
        table.setTableType(org.teiid.metadata.Table.Type.View);
        table.setSelectTransformation(plan.getQuery());
    	table.setBindings(plan.getBindings());
		return table;
	}
	
	public static Procedure createVirtualProcedure(String name, Schema model, List<ProcedureParameter> params, QueryNode queryPlan) {
    	Procedure proc = createStoredProcedure(name, model, params);
    	proc.setVirtual(true);
    	proc.setQueryPlan(queryPlan.getQuery());
        return proc;
    }

	
	public static MetadataStore exampleBQTStore() {
    	MetadataStore metadataStore = new MetadataStore();
    	
        Schema bqt1 = createPhysicalModel("BQT1", metadataStore); //$NON-NLS-1$
        Schema bqt2 = createPhysicalModel("BQT2", metadataStore); //$NON-NLS-1$
        Schema bqt3 = createPhysicalModel("BQT3", metadataStore); //$NON-NLS-1$
        Schema lob = createPhysicalModel("LOB", metadataStore); //$NON-NLS-1$
        Schema vqt = createVirtualModel("VQT", metadataStore); //$NON-NLS-1$
        Schema bvqt = createVirtualModel("BQT_V", metadataStore); //$NON-NLS-1$
        Schema bvqt2 = createVirtualModel("BQT2_V", metadataStore); //$NON-NLS-1$
        
        // Create physical groups
        Table bqt1SmallA = createPhysicalGroup("SmallA", bqt1); //$NON-NLS-1$
        Table bqt1SmallB = createPhysicalGroup("SmallB", bqt1); //$NON-NLS-1$
        Table bqt1MediumA = createPhysicalGroup("MediumA", bqt1); //$NON-NLS-1$
        Table bqt1MediumB = createPhysicalGroup("MediumB", bqt1); //$NON-NLS-1$
        Table bqt2SmallA = createPhysicalGroup("SmallA", bqt2); //$NON-NLS-1$
        Table bqt2SmallB = createPhysicalGroup("SmallB", bqt2); //$NON-NLS-1$
        Table bqt2MediumA = createPhysicalGroup("MediumA", bqt2); //$NON-NLS-1$
        Table bqt2MediumB = createPhysicalGroup("MediumB", bqt2); //$NON-NLS-1$
        Table bqt3SmallA = createPhysicalGroup("SmallA", bqt3); //$NON-NLS-1$
        Table bqt3SmallB = createPhysicalGroup("SmallB", bqt3); //$NON-NLS-1$
        Table bqt3MediumA = createPhysicalGroup("MediumA", bqt3); //$NON-NLS-1$
        Table bqt3MediumB = createPhysicalGroup("MediumB", bqt3); //$NON-NLS-1$
        Table lobTable = createPhysicalGroup("LobTbl", lob); //$NON-NLS-1$
        Table library = createPhysicalGroup("LOB_TESTING_ONE", lob); //$NON-NLS-1$
        
        // add direct query procedure
        ColumnSet<Procedure> nativeProcResults = createResultSet("bqt1.nativers", new String[] {"tuple"}, new String[] { DataTypeManager.DefaultDataTypes.OBJECT}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ProcedureParameter nativeparam = createParameter("param", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.STRING); //$NON-NLS-1$
        ProcedureParameter vardic = createParameter("varag", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.OBJECT); //$NON-NLS-1$
        vardic.setVarArg(true);
        Procedure nativeProc = createStoredProcedure("native", bqt1, Arrays.asList(nativeparam,vardic));  //$NON-NLS-1$ //$NON-NLS-2$
        nativeProc.setResultSet(nativeProcResults);        
        
        createElements( library, new String[] { "CLOB_COLUMN", "BLOB_COLUMN", "KEY_EMULATOR" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        		new String[] { DataTypeManager.DefaultDataTypes.CLOB, DataTypeManager.DefaultDataTypes.BLOB, DataTypeManager.DefaultDataTypes.INTEGER }); 

        // Create virtual groups
        QueryNode vqtn1 = new QueryNode("SELECT * FROM BQT1.SmallA"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg1 = createUpdatableVirtualGroup("SmallA", vqt, vqtn1); //$NON-NLS-1$
        
        QueryNode vqtn2 = new QueryNode("SELECT Concat(stringKey, stringNum) as a12345 FROM BQT1.SmallA"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2 = createUpdatableVirtualGroup("SmallB", vqt, vqtn2); //$NON-NLS-1$        

        // Case 2589
        QueryNode vqtn2589 = new QueryNode("SELECT * FROM BQT1.SmallA WHERE StringNum = '10'"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589 = createVirtualGroup("SmallA_2589", vqt, vqtn2589); //$NON-NLS-1$

        QueryNode vqtn2589a = new QueryNode("SELECT BQT1.SmallA.* FROM BQT1.SmallA INNER JOIN BQT1.SmallB ON SmallA.IntKey = SmallB.IntKey WHERE SmallA.StringNum = '10'"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589a = createVirtualGroup("SmallA_2589a", vqt, vqtn2589a); //$NON-NLS-1$

        QueryNode vqtn2589b = new QueryNode("SELECT BQT1.SmallA.* FROM BQT1.SmallA INNER JOIN BQT1.SmallB ON SmallA.StringKey = SmallB.StringKey WHERE SmallA.StringNum = '10'"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589b = createVirtualGroup("SmallA_2589b", vqt, vqtn2589b); //$NON-NLS-1$

        QueryNode vqtn2589c = new QueryNode("SELECT BQT1.SmallA.* FROM BQT1.SmallA INNER JOIN BQT1.SmallB ON SmallA.StringKey = SmallB.StringKey WHERE concat(SmallA.StringNum, SmallB.StringNum) = '1010'"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589c = createVirtualGroup("SmallA_2589c", vqt, vqtn2589c); //$NON-NLS-1$
        
        QueryNode vqtn2589d = new QueryNode("SELECT BQT1.SmallA.* FROM BQT1.SmallA INNER JOIN BQT1.SmallB ON SmallA.StringKey = SmallB.StringKey WHERE SmallA.StringNum = '10' AND SmallA.IntNum = 10"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589d = createVirtualGroup("SmallA_2589d", vqt, vqtn2589d); //$NON-NLS-1$

        QueryNode vqtn2589f = new QueryNode("SELECT * FROM VQT.SmallA_2589"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589f = createVirtualGroup("SmallA_2589f", vqt, vqtn2589f); //$NON-NLS-1$

        QueryNode vqtn2589g = new QueryNode("SELECT * FROM SmallA_2589b"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589g = createVirtualGroup("SmallA_2589g", vqt, vqtn2589g); //$NON-NLS-1$

        QueryNode vqtn2589h = new QueryNode("SELECT VQT.SmallA_2589.* FROM VQT.SmallA_2589 INNER JOIN BQT1.SmallB ON VQT.SmallA_2589.StringKey = SmallB.StringKey"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589h = createVirtualGroup("SmallA_2589h", vqt, vqtn2589h); //$NON-NLS-1$
        
        QueryNode vqtn2589i = new QueryNode("SELECT BQT1.SmallA.* FROM BQT1.SmallA INNER JOIN BQT1.SmallB ON SmallA.StringKey = SmallB.StringKey WHERE SmallA.StringNum = '10' AND SmallB.StringNum = '10'"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg2589i = createVirtualGroup("SmallA_2589i", vqt, vqtn2589i); //$NON-NLS-1$

        // defect 15355
        QueryNode vqtn15355  = new QueryNode("SELECT convert(IntKey, string) as StringKey, BigIntegerValue FROM BQT1.SmallA UNION SELECT StringKey, (SELECT BigIntegerValue FROM BQT3.SmallA WHERE BQT3.SmallA.BigIntegerValue = BQT2.SmallA.StringNum) FROM BQT2.SmallA"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg15355  = createVirtualGroup("Defect15355", vqt, vqtn15355); //$NON-NLS-1$
        QueryNode vqtn15355a  = new QueryNode("SELECT StringKey, StringNum, BigIntegerValue FROM BQT1.SmallA UNION SELECT StringKey, StringNum, (SELECT BigIntegerValue FROM BQT3.SmallA WHERE BQT3.SmallA.BigIntegerValue = BQT2.SmallA.StringNum) FROM BQT2.SmallA"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg15355a  = createVirtualGroup("Defect15355a", vqt, vqtn15355a); //$NON-NLS-1$
        QueryNode vqtn15355b  = new QueryNode("SELECT convert(IntKey, string) as IntKey, BigIntegerValue FROM BQT1.SmallA UNION SELECT StringKey, (SELECT BigIntegerValue FROM BQT3.SmallA WHERE BQT3.SmallA.BigIntegerValue = BQT2.SmallA.StringNum) FROM BQT2.SmallA"); //$NON-NLS-1$ //$NON-NLS-2$
        Table vqtg15355b  = createVirtualGroup("Defect15355b", vqt, vqtn15355b); //$NON-NLS-1$
        
        QueryNode bvqtn1 = new QueryNode("SELECT a.* FROM BQT1.SMALLA AS a WHERE a.INTNUM = (SELECT MIN(b.INTNUM) FROM BQT1.SMALLA AS b WHERE b.INTKEY = a.IntKey ) OPTION MAKEDEP a"); //$NON-NLS-1$ //$NON-NLS-2$
        Table bvqtg1 = createUpdatableVirtualGroup("BQT_V", bvqt, bvqtn1); //$NON-NLS-1$
        QueryNode bvqt2n1 = new QueryNode("SELECT BQT2.SmallA.* FROM BQT2.SmallA, BQT_V.BQT_V WHERE BQT2.SmallA.IntKey = BQT_V.BQT_V.IntKey"); //$NON-NLS-1$ //$NON-NLS-2$
        Table bvqt2g1 = createUpdatableVirtualGroup("BQT2_V", bvqt2, bvqt2n1); //$NON-NLS-1$

     // Create physical elements
        String[] elemNames = new String[] { 
             "IntKey", "StringKey",  //$NON-NLS-1$ //$NON-NLS-2$
             "IntNum", "StringNum",  //$NON-NLS-1$ //$NON-NLS-2$
             "FloatNum", "LongNum",  //$NON-NLS-1$ //$NON-NLS-2$
             "DoubleNum", "ByteNum",  //$NON-NLS-1$ //$NON-NLS-2$
             "DateValue", "TimeValue",  //$NON-NLS-1$ //$NON-NLS-2$
             "TimestampValue", "BooleanValue",  //$NON-NLS-1$ //$NON-NLS-2$
             "CharValue", "ShortValue",  //$NON-NLS-1$ //$NON-NLS-2$
             "BigIntegerValue", "BigDecimalValue",  //$NON-NLS-1$ //$NON-NLS-2$
             "ObjectValue" }; //$NON-NLS-1$
         String[] elemTypes = new String[] { DataTypeManager.DefaultDataTypes.INTEGER, DataTypeManager.DefaultDataTypes.STRING, 
                             DataTypeManager.DefaultDataTypes.INTEGER, DataTypeManager.DefaultDataTypes.STRING, 
                             DataTypeManager.DefaultDataTypes.FLOAT, DataTypeManager.DefaultDataTypes.LONG, 
                             DataTypeManager.DefaultDataTypes.DOUBLE, DataTypeManager.DefaultDataTypes.BYTE, 
                             DataTypeManager.DefaultDataTypes.DATE, DataTypeManager.DefaultDataTypes.TIME, 
                             DataTypeManager.DefaultDataTypes.TIMESTAMP, DataTypeManager.DefaultDataTypes.BOOLEAN, 
                             DataTypeManager.DefaultDataTypes.CHAR, DataTypeManager.DefaultDataTypes.SHORT, 
                             DataTypeManager.DefaultDataTypes.BIG_INTEGER, DataTypeManager.DefaultDataTypes.BIG_DECIMAL, 
                             DataTypeManager.DefaultDataTypes.OBJECT };
        
        List<Column> bqt1SmallAe = createElements(bqt1SmallA, elemNames, elemTypes);
        bqt1SmallAe.get(1).setNativeType("char"); //$NON-NLS-1$
        createElements(bqt1SmallB, elemNames, elemTypes);
        createElements(bqt1MediumA, elemNames, elemTypes);
        createElements(bqt1MediumB, elemNames, elemTypes);
        createElements(bqt2SmallA, elemNames, elemTypes);
        createElements(bqt2SmallB, elemNames, elemTypes);
        createElements(bqt2MediumA, elemNames, elemTypes);
        createElements(bqt2MediumB, elemNames, elemTypes);                
        createElements(bqt3SmallA, elemNames, elemTypes);
        createElements(bqt3SmallB, elemNames, elemTypes);
        createElements(bqt3MediumA, elemNames, elemTypes);
        createElements(bqt3MediumB, elemNames, elemTypes);
        createElements(lobTable, new String[] {"ClobValue"}, new String[] {DataTypeManager.DefaultDataTypes.CLOB}); //$NON-NLS-1$
        
        // Create virtual elements
        createElements(vqtg1, elemNames, elemTypes);        
        createElements(vqtg2, new String[] {"a12345"}, new String[] {DataTypeManager.DefaultDataTypes.STRING});  //$NON-NLS-1$
        createElements(vqtg15355, new String[] {"StringKey", "BigIntegerValue"}, new String[] {DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.BIG_INTEGER});         //$NON-NLS-1$ //$NON-NLS-2$
        createElements(vqtg15355a, new String[] {"StringKey", "StringNum", "BigIntegerValue"}, new String[] {DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.BIG_INTEGER});         //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        createElements(vqtg15355b, new String[] {"IntKey", "BigIntegerValue"}, new String[] {DataTypeManager.DefaultDataTypes.STRING, DataTypeManager.DefaultDataTypes.BIG_INTEGER});         //$NON-NLS-1$ //$NON-NLS-2$
        createElements(vqtg2589, elemNames, elemTypes);        
        createElements(vqtg2589a, elemNames, elemTypes);        
        createElements(vqtg2589b, elemNames, elemTypes);        
        createElements(vqtg2589c, elemNames, elemTypes);        
        createElements(vqtg2589d, elemNames, elemTypes);        
        createElements(vqtg2589f, elemNames, elemTypes);        
        createElements(vqtg2589g, elemNames, elemTypes);        
        createElements(vqtg2589h, elemNames, elemTypes);        
        createElements(vqtg2589i, elemNames, elemTypes);
        createElements(bvqtg1, elemNames, elemTypes);        
        createElements(bvqt2g1, elemNames, elemTypes);        
        
        ProcedureParameter rsp1 = createParameter("ret", ParameterInfo.RETURN_VALUE, DataTypeManager.DefaultDataTypes.INTEGER);  //$NON-NLS-1$
        ProcedureParameter rsp2 = createParameter("inkey", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        createVirtualProcedure("v_spTest9", bvqt, Arrays.asList(rsp1, rsp2), new QueryNode("ret = call pm4.spTest9(inkey);")); //$NON-NLS-1$ //$NON-NLS-2$

     // Add stored procedure
        Schema pm1 = createPhysicalModel("pm1", metadataStore); //$NON-NLS-1$
        ProcedureParameter rs1p1 = createParameter("intkey", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER);         //$NON-NLS-1$
        ColumnSet<Procedure> rs1 = createResultSet("rs1", new String[] { "IntKey", "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.INTEGER, DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Procedure spTest5 = createStoredProcedure("spTest5", pm1, Arrays.asList(rs1p1)); //$NON-NLS-1$ //$NON-NLS-2$
        spTest5.setResultSet(rs1);

        Schema pm2 = createPhysicalModel("pm2", metadataStore); //$NON-NLS-1$
        ProcedureParameter rs2p1 = createParameter("inkey", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        ProcedureParameter rs2p2 = createParameter("outkey", ParameterInfo.OUT, DataTypeManager.DefaultDataTypes.INTEGER);                 //$NON-NLS-1$
        ColumnSet<Procedure> rs2 = createResultSet("rs2", new String[] { "IntKey", "StringKey"}, new String[] { DataTypeManager.DefaultDataTypes.INTEGER , DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Procedure spTest8 = createStoredProcedure("spTest8", pm2, Arrays.asList(rs2p1, rs2p2)); //$NON-NLS-1$ //$NON-NLS-2$
        spTest8.setResultSet(rs2);
        
        ProcedureParameter rs2p2a = createParameter("outkey", ParameterInfo.OUT, DataTypeManager.DefaultDataTypes.INTEGER);                 //$NON-NLS-1$
        ColumnSet<Procedure> rs2a = createResultSet("rs2", new String[] { "IntKey", "StringKey"}, new String[] { DataTypeManager.DefaultDataTypes.INTEGER , DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Procedure spTest8a = createStoredProcedure("spTest8a", pm2, Arrays.asList(rs2p2a)); //$NON-NLS-1$ //$NON-NLS-2$
        spTest8a.setResultSet(rs2a);
        
        Schema pm4 = createPhysicalModel("pm4", metadataStore); //$NON-NLS-1$
        ProcedureParameter rs4p1 = createParameter("ret", ParameterInfo.RETURN_VALUE, DataTypeManager.DefaultDataTypes.INTEGER);  //$NON-NLS-1$
        ProcedureParameter rs4p2 = createParameter("inkey", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        createStoredProcedure("spTest9", pm4, Arrays.asList(rs4p1, rs4p2)); //$NON-NLS-1$ //$NON-NLS-2$
        
        Schema pm3 = createPhysicalModel("pm3", metadataStore); //$NON-NLS-1$
        ProcedureParameter rs3p1 = createParameter("inkey", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        ProcedureParameter rs3p2 = createParameter("outkey", ParameterInfo.INOUT, DataTypeManager.DefaultDataTypes.INTEGER);                 //$NON-NLS-1$
        ColumnSet<Procedure> rs3 = createResultSet("rs3", new String[] { "IntKey", "StringKey"}, new String[] { DataTypeManager.DefaultDataTypes.INTEGER , DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Procedure spTest11 = createStoredProcedure("spTest11", pm3, Arrays.asList(rs3p1, rs3p2)); //$NON-NLS-1$ //$NON-NLS-2$
        spTest11.setResultSet(rs3);
        
        //add virtual stored procedures 
        Schema mmspTest1 = createVirtualModel("mmspTest1", metadataStore); //$NON-NLS-1$
        ColumnSet<Procedure> vsprs1 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        QueryNode vspqn1 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN DECLARE integer x; LOOP ON (SELECT intkey FROM bqt1.smallA) AS intKeyCursor BEGIN x= intKeyCursor.intkey - 1; END SELECT stringkey FROM bqt1.smalla where intkey=x; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp1 = createVirtualProcedure("MMSP1", mmspTest1, null, vspqn1); //$NON-NLS-1$
        vsp1.setResultSet(vsprs1);

        ColumnSet<Procedure> vsprs2 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        QueryNode vspqn2 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN DECLARE integer x; LOOP ON (SELECT intkey FROM bqt1.smallA) AS intKeyCursor1 BEGIN LOOP ON (SELECT intkey FROM bqt1.smallB) AS intKeyCursor2 BEGIN x= intKeyCursor1.intkey - intKeyCursor2.intkey; END END SELECT stringkey FROM bqt1.smalla where intkey=x; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp2 = createVirtualProcedure("MMSP2", mmspTest1, null, vspqn2); //$NON-NLS-1$
        vsp2.setResultSet(vsprs2);

        ColumnSet<Procedure> vsprs3 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        QueryNode vspqn3 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN DECLARE integer x; LOOP ON (SELECT intkey FROM bqt1.smallA) AS intKeyCursor BEGIN x= intKeyCursor.intkey - 1; if(x = 25) BEGIN BREAK; END ELSE BEGIN CONTINUE; END END SELECT stringkey FROM bqt1.smalla where intkey=x; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp3 = createVirtualProcedure("MMSP3", mmspTest1, null, vspqn3); //$NON-NLS-1$
        vsp3.setResultSet(vsprs3);

        ColumnSet<Procedure> vsprs4 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        QueryNode vspqn4 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN DECLARE integer x; x=0; WHILE(x < 50) BEGIN x= x + 1; if(x = 25) BEGIN BREAK; END ELSE BEGIN CONTINUE; END END SELECT stringkey FROM bqt1.smalla where intkey=x; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp4 = createVirtualProcedure("MMSP4", mmspTest1, null, vspqn4); //$NON-NLS-1$
        vsp4.setResultSet(vsprs4);
        
        ColumnSet<Procedure> vsprs5 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        ProcedureParameter vsp5p1 = createParameter("param1", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.STRING); //$NON-NLS-1$
        QueryNode vspqn5 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN SELECT 0; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp5 = createVirtualProcedure("MMSP5", mmspTest1, Arrays.asList(vsp5p1), vspqn5); //$NON-NLS-1$
        vsp5.setResultSet(vsprs5);

        ColumnSet<Procedure> vsprs6 = createResultSet("mmspTest1.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        ProcedureParameter vsp6p1 = createParameter("p1", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.STRING); //$NON-NLS-1$
        QueryNode vspqn6 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN SELECT p1 as StringKey; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp6 = createVirtualProcedure("MMSP6", mmspTest1, Arrays.asList(vsp6p1), vspqn6); //$NON-NLS-1$
        vsp6.setResultSet(vsprs6);
        
        createStoredProcedure("spRetOut", pm4, Arrays.asList(createParameter("ret", ParameterInfo.RETURN_VALUE, DataTypeManager.DefaultDataTypes.INTEGER),
        		createParameter("x", ParameterInfo.OUT, DataTypeManager.DefaultDataTypes.INTEGER))); //$NON-NLS-1$ //$NON-NLS-2$
        
        ColumnSet<Procedure> vsprs7 = createResultSet("TEIIDSP7.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        ProcedureParameter vsp7p1 = createParameter("p1", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        QueryNode vspqn7 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN declare integer x; x = exec spTest9(p1); declare integer y; exec spTest11(inkey=>x, outkey=>y) without return; select convert(x, string) || y; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp7 = createVirtualProcedure("TEIIDSP7", mmspTest1, Arrays.asList(vsp7p1), vspqn7); //$NON-NLS-1$
        vsp7.setResultSet(vsprs7);
        
        ProcedureParameter vsp8p1 = createParameter("r", ParameterInfo.RETURN_VALUE, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        ProcedureParameter vsp8p2 = createParameter("p1", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        QueryNode vspqn8 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN r = p1; END"); //$NON-NLS-1$ //$NON-NLS-2$
        createVirtualProcedure("TEIIDSP8", mmspTest1, Arrays.asList(vsp8p1, vsp8p2), vspqn8); //$NON-NLS-1$

        ColumnSet<Procedure> vsprs9 = createResultSet("TEIIDSP9.vsprs1", new String[] { "StringKey" }, new String[] { DataTypeManager.DefaultDataTypes.STRING }); //$NON-NLS-1$ //$NON-NLS-2$
        ProcedureParameter vsp9p1 = createParameter("r", ParameterInfo.RETURN_VALUE, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        vsp9p1.setNullType(NullType.No_Nulls);
        ProcedureParameter vsp9p2 = createParameter("p1", ParameterInfo.IN, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        ProcedureParameter vsp9p3 = createParameter("p2", ParameterInfo.OUT, DataTypeManager.DefaultDataTypes.INTEGER); //$NON-NLS-1$
        QueryNode vspqn9 = new QueryNode("CREATE VIRTUAL PROCEDURE BEGIN if (p1 = 1) begin\n r = 1; end\n p2 = 10; select 'hello'; END"); //$NON-NLS-1$ //$NON-NLS-2$
        Procedure vsp9 = createVirtualProcedure("TEIIDSP9", mmspTest1, Arrays.asList(vsp9p1, vsp9p2, vsp9p3), vspqn9); //$NON-NLS-1$
        vsp9.setResultSet(vsprs9);
        
        createStoredProcedure("sp_noreturn", pm4, Collections.EMPTY_LIST); //$NON-NLS-1$ //$NON-NLS-2$
        
        
        // this is for the source added function
        bqt1.addFunction(new FunctionMethod("reverse", "reverse", "misc", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
                new FunctionParameter[] {new FunctionParameter("columnName", DataTypeManager.DefaultDataTypes.STRING, "")}, //$NON-NLS-1$ //$NON-NLS-2$
                new FunctionParameter("result", DataTypeManager.DefaultDataTypes.STRING, "") ) ); //$NON-NLS-1$ //$NON-NLS-2$    		
        
    	 return metadataStore;
    }

	
	public static TransformationMetadata createTransformationMetadata(CompositeMetadataStore store, String vdbName, Properties vdbProperties, FunctionTree... functionModels) {
    	VDBMetaData vdbMetaData = new VDBMetaData();
    	vdbMetaData.setName(vdbName); //$NON-NLS-1$
    	vdbMetaData.setVersion(1);
    	if (vdbProperties != null) {
    		vdbMetaData.setProperties(vdbProperties);
    	}
    	List<FunctionTree> udfs = new ArrayList<FunctionTree>();
    	udfs.addAll(Arrays.asList(functionModels));
    	for (Schema schema : store.getSchemas().values()) {
			vdbMetaData.addModel(RealMetadataFactory.createModel(schema.getName(), schema.isPhysical()));
			if (!schema.getFunctions().isEmpty()) {
				udfs.add(new FunctionTree(schema.getName(), new UDFSource(schema.getFunctions().values()), true));
			}
		}
    	TransformationMetadata metadata = new TransformationMetadata(vdbMetaData, store, null, SFM.getSystemFunctions(), udfs);
    	vdbMetaData.addAttchment(TransformationMetadata.class, metadata);
    	vdbMetaData.addAttchment(QueryMetadataInterface.class, metadata);
    	return metadata;
	}
	
	public static ModelMetaData createModel(String name, boolean source) {
		ModelMetaData model = new ModelMetaData();
		model.setName(name);
		if (source) {
			model.setModelType(Model.Type.PHYSICAL);
		}
		else {
			model.setModelType(Model.Type.VIRTUAL);
		}
		model.setVisible(true);
		model.setSupportsMultiSourceBindings(false);
		model.addSourceMapping(name, name, null);
		
		return model;
	}
	
	public static VDBMetaData exampleBQTVDB() {
		VDBMetaData vdb = new VDBMetaData();
		vdb.setName("example1");
		vdb.setVersion(1);
		vdb.addModel(RealMetadataFactory.createModel("BQT1", true));
		vdb.addModel(RealMetadataFactory.createModel("BQT2", true));
		vdb.addModel(RealMetadataFactory.createModel("BQT3", true));
		vdb.addModel(RealMetadataFactory.createModel("LOB", true));
		vdb.addModel(RealMetadataFactory.createModel("VQT", false));
		vdb.addModel(RealMetadataFactory.createModel("pm1", true));
		vdb.addModel(RealMetadataFactory.createModel("pm2", true));
		vdb.addModel(RealMetadataFactory.createModel("pm3", true));
		vdb.addModel(RealMetadataFactory.createModel("pm4", true));
		
		return vdb;
	}
}
