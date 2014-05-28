# What's this

This docuemnts show how to quick install *Teiid Runtime 8.7 Final* to JBoss EAP 6.1.

# Steps to install Teiid

This section cover steps to install *Teiid Runtime 8.7 Final* to JBoss EAP 6.1.

### Install JBoss EAP 6.1

Download `jboss-eap-6.1.1.zip` from Red Hat Customer Portal, Install the EAP 6.1 by unzipping into a known location:

~~~
unzip jboss-eap-6.1.1.zip
~~~

### Install Teiid Runtime 8.7 Final

Download Teiid Runtime 8.7 Final from [Teiid Download Page](http://teiid.jboss.org/downloads/), the download will generate `teiid-8.7.0.Final-jboss-dist.zip`, unzip the download to JBoss home:

~~~
unzip teiid-8.7.0.Final-jboss-dist.zip -d jboss-eap-6.1
~~~

### Start & Test Teiid Runtime

Standalone Mode start up:

~~~
./standalone.sh -c=standalone-teiid.xml
~~~

Domain Mode start up:

~~~
./domain.sh
~~~
