package com.taobao.tdhs.jdbc;

import java.sql.*;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-26 上午10:29
 */
public class TDHSDatabaseMetaData implements DatabaseMetaData {

    private Connection conn;

    /**
     * Constructor TDHSDatabaseMetaData creates a new TDHSDatabaseMetaData instance.
     *
     * @param conn of type Connection
     */
    public TDHSDatabaseMetaData(Connection conn) {
        this.conn = conn;
    }

    /**
     * Method allProceduresAreCallable ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean allProceduresAreCallable() throws SQLException {
        return false;
    }

    /**
     * Method allTablesAreSelectable ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean allTablesAreSelectable() throws SQLException {
        return false;
    }

    /**
     * Method getURL returns the URL of this TDHSDatabaseMetaData object.
     *
     * @return the URL (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getURL() throws SQLException {
        return null;
    }

    /**
     * Method getUserName returns the userName of this TDHSDatabaseMetaData object.
     *
     * @return the userName (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getUserName() throws SQLException {
        return null;
    }

    /**
     * Method isReadOnly returns the readOnly of this TDHSDatabaseMetaData object.
     *
     * @return the readOnly (type boolean) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    /**
     * Method nullsAreSortedHigh ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean nullsAreSortedHigh() throws SQLException {
        return false;
    }

    /**
     * Method nullsAreSortedLow ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean nullsAreSortedLow() throws SQLException {
        return false;
    }

    /**
     * Method nullsAreSortedAtStart ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean nullsAreSortedAtStart() throws SQLException {
        return false;
    }

    /**
     * Method nullsAreSortedAtEnd ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return false;
    }

    /**
     * Method getDatabaseProductName returns the databaseProductName of this TDHSDatabaseMetaData object.
     *
     * @return the databaseProductName (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getDatabaseProductName() throws SQLException {
        return "TDHS";
    }

    /**
     * Method getDatabaseProductVersion returns the databaseProductVersion of this TDHSDatabaseMetaData object.
     *
     * @return the databaseProductVersion (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getDatabaseProductVersion() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getDriverName returns the driverName of this TDHSDatabaseMetaData object.
     *
     * @return the driverName (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getDriverName() throws SQLException {
        return "TDH_Socket JDBC Driver";
    }

    /**
     * Method getDriverVersion returns the driverVersion of this TDHSDatabaseMetaData object.
     *
     * @return the driverVersion (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getDriverVersion() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getDriverMajorVersion returns the driverMajorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the driverMajorVersion (type int) of this TDHSDatabaseMetaData object.
     */
    public int getDriverMajorVersion() {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getDriverMinorVersion returns the driverMinorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the driverMinorVersion (type int) of this TDHSDatabaseMetaData object.
     */
    public int getDriverMinorVersion() {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method usesLocalFiles ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }

    /**
     * Method usesLocalFilePerTable ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }

    /**
     * Method supportsMixedCaseIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesUpperCaseIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesLowerCaseIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesMixedCaseIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsMixedCaseQuotedIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesUpperCaseQuotedIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesLowerCaseQuotedIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method storesMixedCaseQuotedIdentifiers ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getIdentifierQuoteString returns the identifierQuoteString of this TDHSDatabaseMetaData object.
     *
     * @return the identifierQuoteString (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getIdentifierQuoteString() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSQLKeywords returns the SQLKeywords of this TDHSDatabaseMetaData object.
     *
     * @return the SQLKeywords (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getSQLKeywords() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getNumericFunctions returns the numericFunctions of this TDHSDatabaseMetaData object.
     *
     * @return the numericFunctions (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getNumericFunctions() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getStringFunctions returns the stringFunctions of this TDHSDatabaseMetaData object.
     *
     * @return the stringFunctions (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getStringFunctions() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSystemFunctions returns the systemFunctions of this TDHSDatabaseMetaData object.
     *
     * @return the systemFunctions (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getSystemFunctions() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getTimeDateFunctions returns the timeDateFunctions of this TDHSDatabaseMetaData object.
     *
     * @return the timeDateFunctions (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getTimeDateFunctions() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSearchStringEscape returns the searchStringEscape of this TDHSDatabaseMetaData object.
     *
     * @return the searchStringEscape (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getSearchStringEscape() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getExtraNameCharacters returns the extraNameCharacters of this TDHSDatabaseMetaData object.
     *
     * @return the extraNameCharacters (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getExtraNameCharacters() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsAlterTableWithAddColumn ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsAlterTableWithDropColumn ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsColumnAliasing ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsColumnAliasing() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method nullPlusNonNullIsNull ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsConvert ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsConvert() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsConvert ...
     *
     * @param fromType of type int
     * @param toType   of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsTableCorrelationNames ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsTableCorrelationNames() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsDifferentTableCorrelationNames ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsExpressionsInOrderBy ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOrderByUnrelated ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOrderByUnrelated() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsGroupBy ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsGroupBy() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsGroupByUnrelated ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsGroupByUnrelated() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsGroupByBeyondSelect ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsLikeEscapeClause ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsLikeEscapeClause() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsMultipleResultSets ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMultipleResultSets() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsMultipleTransactions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMultipleTransactions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsNonNullableColumns ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsNonNullableColumns() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsMinimumSQLGrammar ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCoreSQLGrammar ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsExtendedSQLGrammar ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsANSI92EntryLevelSQL ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsANSI92IntermediateSQL ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsANSI92FullSQL ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsANSI92FullSQL() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsIntegrityEnhancementFacility ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOuterJoins ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOuterJoins() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsFullOuterJoins ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsFullOuterJoins() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsLimitedOuterJoins ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getSchemaTerm returns the schemaTerm of this TDHSDatabaseMetaData object.
     *
     * @return the schemaTerm (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getSchemaTerm() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getProcedureTerm returns the procedureTerm of this TDHSDatabaseMetaData object.
     *
     * @return the procedureTerm (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getProcedureTerm() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getCatalogTerm returns the catalogTerm of this TDHSDatabaseMetaData object.
     *
     * @return the catalogTerm (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getCatalogTerm() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method isCatalogAtStart returns the catalogAtStart of this TDHSDatabaseMetaData object.
     *
     * @return the catalogAtStart (type boolean) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public boolean isCatalogAtStart() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getCatalogSeparator returns the catalogSeparator of this TDHSDatabaseMetaData object.
     *
     * @return the catalogSeparator (type String) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public String getCatalogSeparator() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsSchemasInDataManipulation ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSchemasInProcedureCalls ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSchemasInTableDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSchemasInIndexDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSchemasInPrivilegeDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCatalogsInDataManipulation ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCatalogsInProcedureCalls ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCatalogsInTableDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCatalogsInIndexDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCatalogsInPrivilegeDefinitions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsPositionedDelete ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsPositionedDelete() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsPositionedUpdate ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSelectForUpdate ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSelectForUpdate() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsStoredProcedures ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsStoredProcedures() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSubqueriesInComparisons ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSubqueriesInExists ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSubqueriesInExists() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSubqueriesInIns ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSubqueriesInIns() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsSubqueriesInQuantifieds ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsCorrelatedSubqueries ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsUnion ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsUnion() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsUnionAll ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsUnionAll() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOpenCursorsAcrossCommit ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOpenCursorsAcrossRollback ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOpenStatementsAcrossCommit ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsOpenStatementsAcrossRollback ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getMaxBinaryLiteralLength returns the maxBinaryLiteralLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxBinaryLiteralLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxCharLiteralLength returns the maxCharLiteralLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxCharLiteralLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxCharLiteralLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnNameLength returns the maxColumnNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnsInGroupBy returns the maxColumnsInGroupBy of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnsInGroupBy (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnsInIndex returns the maxColumnsInIndex of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnsInIndex (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnsInIndex() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnsInOrderBy returns the maxColumnsInOrderBy of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnsInOrderBy (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnsInSelect returns the maxColumnsInSelect of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnsInSelect (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnsInSelect() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxColumnsInTable returns the maxColumnsInTable of this TDHSDatabaseMetaData object.
     *
     * @return the maxColumnsInTable (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxColumnsInTable() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxConnections returns the maxConnections of this TDHSDatabaseMetaData object.
     *
     * @return the maxConnections (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxConnections() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxCursorNameLength returns the maxCursorNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxCursorNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxCursorNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxIndexLength returns the maxIndexLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxIndexLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxIndexLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxSchemaNameLength returns the maxSchemaNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxSchemaNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxSchemaNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxProcedureNameLength returns the maxProcedureNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxProcedureNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxProcedureNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxCatalogNameLength returns the maxCatalogNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxCatalogNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxCatalogNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxRowSize returns the maxRowSize of this TDHSDatabaseMetaData object.
     *
     * @return the maxRowSize (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxRowSize() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method doesMaxRowSizeIncludeBlobs ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getMaxStatementLength returns the maxStatementLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxStatementLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxStatementLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxStatements returns the maxStatements of this TDHSDatabaseMetaData object.
     *
     * @return the maxStatements (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxStatements() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxTableNameLength returns the maxTableNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxTableNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxTableNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxTablesInSelect returns the maxTablesInSelect of this TDHSDatabaseMetaData object.
     *
     * @return the maxTablesInSelect (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxTablesInSelect() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getMaxUserNameLength returns the maxUserNameLength of this TDHSDatabaseMetaData object.
     *
     * @return the maxUserNameLength (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getMaxUserNameLength() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getDefaultTransactionIsolation returns the defaultTransactionIsolation of this TDHSDatabaseMetaData object.
     *
     * @return the defaultTransactionIsolation (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getDefaultTransactionIsolation() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method supportsTransactions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsTransactions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsTransactionIsolationLevel ...
     *
     * @param level of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsDataDefinitionAndDataManipulationTransactions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsDataManipulationTransactionsOnly ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method dataDefinitionCausesTransactionCommit ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method dataDefinitionIgnoredInTransactions ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getProcedures ...
     *
     * @param catalog              of type String
     * @param schemaPattern        of type String
     * @param procedureNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getProcedureColumns ...
     *
     * @param catalog              of type String
     * @param schemaPattern        of type String
     * @param procedureNamePattern of type String
     * @param columnNamePattern    of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
                                         String columnNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getTables ...
     *
     * @param catalog          of type String
     * @param schemaPattern    of type String
     * @param tableNamePattern of type String
     * @param types            of type String[]
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSchemas returns the schemas of this TDHSDatabaseMetaData object.
     *
     * @return the schemas (type ResultSet) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public ResultSet getSchemas() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getCatalogs returns the catalogs of this TDHSDatabaseMetaData object.
     *
     * @return the catalogs (type ResultSet) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public ResultSet getCatalogs() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getTableTypes returns the tableTypes of this TDHSDatabaseMetaData object.
     *
     * @return the tableTypes (type ResultSet) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public ResultSet getTableTypes() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getColumns ...
     *
     * @param catalog           of type String
     * @param schemaPattern     of type String
     * @param tableNamePattern  of type String
     * @param columnNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getColumnPrivileges ...
     *
     * @param catalog           of type String
     * @param schema            of type String
     * @param table             of type String
     * @param columnNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getTablePrivileges ...
     *
     * @param catalog          of type String
     * @param schemaPattern    of type String
     * @param tableNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getBestRowIdentifier ...
     *
     * @param catalog  of type String
     * @param schema   of type String
     * @param table    of type String
     * @param scope    of type int
     * @param nullable of type boolean
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getVersionColumns ...
     *
     * @param catalog of type String
     * @param schema  of type String
     * @param table   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getPrimaryKeys ...
     *
     * @param catalog of type String
     * @param schema  of type String
     * @param table   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getImportedKeys ...
     *
     * @param catalog of type String
     * @param schema  of type String
     * @param table   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getExportedKeys ...
     *
     * @param catalog of type String
     * @param schema  of type String
     * @param table   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getCrossReference ...
     *
     * @param parentCatalog  of type String
     * @param parentSchema   of type String
     * @param parentTable    of type String
     * @param foreignCatalog of type String
     * @param foreignSchema  of type String
     * @param foreignTable   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
                                       String foreignCatalog, String foreignSchema, String foreignTable)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getTypeInfo returns the typeInfo of this TDHSDatabaseMetaData object.
     *
     * @return the typeInfo (type ResultSet) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public ResultSet getTypeInfo() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getIndexInfo ...
     *
     * @param catalog     of type String
     * @param schema      of type String
     * @param table       of type String
     * @param unique      of type boolean
     * @param approximate of type boolean
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsResultSetType ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsResultSetType(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsResultSetConcurrency ...
     *
     * @param type        of type int
     * @param concurrency of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method ownUpdatesAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method ownDeletesAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method ownInsertsAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method othersUpdatesAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method othersDeletesAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method othersInsertsAreVisible ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method updatesAreDetected ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean updatesAreDetected(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method deletesAreDetected ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean deletesAreDetected(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method insertsAreDetected ...
     *
     * @param type of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean insertsAreDetected(int type) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsBatchUpdates ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsBatchUpdates() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getUDTs ...
     *
     * @param catalog         of type String
     * @param schemaPattern   of type String
     * @param typeNamePattern of type String
     * @param types           of type int[]
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getConnection returns the connection of this TDHSDatabaseMetaData object.
     *
     * @return the connection (type Connection) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public Connection getConnection() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsSavepoints ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsSavepoints() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsNamedParameters ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsNamedParameters() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsMultipleOpenResults ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsMultipleOpenResults() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsGetGeneratedKeys ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsGetGeneratedKeys() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getSuperTypes ...
     *
     * @param catalog         of type String
     * @param schemaPattern   of type String
     * @param typeNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSuperTables ...
     *
     * @param catalog          of type String
     * @param schemaPattern    of type String
     * @param tableNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getAttributes ...
     *
     * @param catalog              of type String
     * @param schemaPattern        of type String
     * @param typeNamePattern      of type String
     * @param attributeNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
                                   String attributeNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsResultSetHoldability ...
     *
     * @param holdability of type int
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getResultSetHoldability returns the resultSetHoldability of this TDHSDatabaseMetaData object.
     *
     * @return the resultSetHoldability (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getResultSetHoldability() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getDatabaseMajorVersion returns the databaseMajorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the databaseMajorVersion (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getDatabaseMajorVersion() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getDatabaseMinorVersion returns the databaseMinorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the databaseMinorVersion (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getDatabaseMinorVersion() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getJDBCMajorVersion returns the JDBCMajorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the JDBCMajorVersion (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getJDBCMajorVersion() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getJDBCMinorVersion returns the JDBCMinorVersion of this TDHSDatabaseMetaData object.
     *
     * @return the JDBCMinorVersion (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getJDBCMinorVersion() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method getSQLStateType returns the SQLStateType of this TDHSDatabaseMetaData object.
     *
     * @return the SQLStateType (type int) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public int getSQLStateType() throws SQLException {
        return 0;  //FIXME 编写实现
    }

    /**
     * Method locatorsUpdateCopy ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean locatorsUpdateCopy() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method supportsStatementPooling ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsStatementPooling() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getRowIdLifetime returns the rowIdLifetime of this TDHSDatabaseMetaData object.
     *
     * @return the rowIdLifetime (type RowIdLifetime) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getSchemas ...
     *
     * @param catalog       of type String
     * @param schemaPattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method supportsStoredFunctionsUsingCallSyntax ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method autoCommitFailureClosesAllResultSets ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method getClientInfoProperties returns the clientInfoProperties of this TDHSDatabaseMetaData object.
     *
     * @return the clientInfoProperties (type ResultSet) of this TDHSDatabaseMetaData object.
     *
     * @throws SQLException when
     */
    public ResultSet getClientInfoProperties() throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getFunctions ...
     *
     * @param catalog             of type String
     * @param schemaPattern       of type String
     * @param functionNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
            throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getFunctionColumns ...
     *
     * @param catalog             of type String
     * @param schemaPattern       of type String
     * @param functionNamePattern of type String
     * @param columnNamePattern   of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
                                        String columnNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method getPseudoColumns ...
     *
     * @param catalog           of type String
     * @param schemaPattern     of type String
     * @param tableNamePattern  of type String
     * @param columnNamePattern of type String
     *
     * @return ResultSet
     *
     * @throws SQLException when
     */
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern,
                                      String columnNamePattern) throws SQLException {
        return null;  //FIXME 编写实现
    }

    /**
     * Method generatedKeyAlwaysReturned ...
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        return false;  //FIXME 编写实现
    }

    /**
     * Method unwrap ...
     *
     * @param iface of type Class<T>
     *
     * @return T
     *
     * @throws SQLException when
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            // This works for classes that aren't actually wrapping
            // anything
            return iface.cast(this);
        } catch (ClassCastException cce) {
            throw new SQLException(cce);
        }
    }

    /**
     * Method isWrapperFor ...
     *
     * @param iface of type Class<?>
     *
     * @return boolean
     *
     * @throws SQLException when
     */
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}
