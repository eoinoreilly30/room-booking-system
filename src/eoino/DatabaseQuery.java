package eoino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseQuery {
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	final private String url = "jdbc:oracle:thin:@ee417.c7clh2c6565n.eu-west-1.rds.amazonaws.com:1521:EE417";
    final private String dbUsername = "ee_user";
    final private String dbPassword = "ee_pass";
    
	public ResultSet makeQuery(String query) {
    	try {
    		Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, dbUsername, dbPassword);
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return rs;
	}
	
	public void closeConnections() {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (con != null) con.close();
		}
		catch (Exception ex) {
			System.out.println("An error occurred while closing down connection/statement");
        }
	}
}
