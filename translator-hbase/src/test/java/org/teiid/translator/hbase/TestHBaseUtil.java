package org.teiid.translator.hbase;

import java.io.FileReader;

import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.core.util.UnitTestUtil;
import org.teiid.metadata.MetadataFactory;
import org.teiid.query.metadata.MetadataValidator;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.unittest.RealMetadataFactory;
import org.teiid.query.validator.ValidatorReport;

public class TestHBaseUtil {
	
	public static QueryMetadataInterface queryMetadataInterface() {
		
		try {
			ModelMetaData mmd = new ModelMetaData();
			mmd.setName("HBaseModel");
			
			MetadataFactory mf = new MetadataFactory("hbase", 1, SystemMetadata.getInstance().getRuntimeTypeMap(), mmd);
			mf.setParser(new QueryParser());
			mf.parse(new FileReader(UnitTestUtil.getTestDataFile("customer.ddl")));
			
			TransformationMetadata tm = RealMetadataFactory.createTransformationMetadata(mf.asMetadataStore(), "x");
			ValidatorReport report = new MetadataValidator().validate(tm.getVdbMetaData(), tm.getMetadataStore());
			if (report.hasItems()) {
				throw new RuntimeException(report.getFailureMessage());
			}
			return tm;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
