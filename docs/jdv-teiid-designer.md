# What is Teiid Designer?

Teiid Designer is an Eclipse based graphical modeling tool for modeling, analyzing, integrating and testing multiple data sources to produce Relational, XML and Web Service Views that expose your business data.


# Why Use Teiid Designer?

Teiid Designer is a visual tool that enables rapid, model-driven definition, integration and testing of data services without programming. With Teiid Designer, not only do you map from data sources to target formats using a visual tool, but you can also:

* resolve semantic differences
* create virtual data structures at a physical or logical level
* use declarative interfaces to integrate, aggregate, and transform the data on its way from source to a target format which is compatible and optimized for consumption by your applications

This allows you to abstract the structure of the information you expose to and use in your applications from the underlying physical data structures. With Teiid Designer, data services are defined quickly, the resulting artifacts are easy to maintain and reuse, and all the valuable work and related metadata are saved for later reference. You can use Teiid Designer to integrate multiple sources, and access them using the common data access standards:

* Web Services / SOAP / XML
* JDBC / SQL
* ODBC / SQL

Teiid Designer is an integral part of the Teiid Designer enterprise-class system for providing data services for service-oriented architectures.


# What is Metadata?

Metadata is data about data. A piece of metadata, called a meta object in the Teiid Designer, contains information about a specific information structure, irrespective of whatever individual data fields that may comprise that structure.

Let us use the example of a very basic database, an address book. Within your address book you certainly have a field or column for the ZIP code (or postal code number). Assuming that the address book services addresses within the United States, you can surmise the following about the column or field for the ZIP code:

* Named ZIPCode
* Numeric
* A string
* Nine characters long
* Located in the StreetAddress table
* Comprised of two parts: The first five digits represent the five ZIP code numbers, the final four represent the ZIP Plus Four digits if available, or 0000 if not, formatted only in integer numeric characters. Errors will result if formatted as 631410.00 or 6314q0000

This definition represents metadata about the ZIP code data in the address book database. It abstracts information from the database itself and becomes useful to describe the content of your enterprise information systems and to determine how a column in one enterprise information source relates to another, and how those two columns could be used together for a new purpose.


# Metadata Models

Metadata model represents a collection of metadata information that describes a complete structure of data.

In a previous example we described the field ZIPCode as a metadata object in an address book database. This meta object represents a single distinct bit of metadata information. We alluded to its parent table, StreetAddress. These meta objects, and others that would describe the other tables and columns within the database, would all combine to form a Source Metadata model for whichever enterprise information system hosts all the objects.

You can have Source Models within your collection of metadata models. These represent physical data storage locations. You can also have View Models, which model the business view of the data. Each contains one type of metadata or another.


# Business and Technical Metadata

Metadata can include different types of information about a piece of data.

* Technical metadata describes the information required to access the data, such as where the data resides or the structure of the data in its native environment.
* Business metadata details other information about the data, such as keywords related to the meta object or notes about the meta object.

> Note: The terms technical and business metadata, refer to the content of the metadata, namely what type of information is contained in the metadata. Do not confuse these with the terms physical and view metadata that indicate what the metadata represents.

### Technical Metadata

Technical metadata represents information that describes how to access the data in its original native data storage. Technical metadata includes things such as datatype, the name of the data in the enterprise information system, and other information that describes the way the native enterprise information system identifies the meta object.

Using our example of an address book database, the following represent the technical metadata we know about the ZIP code column:

* Named ZIPCode
* Nine characters long
* A string
* Located in the StreetAddress table
* Uses SQL Query Language

These bits of information describe the data and information required to access and process the data in the enterprise information system.

### Business Metadata

Business metadata represents additional information about a piece of data, not necessarily related to its physical storage in the enterprise information system or data access requirements. It can also represent descriptions, business rules, and other additional information about a piece of data.

Continuing with our example of the ZIP Code column in the address book database, the following represents business metadata we may know about the ZIP code:

* The first five characters represent the five ZIP code numbers, the final four represent the ZIP Plus. Four digits if available, or 0000 if not
* The application used to populate this field in the database strictly enforces the integrity of the data format

Although the first might seem technical, it does not directly relate to the physical storage of the data. It represents a business rule applied to the contents of the column, not the contents themselves. The second, of course, represents some business information about the way the column was populated. This information, although useful to associate with our definition of the column, does not reflect the physical storage of the data.


# Design-Time and Runtime Metadata

Teiid Designer software distinguishes between design-time metadata and runtime metadata. This distinction becomes important if you use the Teiid Designer Server. Design-time data is laden with details and representations that help the user understand and efficiently organize metadata. Much of that detail is unnecessary to the underlying system that runs the Virtual Database that you will create. Any information that is not absolutely necessary to running the Virtual Database is stripped out of the runtime metadata to ensure maximum system performance.

### Design-Time Metadata

Design-time metadata refers to data within your local directory that you have created or have imported. You can model this metadata in the Teiid Designer, adding Source and View metadata.

### Runtime Metadata

Once you have adequately modeled your enterprise information systems, including the necessary technical metadata that describes the physical structure of your sources, you can use the metadata for data access.

To prepare the metadata for use in the Teiid Designer Server, you take a snapshot of a metadata model for the Teiid Designer Server to use when resolving queries from your client applications. This runtime metadata represents a static version of design-time metadata you created or imported. This snapshot is in the form of a Virtual Database definition, or VDB.

As you create this runtime metadata, the Teiid Designer:

* derives the runtime metadata from a consistent set of metadata models.
* creates a subset of design-time metadata, focusing on the technical metadata that describes the access to underlying enterprise information systems.
* optimizes runtime metadata for data access performance.

You can continue to work with the design-time metadata, but once you have created a runtime metadata model, it remains static.


# Source and View Metadata

In addition to the distinction between business and technical metadata, it is necessary to know the difference between Source Metadata and View Metadata. Source and View metadata refers to what the metadata represents, not its content.

Source Metadata directly represents metadata for an enterprise information system and captures exactly where and how the data is maintained. Source Metadata sounds similar to technical metadata, but Source Metadata can contain both technical and business metadata. When you model Source Metadata, you are modeling the data that your enterprise information systems contain.

View Metadata, on the other hand, represent tailored views that transform the Source Metadata into the terminology and domain of different applications. View Metadata, too, can contain both technical and business metadata. When you model View Metadata, you are modeling the data as your applications (and your enterprise) ultimately use it.

### Modeling Your Source Metadata

When you model the Source Metadata within your enterprise information systems, you capture some detailed information, including:

* Identification of datatype
* Storage formats
* Constraints
* Source-specific locations and names

The Source Metadata captures this detailed technical metadata to provide a map of the data, the location of the data, and how you access it.

This collection of Source Metadata comprises a direct mapping of the information sources within your enterprise. If you use the Teiid Designer Server for information integration, this technical metadata plays an integral part in query resolution.

For example, our ZIPCode column and its parent table StreetAddress map directly to fields within our hypothetical address book database.

To extend our example, we might have a second source of information, a comma separated text file provided by a marketing research vendor. This text file can supply additional demographic information based upon address or ZIP code. This text file would represent another Enterprise Information System(EIS), and the meta objects in its Source Model would describe each comma separated value.

### Modeling Your View Metadata

When you create View Metadata, you are not describing the nature of your physical data storage. Instead, you describe the way your enterprise uses the information in its day to day operations.

View Metadata derives its classes and attributes from other metadata. You can derive View Metadata from Source Metadata that describes the ultimate sources for the metadata or even from other View Metadata. However, when you model View Metadata, you create special views on your existing enterprise information systems that you can tailor to your business use or application expectations. This View Metadata offers many benefits:

* You can expose only the information relevant to an application. The application uses this View Metadata to resolve its queries to the ultimate physical data storage.
* You can add content to existing applications that require different views of the data by adding the View Metadata to the existing View Metadata that application uses. You save time and effort since you do not have to create new models nor modify your existing applications.
* Your applications do not need to refer to specific physical enterprise information systems, offering flexibility and interchangeability. As you change sources for information, you do not have to change your end applications.
* The View Metadata models document the various ways your enterprise uses the information and the different terminology that refers to that information. They do so in a central location.

Our example enterprise information sources, the address book database, and the vendor supplied comma-delimited text file, reside in two different native storage formats and therefore have two Source Metadata models. However, they can represent one business need: a pool of addresses for a mass mailing.

By creating a View Metadata model, we could accurately show that this single View Table, the AddressPool, contains information from the two enterprise information systems. The View Metadata model not only shows from where it gets the information, but also the SQL operations it performs to select its information from its source models.

This View Metadata can not only reflect and describe how your organization uses that information, but, if your enterprise uses the Teiid Designer Server, your applications can use the View Metadata to resolve queries.

# Models

### What are Models

A model is a representation of a set of information constructs. A familiar model is the relational model, which defines tables composed of columns and containing records of data. Another familiar model is the XML model, which defines hierarchical data sets.

In Teiid Designer, models are used to define the entities, and relationships between those entities, required to fully define the integration of information sets so that they may be accessed in a uniform manner, using a single API and access protocol. The file extension used for these models is .xm i (for example, NorthwindOracle.xm i) which adheres to the XMI syntax defined by the OMG.

[Sample Moldes Mysql Data Source](../workspace/MysqlDataSource/sources/MySQL_BokerInfo.xmi)

[Sample Models XML Data Source](../workspace/XMLDataSource/sources/BooksModel.xmi)

The fundamental models in Teiid Designer define the structural and data characteristics of the information contained in data sources. These are referred to as source models. Teiid Designer uses the information in source models to federate the information in multiple sources, so that from a user's viewpoint these all appear to be in a single source.

In addition to source models, Teiid Designer provides the ability to define a variety of view models. These can be used to define a layer of abstraction above the physical (or source) layer, so that information can be presented to end users and consuming applications in business terms rather than as it is physically stored. Views are mapped to sources using transformations between models. These business views can be in a variety of forms:

* Relational Tables and Views
* XML
* Web services
* Functions

A third model type, logical, provides the ability to define models from a logical or structural perspective.

### Guiding through the process

To make the process of using Teiid Designer to build models more as easy as posssible, a guides view has been introduced. It provides action sets which bring together the actions necessary to develop models for specific use-cases. Action sets are available for the following scenerios:

* Consuming a SOAP Web Service
* Modelling from a Flat File Source (a text file)
* Modelling from a JDBC Data Source
* Modelling from a Local XML File Source
* Modelling from a Remote XML File Source
* Modelling from a T eiid Data Source (deployed on server)
* Connecting to a Teiid Server

### Model Classes and Types

Teiid Designer can be used to model a variety of classes of models. Each of these represent a conceptually different classification of models.

* Relational - Model data that can be represented in table columns and records form. Relational models can represent structures found in relational databases, spreadsheets, text files, or simple Web services.
* XML - Model that represents the basic structures of XML documents. These can be backed by XML Schemas. XML models represent nested structures, including recursive hierarchies.
* XML Schema - W3C standard for formally defining the structure and constraints of XML documents, as well as the datatypes defining permissible values in XML documents.
* Web Services - which define Web service interfaces, operations, and operation input and output parameters (in the form of XML Schemas).
* Function - The Function metamodel supports the capability to provide user defined functions, including binary source jars, to use in custom transformation SQL statements.

### The Virtual Database

The critical artifact that Teiid Designer is intended to manage is the VDB, or Virtual DataBase. Through the JBoss Data Virtualization server, VDB's behave like standard JDBC database schema which can be connected to, queried and updated based on how the VDB is configured. Since VDB's are just databases once they are deployed, they can be used as sources to other view model transformations. This allows creating and deploying re-usable or common VDB's in multiple layers depending on your business needs.

### VDB Content and Structure

In Teiid Designer, the VDB file names use a .vdb file extension. VDBs are structurally just ZIP archive files containing 3 folders:

* META-INF - contains vdb.xm l definition file.
* runtime-inf - contains a binary INDEX file for each model included in your VDB
* project folder - contains of the models you will be adding in the VDB Editor (that is, * .xm i and .xsd files)

When deployed, the metadata is consumed by JBoss Data Virtualization in order to create the necessary runtime metadata for your model definitions.

The vdb.xml file contains:

* VDB name, version, properties
* contained model information (name, translator name, connection info)
* translator info
* data role definitions for the referenced models
* import VDB references

Fortunately, Teiid Designer simplifies the management of your VDBs by providing a dedicated VDB Editor which maintains a consistent, valid vdb.xml file for you and assists in synchronizing your workspace models with any related models in your VDB.

### Model Validation

Models must be in a valid state in order to be used for data access. Validation of a single model means that it must be in a self-consistent and complete state, meaning that there are no missing pieces and no references to non-existent entities. Validation of multiple models checks that all inter-model dependencies are present and resolvable.

Models must always be validated when they are deployed in a VDB for data access purposes. Teiid Designer will automatically validate all models whenever they are saved.

### Testing Your Models

Designing and working with data is often much easier when you can see the information you are working with. The T eiid Designer's Preview Data feature makes this possible and allows you to instantly preview the information described by any object, whether it's a physical table or a virtual view. In other words, you can test the views with actual data by simply selecting the table, view, procedure or XML document. The preview functionality insures that data access behavior in Teiid Designer will reliably match when the VDB is deployed to the Server.

Previewing information is a fast and easy way to sample the data. Of course, to run more complicated queries like what your application likely uses, simply execute the VDB in Teiid Designer and type in any query or SQL statement.

After creating your models, you can test them by using the Preview Data action. By selecting a desired table object and executing the action, the results of a simple query will be displayed in the Data Tools SQL Results view. This action is accessible throughout the T eiid Designer in various view toolbars and context menus.

Previewable objects include:

* Relational table or view, including tables involving access patterns
* Relational procedure
* Web Service operation
* XML Document staging table

> Note: If attempting to preview a relational access pattern, a web service operation or a relational procedure with input parameters, a dialog will request values for required parameters.

# Model Object Extensions

Teiid Designer in conjunction with JBoss Data Virtualization provides an extensible framework to define custom properties for model objects other than what is defined in the metamodel. These custom property values are added to your VDB and included in your runtime metadata. This additional metadata is available to use in your custom translators for both source query manipulation as well as adjusting your result set data being returned.

Teiid Designer introduces a new Model Extension Definition (MED) framework that will replace the EMF based Model Extension metamodel.

This MED framework provides the following improvements:

* Eliminate need for separate EMF metamodel.
* Simpler approach including reduction of extendable metamodels and metamodel objects (Relational, Web Services, XML Document, User Defined Functions) and replacing EMF terminology with basic object types.
* Allows metamodels to be extended by multiple MEDs.
* MEDs are stored in models so no added dependency needed in VDB.

### Model Extension Definition (MED)

The purpose of a MED is to define one or more sets of extension properties. Each set of extension properties pertains to one model object type (or metaclass). Each MED consists of the following:

* Namespace Prefix - a unique identifier. Typically only a small number of letters and can be used as an abbreviation for the namespace URI.
* Namespace URI - a unique URI.
* Extended Metamodel URI (Model Class) - the metamodel URI that is being extended. Each metamodel URI also has model class and that is typically what is shown in the Designer. The model classes supported for extension are: Relational, Web Service, XML Document, and Function.
* Version - (currently not being used)
* Description - an optional description or purpose.
* Extended Model Object Types (Metaclasses) - a set of model object types, or metaclasses, that have extension properties defined.
* Properties - the extension property definitions grouped by model object type.

A MED file is an XML file with an extension of m xd. A MED schema file (see attached modelExtension.xsd file) is used to validate a MED file. Here is a sample MED file:

~~~
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<modelExtension xm lns:p="http://org.teiid.modelExtension/2011" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" metamodelUri="http://www.metamatrix.com/metamodels/Relational" namespacePrefix="mymodelextension" namespaceUri="org.m y.extension.mymodelextension" version="1" xsi:schemaLocation="http://org.teiid.modelExtension/2011modelExtension.xsd" xmlns="http://org.teiid.modelExtension/2011">
	<p:description>This is my model extension</p:description>
	<p:extendedMetaclass name="com.metamatrix.metamodels.relational.impl.BaseTableImpl">
		<p:property advanced="false" index="true" masked="false" name="copyable" required="false" type="boolean">
			<p:description locale="en_US">Indicates if table can be copied</p:description>
			<p:display locale="en_US">Copyable</p:display>
		</p:property>
	</p:extendedMetaclass>
</modelExtension>
~~~

The MED Registry is where the MEDs used by Teiid Designer are stored. MED files can be edited by opening the .m xd file in the Extension Editor.

### Model Extension Definition Registry (MED Registry)

A MED registry keeps track of all the MEDs that are registered in a workspace. Only registered MEDs can be used to extend a model. There are 2 different types of MEDs stored in the registry:

* Built-In MED - these are registered during Teiid Designer installation. These MEDs cannot be updated or unregistered by the user.
* User Defined MED - these are created by the user. These MEDs can be updated, registered, and unregistered by the user.

The MED Registry state is persisted and is restored each time a new session is started.
