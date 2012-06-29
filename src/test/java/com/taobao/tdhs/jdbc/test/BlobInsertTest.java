package com.taobao.tdhs.jdbc.test;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-6-29 下午2:35
 */
public class BlobInsertTest extends TestBase {

    private static byte[][] INSERT_DATA = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}, {0, 9, 8, 7, 6, 5, 4, 3, 2, 1},
            {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                    'v', 'w', 'x', 'y', 'z'},
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'}};

    private static byte[][] UPDATE_DATA =
            {{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'},
                    {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                            'u',
                            'v', 'w', 'x', 'y', 'z'}, {0, 9, 8, 7, 6, 5, 4, 3, 2, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 0}

            };

    @BeforeClass
    public static void init() throws ClassNotFoundException, SQLException, IOException {
        truncate("blob_test");
        Connection connection = getTDHSConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into blob_test (d) values (?)");
        for (byte[] o : INSERT_DATA) {
            preparedStatement.setBytes(1, o);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        connection.close();

    }

    public List<byte[]> executeSelect(Connection connection, String sql) throws SQLException {
        List<byte[]> ret = new ArrayList<byte[]>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute(sql);
            assertTrue(r);
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                ret.add(resultSet.getBytes(1));
            }
            return ret;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }


    public List<String> executeSelectForId(Connection connection, String sql) throws SQLException {
        List<String> ret = new ArrayList<String>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;
        try {
            boolean r = statement.execute(sql);
            assertTrue(r);
            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                ret.add(resultSet.getString(1));
            }
            return ret;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            connection.close();
        }
    }

    private void compareRecord(List<byte[]> mysqlRecord, List<byte[]> tdhsRecord) {
        assertEquals(mysqlRecord.size(), tdhsRecord.size());
        for (int i = 0; i < mysqlRecord.size(); i++) {
            assertArrayEquals(mysqlRecord.get(i), tdhsRecord.get(i));
        }
    }

    @Test
    public void testInsertDone() throws ClassNotFoundException, SQLException {
        List<byte[]> ret = executeSelect(getTDHSConnection(), "select d from blob_test where id >-1");
        compareRecord(Arrays.asList(INSERT_DATA), ret);
    }

    @Test
    public void testInsertDone2() throws ClassNotFoundException, SQLException {
        List<byte[]> ret1 = executeSelect(getTDHSConnection(), "select d from blob_test where id >-1");
        List<byte[]> ret2 = executeSelect(getMySQLConnection(), "select d from blob_test where id >-1");
        compareRecord(ret2, ret1);
    }

}
