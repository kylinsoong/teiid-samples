# Objective

Primary purpose of this document is supply the steps for install `Red Hat JBoss Data Virtualization`, `Red Hat JBoss Developer Studio` and reference Environment setting up, the document cover:

* Installing Red Hat JBoss Data Virtualization
* Installing Red Hat JBoss Developer Studio
* Installing Mysql Data Base

# Installing Red Hat JBoss Data Virtualization

Download JBoss Data Virtualization installer binary via clicking the green button at [http://www.jboss.org/products/datavirt/overview/](http://www.jboss.org/products/datavirt/overview/)

Open a terminal window and navigate to the location where the GUI installer was downloaded. Run the installer using java at the command prompt: java -jar jboss-dvinstaller-{version}.jar, for example:

~~~
java -jar jboss-dv-installer-6.0.0.GA-redhat-4.jar
~~~

This command will pop up the installation wizard, follow the wizard finish the installation. After the installation, there will be a jboss-eap-6.1 folder generated.

Edit the `jboss-eap-6.1/standalone/configuration/teiid-security-roles.properties`, enable 'user=odata,example-role'.

Edit the `jboss-eap-6.1/standalone/configuration/teiid-security-users.properties`, enable 'user=user'.

# Installing Red Hat JBoss Developer Studio

Download JBoss Developer Studio installer binary via clicking the green button at [https://www.jboss.org/products/devstudio/overview/](https://www.jboss.org/products/devstudio/overview/)

Open a terminal window and navigate to the location where the GUI installer was downloaded. Run the installer using java at the command prompt: java -jar {jbdevstudioinstaller.jar}, for example:

~~~
java -jar jbdevstudio-product-eap-universal-7.1.1.GA-v20140314-2145-B688.jar
~~~

This command will pop up the installation wizard, follow the wizard finish the installation. When the installation is completed, run JBoss Developer Studio, when the Studio starts, you are asked to choose the workspace folder for the session. The workspace is where your projects are stored.

> JBoss Data Virtualization Development tools are necessary for Data Virtualization Design time, the following steps is for the tools installation

From the JBDS menu bar, choose Help → Install New Software, In the "Work with:" field on the Install wizard, paste the following link:

[http://download.jboss.org/jbosstools/updates/stable/kepler/integration-stack/](http://download.jboss.org/jbosstools/updates/stable/kepler/integration-stack/)

and click the Add button. Select JBoss Data Virtualization Development option in the list and click Next to finish the tools installation. The restart is necessary, restart the JBoss Developer Studio to make the changes take effect. 

> For development convenience, we need attach the installed JDV server with JBDS, the following steps is for the server attachment.

Enable the Servers pane via Window → Show View → Other → Server → Servers, Click the link [No servers are available. Click this link to create a new server…](Click the link No servers are available. Click this link to create a new server…) finish the sever attachment.

# Installing Mysql Data Base

