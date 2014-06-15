# Workshop setting up script

| *Journals* | *Timestamp* | *Notes* |
|------------|:-----------:|--------:|
|Install postgrest sql with initial test sql data, install mysql with initial test sql data, install `JDV 6.0.0.GA`, install `JBDS 7.1.1` |Jun 11 | postgres user password: `redhat`, mysql root user password: `redhat`, JDV password `password1!`|
|JDV start throw `org.teiid.core.TeiidRuntimeException: TEIID40065 Failed to resolve the bind address` |Jun 11 |[Bugzilla 1108418](https://bugzilla.redhat.com/show_bug.cgi?id=1108418), [startup-exception](../startup-exception) |
|JDV Designer can not connect to ptostgres database |Jun 12 |[install and configure the PostgreSQL](https://github.com/kylinsoong/workspace-2014/blob/master/docs/RHL/postgresql_administration.asciidoc) |
|Designer: create teiid project, create source model, import metadata from the product database|Jun 12 |[chapter 4.1 - 4.4](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
|Designer: preview data via teiid server, advanced concept for importing metadata, import metadata from a flat file|Jun 13 |[postgresql-permission](../postgresql-permission), [chapter 4.5 - 4.7](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
|Designer: create a Virtual Base Layer |Jun 14 |[chapter 5](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
|Designer: create an Enterprise Data Layer |Jun 14 |[chapter 6](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
|Designer: create a Data Federation Layer |Jun 14 |[chapter 7](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
|Designer: create a Web Service |Jun 14 |[chapter 8](https://github.com/DataVirtualizationByExample/DVWorkshop/blob/master/docs/jboss-dv-workshop.pdf) |
