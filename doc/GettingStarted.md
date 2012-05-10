#GettingStarted


##Simple new a client

    import com.taobao.tdhs.client.TDHSClient
    import com.taobao.tdhs.client.TDHSClientImpl
    //connect to t-wentong:9999 and 2-connections pool
    TDHSClient client = new TDHSClientImpl(new InetSocketAddress("t-wentong", 9999), 2);

TDHSClient is a main class to talk with TDH_Socket , it is thread-safe.

##Query Data

    TDHSResponse r = client.get(db, table, index, new String[] { "name" },
    				new String[][] { { "aaa" } }, FindFlag.TDHS_EQ, 0, 100,
    				new Filter[] { f });

return TDHSResponse and can access TDHSResponse.getResultSet() to get result as a java.sql.ResultSet.


##Count Data Number

    TDHSResponse r = client.count(db, table, index, new String[][] { { "1" } },
                    FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });

get the count of data which meet the conditions.


##Insert Data

    TDHSResponse r = client.insert(db, table,
    					new String[] { "id" }, new String[] { "0" });

return the primary key if the primary key is AUTO_INCREMENT.

##Update Data

    TDHSResponse r = client.update(db, table, null,
					new String[] { fields[0] }, new ValueEntry[] { entry },
					new String[][] { { id } }, FindFlag.TDHS_EQ, 0, 100, null);

update the data , return the number of data which changed and queried.

##Delete Data

    TDHSResponse r = client.delete(db, table, index, new String[][] { { "1" } },
                        FindFlag.TDHS_EQ, 0, 100, new Filter[] { f });

delete the datas ,return the number of data which deleted.

##Easy API

TDHS-Java-Client has a easy api to use.

* get
``TDHSResponse response = client.query().use("benchmark").from("test_0001")
                   .select("id", "k", "i", "c")
                   .where().fields("i").equal("1").get()``
* count
``TDHSResponse r = client.query().use(db).from(table).where().lessEqual("1989800").and().field("id").greaterThan(
                  "1000").count();``
* insert
``TDHSResponse response = client.createStatement(end).insert().use("benchmark_insert").from(table)
            .value("k", "10000")
            .value("i", "1")
            .value("c", "_abcdefghijklmnopqrstuvwxyz").insert()``
* update
``TDHSResponse response = client.query().use("benchmark").from(table)
            .set().field("kc").add(1)
            .where().index("idx_i").equal("1").update()``
* delete
``TDHSResponse r = client.query().use(db).from(table)
  				.where().in(new String[] { "100" }).delete();``

##Statement
TDHSClient has two type statement: the common Statement and the BatchStatement
The statment is **not** thread-safe

###Statement

      TDHSResponse response = client.createStatement(3).query().use("benchmark").from("test_0001")
              .select("id", "k", "i", "c")
              .where().fields("i").equal(_id.toString()).get()

query data by createStatement() , the parameter of createStatement() mean the hashcode for assign the thread which server-side to run.

###BatchStatement

      BatchStatement b = client.createBatchStatement()
      b.insert().use("test").from("test")
              .value("data", v.getAndIncrement().toString() + "_aaa")
              .insert()
      b.insert().use("test").from("test")
              .value("data", v.getAndIncrement().toString() + "_bbb")
              .insert()
      b.insert().use("test").from("test")
              .value("data", v.getAndIncrement().toString() + "_ccc")
              .insert()
      b.commit()

use BatchStatement to make sure the sub request can be done in one transaction.

##JDBC

TDHS-Java-Client also supported JDBC drvier.

     String url = "jdbc:tdhs://t-wentong:9999/jdbc_test";
     Class.forName("com.taobao.tdhs.jdbc.Driver");
     Connection connection = DriverManager.getConnection(url, null, null)
     Statement statement = connection.createStatement();
     ResultSet resultSet = null;
     try {
         boolean r = statement.execute("select p.id,p.name,p.age,p.memo as `m` from person as p where id>0");
         Assert.assertTrue(r);
         resultSet = statement.getResultSet();
         int size = 0;
         while (resultSet.next()) {
             Assert.assertEquals(data[size][0], resultSet.getInt(1));
             Assert.assertEquals(data[size][1], resultSet.getString(2));
             Assert.assertEquals(data[size][2], resultSet.getInt("age"));
             Assert.assertEquals(data[size][3], resultSet.getString("m"));
         }
     } finally {
         if (resultSet != null) {
             resultSet.close();
         }
         if (statement != null) {
             statement.close();
         }
         connection.close();
     }

