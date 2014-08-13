package org.teiid.eclipselink.platform;

import org.eclipse.persistence.platform.database.DatabasePlatform;

public class TeiidServerPlatform extends DatabasePlatform{

	private static final long serialVersionUID = 6894570254643353289L;
	
	public TeiidServerPlatform() {
		super();
		this.pingSQL = "SELECT 1";
	}

	/**
	 * Avoid alter/create Constraint/index
	 */
	public boolean supportsDeleteOnCascade() {
		return false;
	}

	public boolean supportsForeignKeyConstraints() {
		return false;
	}

	public boolean requiresUniqueConstraintCreationOnTableCreate() {
		return false;
	}
	
	public boolean supportsIndexes() {
		return false;
	}

	public boolean supportsTempTables() {
		return true;
	}

	public boolean supportsLocalTempTables() {
		return true;
	}

	public boolean supportsGlobalTempTables() {
		return false;
	}

	public void setPrintOuterJoinInWhereClause(boolean printOuterJoinInWhereClause) {
		super.setPrintOuterJoinInWhereClause(false);
	}

	
	
	

}
