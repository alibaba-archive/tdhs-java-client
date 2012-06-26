package com.taobao.tdhs.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * 带池功能的DataSource
 *
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-26 下午3:50
 */
public class SimplePooledTDHSDataSource implements DataSource {

    private PrintWriter logWriter;

    private PooledTDHSConnection lastOne = null;

    private AtomicLong count = new AtomicLong(0);

    private final String url;

    /**
     * Constructor SimplePooledTDHSDataSource creates a new SimplePooledTDHSDataSource instance.
     *
     * @param url of type String
     */
    public SimplePooledTDHSDataSource(String url) {
        this.url = url;
    }


    /**
     * Method getConnection returns the connection of this PooledTDHSDataSource object.
     *
     * @return the connection (type Connection) of this PooledTDHSDataSource object.
     *
     * @throws SQLException when
     */
    public synchronized Connection getConnection() throws SQLException {
        PooledTDHSConnection ret;
        if (count.get() == 0 && lastOne != null) {
            ret = lastOne;
            lastOne = null;
        } else {
            try {
                ret = new PooledTDHSConnection(createConnection(), this);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
        count.incrementAndGet();
        return ret;
    }

    /**
     * Method createConnection ...
     *
     * @return Connection
     */
    private TDHSConnection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.taobao.tdhs.jdbc.Driver");
        return (TDHSConnection) DriverManager.getConnection(url, null, null);
    }


    /**
     * Method returnToDataSource ...
     *
     * @param conn of type PooledTDHSConnection
     *
     * @return boolean  true mean cached can't be real close
     */
    private synchronized boolean returnToDataSource(PooledTDHSConnection conn) {
        if (count.decrementAndGet() == 0) {
            lastOne = conn;
            return true;
        }
        return false;
    }

    /**
     * Method getConnection ...
     *
     * @param username of type String
     * @param password of type String
     *
     * @return Connection
     *
     * @throws SQLException when
     */
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    /**
     * Method getLogWriter returns the logWriter of this PooledTDHSDataSource object.
     *
     * @return the logWriter (type PrintWriter) of this PooledTDHSDataSource object.
     *
     * @throws SQLException when
     */
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    /**
     * Method setLogWriter sets the logWriter of this PooledTDHSDataSource object.
     *
     * @param out the logWriter of this PooledTDHSDataSource object.
     *
     * @throws SQLException when
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    /**
     * Method setLoginTimeout sets the loginTimeout of this PooledTDHSDataSource object.
     *
     * @param seconds the loginTimeout of this PooledTDHSDataSource object.
     *
     * @throws SQLException when
     */
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    /**
     * Method getLoginTimeout returns the loginTimeout of this PooledTDHSDataSource object.
     *
     * @return the loginTimeout (type int) of this PooledTDHSDataSource object.
     *
     * @throws SQLException when
     */
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    /**
     * Method getParentLogger returns the parentLogger of this PooledTDHSDataSource object.
     *
     * @return the parentLogger (type Logger) of this PooledTDHSDataSource object.
     *
     * @throws SQLFeatureNotSupportedException
     *          when
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
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


    private class PooledTDHSConnection implements Connection {

        private TDHSConnection tdhsConn;

        private SimplePooledTDHSDataSource pooledTDHSDataSource;

        private PooledTDHSConnection(TDHSConnection tdhsConn, SimplePooledTDHSDataSource pooledTDHSDataSource) {
            this.tdhsConn = tdhsConn;
            this.pooledTDHSDataSource = pooledTDHSDataSource;
        }

        public Statement createStatement() throws SQLException {
            return tdhsConn.createStatement();
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return tdhsConn.prepareStatement(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return tdhsConn.prepareCall(sql);
        }

        public String nativeSQL(String sql) throws SQLException {
            return tdhsConn.nativeSQL(sql);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            tdhsConn.setAutoCommit(autoCommit);
        }

        public boolean getAutoCommit() throws SQLException {
            return tdhsConn.getAutoCommit();
        }

        public void commit() throws SQLException {
            tdhsConn.commit();
        }

        public void rollback() throws SQLException {
            tdhsConn.rollback();
        }

        public void close() throws SQLException {
            if (!pooledTDHSDataSource.returnToDataSource(this)) {
                tdhsConn.close();
            }
        }

        public boolean isClosed() throws SQLException {
            return tdhsConn.isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return tdhsConn.getMetaData();
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            tdhsConn.setReadOnly(readOnly);
        }

        public boolean isReadOnly() throws SQLException {
            return tdhsConn.isReadOnly();
        }

        public void setCatalog(String catalog) throws SQLException {
            tdhsConn.setCatalog(catalog);
        }

        public String getCatalog() throws SQLException {
            return tdhsConn.getCatalog();
        }

        public void setTransactionIsolation(int level) throws SQLException {
            tdhsConn.setTransactionIsolation(level);
        }

        public int getTransactionIsolation() throws SQLException {
            return tdhsConn.getTransactionIsolation();
        }

        public SQLWarning getWarnings() throws SQLException {
            return tdhsConn.getWarnings();
        }

        public void clearWarnings() throws SQLException {
            tdhsConn.clearWarnings();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return tdhsConn.createStatement(resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return tdhsConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return tdhsConn.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return tdhsConn.getTypeMap();
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            tdhsConn.setTypeMap(map);
        }

        public void setHoldability(int holdability) throws SQLException {
            tdhsConn.setHoldability(holdability);
        }

        public int getHoldability() throws SQLException {
            return tdhsConn.getHoldability();
        }

        public Savepoint setSavepoint() throws SQLException {
            return tdhsConn.setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return tdhsConn.setSavepoint(name);
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            tdhsConn.rollback(savepoint);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            tdhsConn.releaseSavepoint(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return tdhsConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                                  int resultSetHoldability) throws SQLException {
            return tdhsConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                             int resultSetHoldability) throws SQLException {
            return tdhsConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return tdhsConn.prepareStatement(sql, autoGeneratedKeys);
        }

        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return tdhsConn.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return tdhsConn.prepareStatement(sql, columnNames);
        }

        public Clob createClob() throws SQLException {
            return tdhsConn.createClob();
        }

        public Blob createBlob() throws SQLException {
            return tdhsConn.createBlob();
        }

        public NClob createNClob() throws SQLException {
            return tdhsConn.createNClob();
        }

        public SQLXML createSQLXML() throws SQLException {
            return tdhsConn.createSQLXML();
        }

        public boolean isValid(int timeout) throws SQLException {
            return tdhsConn.isValid(timeout);
        }

        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            tdhsConn.setClientInfo(name, value);
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            tdhsConn.setClientInfo(properties);
        }

        public String getClientInfo(String name) throws SQLException {
            return tdhsConn.getClientInfo(name);
        }

        public Properties getClientInfo() throws SQLException {
            return tdhsConn.getClientInfo();
        }

        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return tdhsConn.createArrayOf(typeName, elements);
        }

        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return tdhsConn.createStruct(typeName, attributes);
        }

        public void setSchema(String schema) throws SQLException {
            tdhsConn.setSchema(schema);
        }

        public String getSchema() throws SQLException {
            return tdhsConn.getSchema();
        }

        public void abort(Executor executor) throws SQLException {
            tdhsConn.abort(executor);
        }

        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            tdhsConn.setNetworkTimeout(executor, milliseconds);
        }

        public int getNetworkTimeout() throws SQLException {
            return tdhsConn.getNetworkTimeout();
        }

        public <T> T unwrap(Class<T> iface) throws SQLException {
            return tdhsConn.unwrap(iface);
        }

        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return tdhsConn.isWrapperFor(iface);
        }
    }
}
