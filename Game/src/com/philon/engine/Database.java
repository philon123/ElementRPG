package com.philon.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    Connection connection = null;
    Statement statement;

  public Database(String dbPath) {
  	try {
  	    Class.forName("org.sqlite.JDBC"); //this initalizes the class through the current class loader
  	} catch (ClassNotFoundException e) {
  	    throw new RuntimeException("Couldn't find JDBC driver class");
  	}

  	if (!getConnection(dbPath)) {
  	    throw new RuntimeException("Failed to load DB, probably wrong path: " + dbPath);
  	}
  }

  private boolean getConnection(String absPath) {
    try {
	    connection = DriverManager.getConnection("jdbc:sqlite:" + absPath);
	    statement = connection.createStatement();
	    statement.setQueryTimeout(30);  // set timeout to 30 sec.

	    return true;
    } catch (SQLException e) {
	    e.printStackTrace();
	    try {
	      if(connection != null)
	        connection.close();
	    } catch(SQLException ee) {
	      // connection close failed.
	      System.err.println(ee);
	    }
	    return false;
    }
  }

  public Object[][] execQuery(String sql) {
  	ResultSet rs;
  	ArrayList<ArrayList<Object>> result0 = new ArrayList<ArrayList<Object>>();
  	try {

      rs = statement.executeQuery(sql);
      int numRows=0;
      while(rs.next()) {
        numRows++;
        result0.add(new ArrayList<Object>());
        for (int currColumn=1; currColumn<=rs.getMetaData().getColumnCount(); currColumn++) {
          result0.get(numRows-1).add(rs.getObject(currColumn));
        }
      }
      if (numRows==0) return new Object[0][0];

      ArrayList<Object[]> result1 = new ArrayList<Object[]>();
      for (ArrayList<Object> currList : result0) {
        result1.add(currList.toArray());
      }

      Object[][] result2 = new Object[numRows][];
      for (int i=0; i<result1.size(); i++) {
        result2[i] = result1.get(i);
      }

      return result2;

  	} catch (SQLException e) {
  	    System.err.println("Bad SQL: " + sql);
  	    e.printStackTrace();
  	    throw new RuntimeException("SQL Query failed. Probably bad syntax");
  	}
  }
}
