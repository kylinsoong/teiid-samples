# Stepup Steps

### Install Resource Adapter

Either use the following CLI commands

~~~
/subsystem=resource-adapters/resource-adapter=filemarketdata:add(module=org.jboss.teiid.resource-adapter.file)
subsystem=resource-adapters/resource-adapter=filemarketdata/connection-definitions=fileDS:add(jndi-name=java:/marketdata-file, class-name=org.teiid.resource.adapter.file.FileManagedConnectionFactory, enabled=true, use-java-context=true)
/subsystem=resource-adapters/resource-adapter=filemarketdata/connection-definitions=fileDS/config-properties=ParentDirectory:add(value=/home/kylin/project/teiid-designer-samples/metadata)
/subsystem=resource-adapters/resource-adapter=filemarketdata/connection-definitions=fileDS/config-properties=AllowParentPaths:add(value=true)
/subsystem=resource-adapters/resource-adapter=filemarketdata:activate
/:reload
~~~

Or, edit the `standalone.xml` in subsystem `urn:jboss:domain:resource-adapters:1.1`, add the following

~~~
                <resource-adapter id="filemarketdata">
                    <module slot="main" id="org.jboss.teiid.resource-adapter.file"/>
                    <connection-definitions>
                        <connection-definition class-name="org.teiid.resource.adapter.file.FileManagedConnectionFactory" jndi-name="java:/marketdata-file" enabled="true" use-java-context="true" pool-name="fileDS">
                            <config-property name="ParentDirectory">
                                /home/kylin/project/teiid-designer-samples/metadata
                            </config-property>
                            <config-property name="AllowParentPaths">
                                true
                            </config-property>
                        </connection-definition>
                    </connection-definitions>
                </resource-adapter>
~~~
