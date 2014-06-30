# What's this

This is workshop for quick learning JBoss Data Virtualization. [Step by Step document entry](https://github.com/kylinsoong/DVWorkshop/tree/master/docs).

# Procedure

| *Steps* | *Notes* |
|-------- |:-------:|
|Creating a Teiid Project |`Financials` contain sub folder DataSources, EnterpriseDataLayer, Schemas, VirtualBaseLayer and WebServices. |
|Creating a Source Model  |create model for accessing physical data from a source  |
|Importing Metadata from the Product Database |`Products.xmi` be created under DataSources foler |
|Preview Data via the Teiid Server |Preview `productdata` data |
|Import Metadata from the US_Customers  |`US_Customers.xmi` be created under DataSource folder |
|Import Metadata from the EU_Customers  |`EU_Customers.xmi` be created under DataSource folder |
|Import Metadata from the APAC_Customers  |`APAC_Customers.xmi` be created under DataSource folder |
|Import Metadata from the BrokerInfo |`BrokerInfo.xmi` be created under DataSource folder |
|Import Metadata from a flat file |`MarketsData.xmi` under DataSources folder, and `MarketsData_VBL.xmi` under VirtualBaseLayer folder |
|Preview Data via the Teiid Server |Preview `MarketsDataView` data  |
|Create a virtual base layer: US_Customers_VBL |`US_Customers_VBL.xmi` be created under VirtualBaseLayer folder |
|Create a virtual base layer: EU_Customers_VBL |`EU_Customers_VBL.xmi` be created under VirtualBaseLayer folder |
|Create a virtual base layer: APAC_Customers_VBL |`APAC_Customers_VBL.xmi` be created under VirtualBaseLayer folder |
|Create a virtual base layer: Products_VBL |`Products_VBL.xmi` be created under VirtualBaseLayer folder |
|Create a virtual base layer: BrokerInfo_VBL |`BrokerInfo_VBL.xmi` be created under VirtualBaseLayer folder |
|Import the Data Dictionary Schema |Import `DataDictionary.xsd` to Schemas folder |
|Examine the Data Dictionary |N/A  |
|Create the EU_Customers_DDC Enterprise Data Layer |`EU_Customers_DDC.xmi` generated under EnterpriseDataLyer |
|Create the US_Customers_DDC Enterprise Data Layer |`US_Customers_DDC.xmi` generated under EnterpriseDataLyer |


        
