package org.jboss.teiid.dashboard.hibernate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateInitializerTest {
	
	static final String BaseCfgDirectory = "/home/kylin/work/dashbuilder/dashbuilder-6.1.0.Final-wildfly-8/WEB-INF/etc";

	static final String DB_MYSQL = "mysql";
	 
	public static void main(String[] args) {
		
		// Configure Hibernate using the hibernate.cfg.xml.
        String hbnCfgPath = BaseCfgDirectory + File.separator + "hibernate.cfg.xml";
        
        Configuration hbmConfig = new Configuration().configure(new File(hbnCfgPath));
        
        hbmConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        
      // Initialize the Hibernate session factory.
        ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(hbmConfig.getProperties());

        
        System.out.println(hbmConfig);

	}
	
	protected void loadHibernateDescriptors(Configuration hbmConfig) throws IOException {
        Set<File> jars = Application.lookup().getJarFiles();
        for (File jar : jars) {
            ZipFile zf = new ZipFile(jar);
            for (Enumeration en = zf.entries(); en.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) en.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith("hbm.xml") && !entry.isDirectory()) {
                    InputStream is = zf.getInputStream(entry);
                    String xml = readXMLForFile(entryName, is);
                    xml = processXMLContents(xml);
                    hbmConfig.addXML(xml);
                }
            }
        }
    }
	
	protected String readXMLForFile(String fileName, InputStream is) throws IOException {
        try {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer fileContents = new StringBuffer();
                String lineRead;
                while ((lineRead = reader.readLine()) != null) {
                    fileContents.append(lineRead);
                }
                return fileContents.toString();
            } finally {
                if (reader != null) reader.close();
            }
        } catch (IOException e) {
            return null;
        }
    }
	
	protected String processXMLContents(String fileContent) {
        if (ArrayUtils.contains(nativeToSequenceReplaceableDialects, hbmConfig.getProperty("hibernate.dialect"))) {
            String line = "class=\"sequence\" />";
            fileContent = StringUtils.replace(fileContent, "class=\"native\"/>", line);
            fileContent = StringUtils.replace(fileContent, "class=\"native\" />", line);
        }
        if (ArrayUtils.contains(nativeToHiloReplaceableDialects, hbmConfig.getProperty("hibernate.dialect"))) {
            String line = "class=\"hilo\"><param name=\"table\">hibernate_unique_key</param><param name=\"column\">next_hi</param><param name=\"max_lo\">0</param></generator>";
            fileContent = StringUtils.replace(fileContent, "class=\"native\"/>", line);
            fileContent = StringUtils.replace(fileContent, "class=\"native\" />", line);
        }
        return fileContent;
    }

}
