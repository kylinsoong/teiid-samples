SET NAMESPACE 'http://www.teiid.org/translator/hbase/2014' AS teiid_hbase;
            
CREATE FOREIGN TABLE Customer (
    PK string OPTIONS ("teiid_hbase:CELL" 'ROW_ID'),
    city string OPTIONS ("teiid_hbase:CELL" 'customer.city'),
    name string OPTIONS ("teiid_hbase:CELL" 'customer.name'),
    amount string OPTIONS ("teiid_hbase:CELL" 'sales.amount'),
    product string OPTIONS ("teiid_hbase:CELL" 'sales.product'),
    CONSTRAINT PK0 PRIMARY KEY(PK)
) OPTIONS("teiid_hbase:TABLE" 'Customer', "UPDATABLE" 'TRUE');